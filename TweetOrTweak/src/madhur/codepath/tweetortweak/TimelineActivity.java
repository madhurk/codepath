package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.fragments.HomeTimelineFragment;
import madhur.codepath.tweetortweak.fragments.MentionsTimelineFragment;
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

  private TwitterClient client;
  private User self;
  HomeTimelineFragment homeTimelineFragment = null;
  
  private static final int COMPOSE_REQUEST_CODE = 1;
  
  private static final String FRAGMENT_HOME_TAG="home";
  private static final String FRAGMENT_MENTIONS_TAG="mentions";
  
  private static final String TAB_HOME_TAG="HomeTimelineTab";
  private static final String TAB_MENTIONS_TAG="MentionsTimelineTab";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
    client = TwitterApplication.getRestClient();
    populateSelfInfo();    
    setContentView(R.layout.activity_timeline);
    
    String currTab = null;
    if(savedInstanceState != null)
      currTab = savedInstanceState.getString("current_tab");
    setupTabs(currTab);       
    
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    ActionBar actionBar = getSupportActionBar();
    Tab currTab = actionBar.getSelectedTab();
    outState.putString("current_tab", (String)currTab.getTag());
  }
  
  private void populateSelfInfo(){
    client.getUserInfo(0, new JsonHttpResponseHandler(){
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
    
    item = menu.findItem(R.id.action_profile);

    item.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener (){ 
      @Override
      public boolean onMenuItemClick(MenuItem item){
        onProfileView(item);
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
  
  public void onProfileView(MenuItem mi){
    Intent i = new Intent(this, ProfileActivity.class);
    startActivity(i);
  }
  
  private void setupTabs(String currTab) {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setDisplayShowTitleEnabled(true);

    Tab homeTab = actionBar
        .newTab()
        .setText("Home")
        .setIcon(R.drawable.ic_home)
        .setTag(TAB_HOME_TAG)
        .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
            FRAGMENT_HOME_TAG, HomeTimelineFragment.class));

    Tab mentionsTab = actionBar
        .newTab()
        .setText("Mentions")
        .setIcon(R.drawable.ic_mentions)
        .setTag(TAB_MENTIONS_TAG)
        .setTabListener(new SupportFragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this,
            FRAGMENT_MENTIONS_TAG, MentionsTimelineFragment.class));

    if(currTab == null || currTab.equals(TAB_HOME_TAG)){
      actionBar.addTab(homeTab, 0, true);
      actionBar.addTab(mentionsTab, 1, false);
    }else if(currTab.equals(TAB_MENTIONS_TAG)){
      actionBar.addTab(homeTab, 0, false);
      actionBar.addTab(mentionsTab, 1, true);      
    }
  }

  private HomeTimelineFragment getHomeTimelineFragment(){
    
    if(homeTimelineFragment == null){
      homeTimelineFragment = (HomeTimelineFragment)
        getSupportFragmentManager().findFragmentByTag(FRAGMENT_HOME_TAG);
    }
    
    return homeTimelineFragment;
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      HomeTimelineFragment fragment = getHomeTimelineFragment();
      fragment.fetchNewTweets();
    }
  }  
  
  @Override
  protected void onStop() {
    HomeTimelineFragment fragment = getHomeTimelineFragment();
    fragment.saveTweets();
    super.onStop();
  }
}
