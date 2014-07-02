package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.fragments.UserTimelineFragment;
import madhur.codepath.tweetortweak.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends ActionBarActivity {

  User u;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
    setContentView(R.layout.activity_profile);
    Intent i = getIntent();
    
    long userId = 0;
    if(i.getExtras() != null){
      userId = i.getExtras().getLong("user_id");
    }
    
    if(Utils.isNetworkAvailable(this)){
      setProgressBarIndeterminateVisibility(true);
      loadProfileInfo(userId);
    }else{
      Utils.toastNoNetwork(this);
    }
  }
  
  
  private void loadProfileInfo(long userId){
    TwitterApplication.getRestClient().getUserInfo(userId, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(JSONObject json) {
        u = User.fromJSON(json);
        getActionBar().setTitle("@" + u.getScreenName());
        populateProfileHeader();
        setProgressBarIndeterminateVisibility(false);
        
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        UserTimelineFragment tweetsFragment = UserTimelineFragment.newInstance(u.getUserId());
        ft.replace(R.id.flUserTweetsContainer, tweetsFragment);
        ft.commit();
      }
    });
  }
  
  private void populateProfileHeader(){
    TextView tvName = (TextView)findViewById(R.id.tvProfileName);
    TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
    TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
    TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);    
    ImageView ivProfileImg = (ImageView)findViewById(R.id.ivProfileImage);
    
    ImageLoader.getInstance().displayImage(u.getProfileImg(), ivProfileImg);
    
    tvName.setText(u.getName());
    tvTagline.setText(u.getDescription());
    tvFollowers.setText(u.getFollowersCount() + " Followers");
    tvFollowing.setText(u.getFollowingCount() + " Following");
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent intent = NavUtils.getParentActivityIntent(this); 
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        NavUtils.navigateUpTo(this, intent);
        return true;        
    }
    
    return super.onOptionsItemSelected(item);
  }
}
