package madhur.codepath.tweetortweak;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TweetsFetcher extends JsonHttpResponseHandler {
  
  private Context context;
  private TweetArrayAdapter aTweets;
  private long oldestTweetId=-1;
  private long newestTweetId=-1;
  private TwitterClient client;
  private PullToRefreshListView lvTweets;
  private int mode;
  
  public TweetsFetcher(Context context, TwitterClient client, 
      TweetArrayAdapter aTweets, PullToRefreshListView lvTweets){
    this.context = context;
    this.aTweets = aTweets;
    this.client = client;
    this.lvTweets = lvTweets;
  }
  
  public synchronized void loadSavedTweets(){
    List<Tweet> savedTweets = Tweet.getSavedTweets();
    if(savedTweets != null && savedTweets.size() > 0){
      aTweets.addAll(savedTweets);
      Tweet lastTweet = savedTweets.get(savedTweets.size()-1);
      oldestTweetId = lastTweet.getTweetId();
      
      Tweet firstTweet = savedTweets.get(0);
      newestTweetId = firstTweet.getTweetId();  
    }
  }
  
  public synchronized void fetch(int mode){
    
    String params = oldestTweetId + "&&" + newestTweetId;
    //Toast.makeText(context, "Got fetch request with mode ="+mode+"&"+params, Toast.LENGTH_SHORT).show();

    this.mode = mode;
    
    if(mode == TwitterClient.FETCH_HOME_TWEETS){
      client.getHomeTimeline(-1, -1, context, this);
    }else if(mode == TwitterClient.FETCH_NEW_TWEETS){
      client.getHomeTimeline(-1, newestTweetId, context, this);
    }else if(mode == TwitterClient.FETCH_OLD_TWEETS){
      if(oldestTweetId > 0)
        client.getHomeTimeline(oldestTweetId, -1, context, this);
    }
  }
  
  @Override
  public void onFailure(Throwable e, String s) {
    Toast.makeText(context, "Error fetching tweets", Toast.LENGTH_SHORT).show();
    Log.d("debug", e.toString());
    Log.d("debug", s);
    
    if(mode == TwitterClient.FETCH_NEW_TWEETS){
      lvTweets.onRefreshComplete();
    }
  }
  
  @Override
  public synchronized void onSuccess(int returnCode, JSONArray json){
    
    if(returnCode != 200){
      Toast.makeText(context, "Error fetching tweets, code="+returnCode, Toast.LENGTH_SHORT).show();
      return;
    }
    
    List<Tweet> fetchedTweets = Tweet.fromJSONArray(json);
    //Toast.makeText(context, "Number of tweets fetched="+fetchedTweets.size(), Toast.LENGTH_SHORT).show();
    
    if(fetchedTweets.size() > 0){
      
      if(mode == TwitterClient.FETCH_HOME_TWEETS){
        Tweet lastTweet = fetchedTweets.get(fetchedTweets.size()-1);
        oldestTweetId = lastTweet.getTweetId();
        
        Tweet firstTweet = fetchedTweets.get(0);
        newestTweetId = firstTweet.getTweetId();  
        
        aTweets.clear();
        aTweets.addAll(fetchedTweets);
      }
      
      if(mode == TwitterClient.FETCH_OLD_TWEETS){
        Tweet lastTweet = fetchedTweets.get(fetchedTweets.size()-1);
        oldestTweetId = lastTweet.getTweetId();
        
        aTweets.addAll(fetchedTweets);
      }
      
      if(mode == TwitterClient.FETCH_NEW_TWEETS){
        Tweet firstTweet = fetchedTweets.get(0);
        newestTweetId = firstTweet.getTweetId();        
        
        List<Tweet> allTweets = new ArrayList<Tweet>(fetchedTweets.size() + aTweets.getCount());
        allTweets.addAll(fetchedTweets);
        for(int i = 0; i < aTweets.getCount(); ++i){
          allTweets.add(aTweets.getItem(i));
        }
        
        aTweets.clear();
        aTweets.addAll(allTweets);        
      }
    }
        
    if(mode == TwitterClient.FETCH_NEW_TWEETS){
      lvTweets.onRefreshComplete();
    }
  }
}
