package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.fragments.HomeTimelineFragment;
import madhur.codepath.tweetortweak.fragments.MentionsTimelineFragment;
import madhur.codepath.tweetortweak.fragments.TweetsListFragment;
import madhur.codepath.tweetortweak.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;


public class TimelineActivity extends ActionBarActivity {

  private TweetsListFragment fragmentTweetsList;
  private TwitterClient client;
  private User self;
  
  private static final int COMPOSE_REQUEST_CODE = 1;  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
    client = TwitterApplication.getRestClient();
    populateSelfInfo();    
    setContentView(R.layout.activity_timeline);
    setupTabs();
        
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
    
    MenuItem item = menu.findItem(R.id.action_compose);

    item.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener (){ 
      @Override
      public boolean onMenuItemClick(MenuItem item){
        onCompose(item);
        return true;
      }
    }); 
    
    return super.onCreateOptionsMenu(menu);
  }
  
  public void onCompose(MenuItem mi){
    Intent i = new Intent(this, ComposeActivity.class);
    i.putExtra("name", self.getName());
    i.putExtra("screen_name", self.getScreenName());
    i.putExtra("profile_image", self.getProfileImg());
    startActivityForResult(i, COMPOSE_REQUEST_CODE);
  }
  
  private void setupTabs() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setDisplayShowTitleEnabled(true);

    Tab tab1 = actionBar
        .newTab()
        .setText("Home")
        .setIcon(R.drawable.ic_home)
        .setTag("HomeTimelineFragment")
        .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
                    "home", HomeTimelineFragment.class));

    actionBar.addTab(tab1);
    actionBar.selectTab(tab1);

    Tab tab2 = actionBar
        .newTab()
        .setText("Mentions")
        .setIcon(R.drawable.ic_mentions)
        .setTag("MentionsTimelineFragment")
        .setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this,
                    "mentions", MentionsTimelineFragment.class));
    actionBar.addTab(tab2);
}

  
//  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
//    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
//      tweetFetcher.fetch(TwitterClient.FETCH_NEW_TWEETS);
//    }
//  }  

}
