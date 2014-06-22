package madhur.codepath.tweetortweak;

import java.util.ArrayList;
import java.util.List;

import madhur.codepath.tweetortweak.models.Tweet;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

  private TwitterClient client;
  private List<Tweet> tweets;
  //private ArrayAdapter<Tweet> aTweets;
  private TweetArrayAdapter aTweets;
  private ListView lvTweets; 
  long lastLoadedId = 0;
  
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
    //aTweets = new ArrayAdapter<Tweet>(this, android.R.layout.simple_list_item_1, tweets);
    aTweets = new TweetArrayAdapter(this, tweets);
    lvTweets.setAdapter(aTweets);
    
    populateTimeline(true);
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
