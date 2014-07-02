package madhur.codepath.tweetortweak.fragments;

import java.util.List;

import madhur.codepath.tweetortweak.TweetsFetcher;
import madhur.codepath.tweetortweak.Utils;
import madhur.codepath.tweetortweak.models.Tweet;
import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment{

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    tweetsFetcher = new TweetsFetcher(client, 0, TweetsFetcher.TWEET_TYPE_HOME, this);
    
    showProgressBar();
    
    if(Utils.isNetworkAvailable(getActivity())){
      tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
    }else{
      List<Tweet> savedTweets = tweetsFetcher.loadSavedTweets();
      replaceTweets(savedTweets);
    }
  }
  
  public void fetchNewTweets(){
    tweetsFetcher.fetch(TweetsFetcher.FETCH_NEW_TWEETS);
  }
  
  public void saveTweets() {
    List<Tweet> currentTweets = getCurrentTweets() ;
    if(currentTweets != null){
      for(int i = 0; i < currentTweets.size(); ++i){
        Tweet tweet = currentTweets.get(i);
        tweet.getUser().save();
        tweet.save();
      }
    }
  }

}
