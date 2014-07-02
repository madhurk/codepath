package madhur.codepath.tweetortweak.fragments;

import madhur.codepath.tweetortweak.TweetsFetcher;
import madhur.codepath.tweetortweak.Utils;
import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    tweetsFetcher = new TweetsFetcher(client, 0, TweetsFetcher.TWEET_TYPE_MENTIONS, this);
    
    if(Utils.isNetworkAvailable(getActivity())){
      showProgressBar();
      tweetsFetcher.fetch(TweetsFetcher.FETCH_ALL_TWEETS);
    }else{
      Utils.toastNoNetwork(getActivity());
    }
  }
}
