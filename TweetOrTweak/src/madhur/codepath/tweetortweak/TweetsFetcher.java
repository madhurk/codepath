package madhur.codepath.tweetortweak;

import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsFetcher extends JsonHttpResponseHandler {
  
  public static final int FETCH_HOME_TWEETS = 0;
  public static final int FETCH_OLD_TWEETS = 1;
  public static final int FETCH_NEW_TWEETS = 2;

  public static final int TWEET_TYPE_HOME = 0;
  public static final int TWEET_TYPE_MENTIONS = 1;
  
  private Context context;
  private long oldestTweetId=-1;
  private long newestTweetId=-1;
  private int mode;
  private int tweetType = -1;
  TwitterClient client;  
  TweetViewListener tweetViewListener;
  
  public TweetsFetcher(Context context, TwitterClient client,  int tweetType,
      TweetViewListener tweetViewListener){
    this.context = context;
    this.tweetType = tweetType;
    this.tweetViewListener = tweetViewListener;
    this.client = client;
  }
  
  public synchronized List<Tweet> loadSavedTweets(){
    List<Tweet> savedTweets = Tweet.getSavedTweets();
    if(savedTweets != null && savedTweets.size() > 0){
      
      Tweet lastTweet = savedTweets.get(savedTweets.size()-1);
      oldestTweetId = lastTweet.getTweetId();
      
      Tweet firstTweet = savedTweets.get(0);
      newestTweetId = firstTweet.getTweetId();
      
      return savedTweets;
    }
    
    return null;
  }
    
  
  public synchronized void fetch(int mode){
    
    //String params = oldestTweetId + "&&" + newestTweetId;
    //Toast.makeText(context, "Got fetch request with mode ="+mode+"&"+params, Toast.LENGTH_SHORT).show();

    this.mode = mode;
    
    if(mode == FETCH_HOME_TWEETS){
      if(tweetType == TWEET_TYPE_HOME)
        client.getHomeTimeline(-1, -1, context, this);
    }else if(mode == FETCH_NEW_TWEETS){
      if(tweetType == TWEET_TYPE_HOME)
        client.getHomeTimeline(-1, newestTweetId, context, this);
    }else if(mode == FETCH_OLD_TWEETS){
      if(oldestTweetId > 0){
        if(tweetType == TWEET_TYPE_HOME)
          client.getHomeTimeline(oldestTweetId, -1, context, this);
      }
    }
  }
  
  @Override
  public void onFailure(Throwable e, String s) {
    Toast.makeText(context, "Error fetching tweets", Toast.LENGTH_SHORT).show();
    Log.d("debug", e.toString());
    Log.d("debug", s);
    
    if(mode == FETCH_NEW_TWEETS){
      tweetViewListener.handleRefreshComplete();
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
      
      if(mode == FETCH_HOME_TWEETS){
        Tweet lastTweet = fetchedTweets.get(fetchedTweets.size()-1);
        oldestTweetId = lastTweet.getTweetId();
        
        Tweet firstTweet = fetchedTweets.get(0);
        newestTweetId = firstTweet.getTweetId();  
        
        tweetViewListener.replaceTweets(fetchedTweets);
      }
      
      if(mode == FETCH_OLD_TWEETS){
        Tweet lastTweet = fetchedTweets.get(fetchedTweets.size()-1);
        oldestTweetId = lastTweet.getTweetId();
        
        tweetViewListener.appendTweets(fetchedTweets);
      }
      
      if(mode == FETCH_NEW_TWEETS){
        Tweet firstTweet = fetchedTweets.get(0);
        newestTweetId = firstTweet.getTweetId();        
        
        tweetViewListener.prependTweets(fetchedTweets);
      }
    }
        
    if(mode == FETCH_NEW_TWEETS){
      tweetViewListener.handleRefreshComplete();
    }
  }
}
