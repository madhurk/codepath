package madhur.codepath.tweetortweak.fragments;

import madhur.codepath.tweetortweak.TweetsFetcher;
import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment{

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    tweetsFetcher = new TweetsFetcher(client, 0, TweetsFetcher.TWEET_TYPE_HOME, this);
    
    showProgressBar();
    tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
  }

}
