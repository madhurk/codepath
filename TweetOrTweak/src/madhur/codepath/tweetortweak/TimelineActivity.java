package madhur.codepath.tweetortweak;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;
import madhur.codepath.tweetortweak.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

  private TwitterClient client;
  private List<Tweet> tweets;
  private TweetArrayAdapter aTweets;
  private ListView lvTweets; 
  long lastLoadedId = 0;
  public static final int COMPOSE_REQUEST_CODE = 1;
  
  User self;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_timeline);
    client = TwitterApplication.getRestClient();
    
    lvTweets = (ListView)findViewById(R.id.lvTweets);
    lvTweets.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        populateTimeline(false);
      }
    });
    
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdapter(this, tweets);
    lvTweets.setAdapter(aTweets);
    
    populateTimeline(true);
    populateSelfInfo();
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
    Intent i = new Intent(this, ComposeActivity.class);
    i.putExtra("name", self.getName());
    i.putExtra("screen_name", self.getScreenName());
    i.putExtra("profile_image", self.getProfileImg());
    startActivityForResult(i, COMPOSE_REQUEST_CODE);
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      populateTimeline(true);
    }
  } 
  
  private void populateTimeline(boolean latest){
    long maxId = -1;
    if(!latest && lastLoadedId > 0)
      maxId = lastLoadedId;
    
    client.getHomeTimeline(maxId, getApplicationContext(), new JsonHttpResponseHandler(){
      
      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(getApplicationContext(), "Error getting Json response", Toast.LENGTH_SHORT).show();
        Log.d("debug", e.toString());
        Log.d("debug", s);
      }
      
      @Override
      public void onSuccess(JSONArray json){
        aTweets.addAll(Tweet.fromJSONArray(json));
        if(aTweets.getCount() > 0){
          Tweet lastTweet = aTweets.getItem(aTweets.getCount()-1);
          lastLoadedId = lastTweet.getId();
          //Toast.makeText(getApplicationContext(), "Last Id="+lastLoadedId, Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
