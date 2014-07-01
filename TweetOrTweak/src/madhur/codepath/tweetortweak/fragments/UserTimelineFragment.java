package madhur.codepath.tweetortweak.fragments;

import madhur.codepath.tweetortweak.TweetsFetcher;
import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    long userId = getArguments().getLong("user_id");
    tweetsFetcher = new TweetsFetcher(client, userId, TweetsFetcher.TWEET_TYPE_USER, this);    
    showProgressBar();
    tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
  }
  
  public static UserTimelineFragment newInstance(long userId) {
    UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
    Bundle args = new Bundle();
    args.putLong("user_id", userId);
    userTimelineFragment.setArguments(args);
    return userTimelineFragment;
  }
}
