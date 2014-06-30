package madhur.codepath.tweetortweak;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import madhur.codepath.tweetortweak.fragments.TweetsListFragment;
import madhur.codepath.tweetortweak.models.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class TimelineActivity extends FragmentActivity {

  private TweetsListFragment fragmentTweetsList;
  private TwitterClient client;
  private User self;
  
  private static final int COMPOSE_REQUEST_CODE = 1;  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = TwitterApplication.getRestClient();
    populateSelfInfo();    
    setContentView(R.layout.activity_timeline);
        
//    // Begin the transaction
//    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//    // Replace the container with the new fragment
//    ft.replace(R.id.your_placeholder, new HomeTimelineFragment());
//    // or ft.add(R.id.your_placeholder, new FooFragment());
//    // Execute the changes specified
//    ft.commit();
//    
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
  
//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
//    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
//      tweetFetcher.fetch(TwitterClient.FETCH_NEW_TWEETS);
//    }
//  }  

}
