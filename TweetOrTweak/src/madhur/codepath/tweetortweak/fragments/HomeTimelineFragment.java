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
    
    tweetsFetcher = new TweetsFetcher(client, 0, Tweet.TYPE_HOME, this);
    
    if(Utils.isNetworkAvailable(getActivity())){
      showProgressBar();
      tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
    }else{
      List<Tweet> savedTweets = tweetsFetcher.loadSavedTweets(Tweet.TYPE_HOME);
      replaceTweets(savedTweets);
    }
  }  
}
