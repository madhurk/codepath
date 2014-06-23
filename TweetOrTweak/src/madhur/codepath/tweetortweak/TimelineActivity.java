package madhur.codepath.tweetortweak;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;
import madhur.codepath.tweetortweak.models.User;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

  private TwitterClient client;
  private TweetsFetcher tweetFetcher;
  private List<Tweet> tweets;
  private TweetArrayAdapter aTweets;
  private PullToRefreshListView lvTweets; 
  private User self;

  private static final int COMPOSE_REQUEST_CODE = 1;  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
        
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdapter(this, tweets);
    
    client = TwitterApplication.getRestClient();
    
    lvTweets = (PullToRefreshListView)findViewById(R.id.lvTweets);
    lvTweets.setAdapter(aTweets);

    tweetFetcher = new TweetsFetcher(this, client, aTweets, lvTweets);
    
    if(isNetworkAvailable()){
      tweetFetcher.fetch(TwitterClient.FETCH_HOME_TWEETS);
      populateSelfInfo();
    }else{
      List<Tweet> savedTweets = Tweet.getSavedTweets();
      if(savedTweets != null && savedTweets.size() > 0){
        aTweets.addAll(savedTweets);
        Toast.makeText(this, "Internet not available, showing old tweets", Toast.LENGTH_SHORT).show();
      }else{
        toastNoNetwork();
      }
    }

    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        if(isNetworkAvailable()){
          tweetFetcher.fetch(TwitterClient.FETCH_OLD_TWEETS);
        }else{
          toastNoNetwork();
        }
      }
    });
    
    lvTweets.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        if(isNetworkAvailable()){
          tweetFetcher.fetch(TwitterClient.FETCH_NEW_TWEETS);
        }else{
          lvTweets.onRefreshComplete();
          toastNoNetwork();
        }
      }
    });
  }
  
  private void populateSelfInfo(){
    client.getSelfInfo(new JsonHttpResponseHandler(){
      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(getApplicationContext(), "Error getting Json response", Toast.LENGTH_SHORT).show();
        Log.d("debug", e.toString());
        Log.d("debug", s);
      }
      
      @Override
      public void onSuccess(JSONObject json){
        self = User.fromJSON(json);
      }
    });
  }
  
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_timeline, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  public void onCompose(MenuItem mi){
    if(self != null){
      Intent i = new Intent(this, ComposeActivity.class);
      i.putExtra("name", self.getName());
      i.putExtra("screen_name", self.getScreenName());
      i.putExtra("profile_image", self.getProfileImg());
      startActivityForResult(i, COMPOSE_REQUEST_CODE);
    }
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      tweetFetcher.fetch(TwitterClient.FETCH_NEW_TWEETS);
    }
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
  
  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
  
  private void toastNoNetwork(){
    Toast.makeText(this, "Sorry, Internet connection is not available", Toast.LENGTH_SHORT).show();
  }
}
