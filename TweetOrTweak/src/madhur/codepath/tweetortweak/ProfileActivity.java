package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends ActionBarActivity {

  User u;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    
    loadProfileInfo();
  }
  
  
  private void loadProfileInfo(){
    TwitterApplication.getRestClient().getSelfInfo(new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(JSONObject json) {
        u = User.fromJSON(json);
        getActionBar().setTitle("@" + u.getScreenName());
        populateProfileHeader();
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
