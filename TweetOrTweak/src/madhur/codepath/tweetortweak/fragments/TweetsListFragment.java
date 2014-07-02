package madhur.codepath.tweetortweak.fragments;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.EndlessScrollListener;
import madhur.codepath.tweetortweak.R;
import madhur.codepath.tweetortweak.TweetArrayAdapter;
import madhur.codepath.tweetortweak.TweetViewListener;
import madhur.codepath.tweetortweak.TweetsFetcher;
import madhur.codepath.tweetortweak.TwitterApplication;
import madhur.codepath.tweetortweak.TwitterClient;
import madhur.codepath.tweetortweak.models.Tweet;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment implements
  TweetViewListener{

  private List<Tweet> tweets;
  private TweetArrayAdapter aTweets;
  private PullToRefreshListView lvTweets; 
  
  protected TwitterClient client;
  protected TweetsFetcher tweetsFetcher;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {    
    
    super.onCreate(savedInstanceState);
    
    client =  TwitterApplication.getRestClient();
    
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdapter(getActivity(), tweets);
    
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    
    View v = inflater.inflate(R.layout.fragment_tweetslist, container, false);
    
    lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);
    
    
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
          refreshView();
          toastNoNetwork();
        }
      }
    });
    
    return v;  
  }
  
  
  public void setScrollListener(OnScrollListener listener){
    lvTweets.setOnScrollListener(listener);
  }
  
  public void setRefreshListener(OnRefreshListener listener){
    lvTweets.setOnRefreshListener(listener);
  }
    
  protected void clear(){
    aTweets.clear();
  }
  
  protected void append(List<Tweet> tweets){
    aTweets.addAll(tweets);
  }
  
  protected List<Tweet> getCurrentTweets(){
    return tweets;
  }
  
  protected void prepend(List<Tweet> tweets){
    List<Tweet> allTweets = new ArrayList<Tweet>(tweets.size() + aTweets.getCount());
    allTweets.addAll(tweets);
    for(int i = 0; i < aTweets.getCount(); ++i){
      allTweets.add(aTweets.getItem(i));
    }
    
    clear();
    append(allTweets);        
  }
  
  protected void refreshView(){
    lvTweets.onRefreshComplete();
  }
  
  
  protected void toastNoNetwork(){
    Toast.makeText(getActivity(), "Sorry, Internet connection is not available", Toast.LENGTH_SHORT).show();
  }  
  
  protected boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
  
  @Override
  public void appendTweets(List<Tweet> tweets) {
    append(tweets);
    hideProgressBar();
  }

  @Override
  public void replaceTweets(List<Tweet> tweets) {
    clear();
    append(tweets);
    hideProgressBar();
  }
  
  @Override
  public void prependTweets(List<Tweet> tweets) {    
    prepend(tweets);
    hideProgressBar();
  }

  @Override
  public void handleRefreshComplete() {
    refreshView();
    hideProgressBar();
  }

  @Override
  public void handleNetworkFailure() {
    Toast.makeText(getActivity(), "Error fetching tweets", Toast.LENGTH_SHORT).show();
    hideProgressBar();
  }
  
  // Should be called manually when an async task has started
  public void showProgressBar() {
      getActivity().setProgressBarIndeterminateVisibility(true); 
  }

  // Should be called when an async task has finished
  public void hideProgressBar() {
    getActivity().setProgressBarIndeterminateVisibility(false); 
  }
}
