package madhur.codepath.tweetortweak.fragments;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.EndlessScrollListener;
import madhur.codepath.tweetortweak.R;
import madhur.codepath.tweetortweak.TweetArrayAdapter;
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

public class TweetsListFragment extends Fragment {

  private List<Tweet> tweets;
  private TweetArrayAdapter aTweets;
  private PullToRefreshListView lvTweets; 

  @Override
  public void onCreate(Bundle savedInstanceState) {    
    
    super.onCreate(savedInstanceState);
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdapter(getActivity(), tweets);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    
    View v = inflater.inflate(R.layout.fragment_tweetslist, container, false);
    
    lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);
    
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
  public void onStop(){
    if(aTweets != null){
      for(int i = 0; i < aTweets.getCount(); ++i){
        Tweet tweet = aTweets.getItem(i);
        tweet.getUser().save();
        tweet.save();
      }
    }
    super.onStop();
  }
}
