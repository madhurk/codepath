package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.fragments.HomeTimelineFragment;
import madhur.codepath.tweetortweak.fragments.MentionsTimelineFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;


public class TimelineActivity extends ActionBarActivity {

  HomeTimelineFragment homeTimelineFragment = null;
  MentionsTimelineFragment mentionsTimelineFragment = null;
  
  private static final int COMPOSE_REQUEST_CODE = 1;
  
  private static final String FRAGMENT_HOME_TAG="home";
  private static final String FRAGMENT_MENTIONS_TAG="mentions";
  
  private static final String TAB_HOME_TAG="HomeTimelineTab";
  private static final String TAB_MENTIONS_TAG="MentionsTimelineTab";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
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

    Display mDisplay= getWindowManager().getDefaultDisplay();
    int rotation = mDisplay.getRotation();
    
    int homeIcon = R.drawable.ic_home;
    int mentionsIcon = R.drawable.ic_mentions;
    
    if(rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270){
      homeIcon = R.drawable.ic_home_landscape;
      mentionsIcon = R.drawable.ic_mentions_landscape;
    }
    
    Tab homeTab = actionBar
        .newTab()
        .setText("Home")
        .setIcon(homeIcon)
        .setTag(TAB_HOME_TAG)
        .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
            FRAGMENT_HOME_TAG, HomeTimelineFragment.class));
    
    Tab mentionsTab = actionBar
        .newTab()
        .setText("Mentions")
        .setIcon(mentionsIcon)
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
  
  private MentionsTimelineFragment getMentionsTimelineFragment(){
    
    if(mentionsTimelineFragment == null){
      mentionsTimelineFragment = (MentionsTimelineFragment)
        getSupportFragmentManager().findFragmentByTag(FRAGMENT_MENTIONS_TAG);
    }
    
    return mentionsTimelineFragment;
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
    if (resultCode == RESULT_OK && requestCode == COMPOSE_REQUEST_CODE) {
      HomeTimelineFragment fragment = getHomeTimelineFragment();
      fragment.fetchNewTweets();
    }
  }  
  
  @Override
  protected void onStop() {
    HomeTimelineFragment homeFrag = getHomeTimelineFragment();
    if(homeFrag != null)
      homeFrag.saveTweets();
    
    MentionsTimelineFragment mentionsFrag = getMentionsTimelineFragment();
    if(mentionsFrag != null)
      mentionsFrag.saveTweets();
    
    super.onStop();
  }  
}
