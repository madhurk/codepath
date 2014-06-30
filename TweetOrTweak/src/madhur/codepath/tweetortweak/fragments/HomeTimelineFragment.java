package madhur.codepath.tweetortweak.fragments;

import java.util.List;

import madhur.codepath.tweetortweak.EndlessScrollListener;
import madhur.codepath.tweetortweak.TweetViewListener;
import madhur.codepath.tweetortweak.TweetsFetcher;
import madhur.codepath.tweetortweak.TwitterApplication;
import madhur.codepath.tweetortweak.TwitterClient;
import madhur.codepath.tweetortweak.models.Tweet;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment 
  implements TweetViewListener{

  private TwitterClient client;
  private TweetsFetcher tweetsFetcher;
  
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client =  TwitterApplication.getRestClient();
    tweetsFetcher = new TweetsFetcher(getActivity(), client, 
        TweetsFetcher.TWEET_TYPE_HOME, this);
    
    tweetsFetcher.fetch(TweetsFetcher.FETCH_HOME_TWEETS);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    
    setScrollListener(new EndlessScrollListener() {      
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        if(isNetworkAvailable()){
          tweetsFetcher.fetch(TweetsFetcher.FETCH_OLD_TWEETS);
        }else{
          toastNoNetwork();
        }
      }
    });
    
    setRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        if(isNetworkAvailable()){
          tweetsFetcher.fetch(TweetsFetcher.FETCH_NEW_TWEETS);
        }else{
          handleRefreshComplete();
          toastNoNetwork();
        }
      }
    });
    return v;
  }
  

  @Override
  public void appendTweets(List<Tweet> tweets) {
    append(tweets);
  }

  @Override
  public void replaceTweets(List<Tweet> tweets) {
    clear();
    append(tweets);
  }
  
  @Override
  public void prependTweets(List<Tweet> tweets) {    
    prepend(tweets);
  }

  @Override
  public void handleRefreshComplete() {
    refreshView();
  }
}
