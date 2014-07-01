package madhur.codepath.tweetortweak;

import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsFetcher extends JsonHttpResponseHandler {
  
  public static final int FETCH_ALL_TWEETS = 0;
  public static final int FETCH_OLD_TWEETS = 1;
  public static final int FETCH_NEW_TWEETS = 2;

  public static final int TWEET_TYPE_HOME = 0;
  public static final int TWEET_TYPE_MENTIONS = 1;
  public static final int TWEET_TYPE_USER = 2;

 
  private long oldestTweetId=-1;
  private long newestTweetId=-1;
  private int mode;
  private int tweetType = -1;
  TwitterClient client;  
  TweetViewListener tweetViewListener;
  
  public TweetsFetcher(TwitterClient client,  int tweetType,
      TweetViewListener tweetViewListener){
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
    
    if(mode == FETCH_ALL_TWEETS){
      client.getTimeline(tweetType, -1, -1, this);
    }else if(mode == FETCH_NEW_TWEETS){
      client.getTimeline(tweetType, -1, newestTweetId, this);
    }else if(mode == FETCH_OLD_TWEETS){
      if(oldestTweetId > 0){
        client.getTimeline(tweetType, oldestTweetId, -1, this);
      }
    }
  }
  
  @Override
  public void onFailure(Throwable e, String s) {
    Log.d("debug", e.toString());
    Log.d("debug", s);
    tweetViewListener.handleNetworkFailure();
    
    if(mode == FETCH_NEW_TWEETS){
      tweetViewListener.handleRefreshComplete();
    }
  }
  
  @Override
  public synchronized void onSuccess(int returnCode, JSONArray json){
    
    if(returnCode != 200){
      return;
    }
    
    List<Tweet> fetchedTweets = Tweet.fromJSONArray(json);
    //Toast.makeText(context, "Number of tweets fetched="+fetchedTweets.size(), Toast.LENGTH_SHORT).show();
    
    if(fetchedTweets.size() > 0){
      
      if(mode == FETCH_ALL_TWEETS){
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
