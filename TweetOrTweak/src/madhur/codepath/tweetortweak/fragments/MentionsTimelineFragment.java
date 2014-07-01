package madhur.codepath.tweetortweak.fragments;

import madhur.codepath.tweetortweak.TweetsFetcher;
import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    tweetsFetcher = new TweetsFetcher(client, TweetsFetcher.TWEET_TYPE_MENTIONS, this);
    
    showProgressBar();
    tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
  }
}
