package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.models.Tweet;
import madhur.codepath.tweetortweak.models.User;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends ActionBarActivity {

  private TwitterClient client;
  EditText etCompose;  
  User self;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose);
        
    client = TwitterApplication.getRestClient();
    self = new User();
    populateSelfInfo();
        
    final TextView tvRemainingCharcount = (TextView)findViewById(R.id.tvRemaningCharCount);
    
    etCompose = (EditText)findViewById(R.id.etComposeTweet);
    etCompose.addTextChangedListener(new TextWatcher() {
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvRemainingCharcount.setText(String.valueOf(140-count));
      }
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      
      @Override
      public void afterTextChanged(Editable s) {
      }
    });    
  }
  
  private void populateSelfInfo(){
    if(!Utils.isNetworkAvailable(this)){
      Utils.toastNoNetwork(this);
      return;
    }
    
    client.getUserInfo(0, new JsonHttpResponseHandler(){
    @Override
    public void onFailure(Throwable e, String s) {
      Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onSuccess(JSONObject json){
      self = User.fromJSON(json);
      
      TextView tvFullName = (TextView)findViewById(R.id.tvComposeFullName);
      tvFullName.setText(self.getName());
      
      TextView tvScreenName = (TextView)findViewById(R.id.tvComposeScreenName);
      tvScreenName.setText(self.getScreenName());    
      
      ImageView ivProfileImg = (ImageView)findViewById(R.id.ivComposeProfileImg);
      ivProfileImg.setImageResource(android.R.color.transparent);
      ImageLoader loader = ImageLoader.getInstance();
      loader.displayImage(self.getProfileImg(), ivProfileImg);
    }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_compose, menu);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    
    switch (item.getItemId()) {
      case android.R.id.home:
        Intent intent = NavUtils.getParentActivityIntent(this); 
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        NavUtils.navigateUpTo(this, intent);
        return true;
        
      case R.id.action_send:
        onSend(item);
        return true;
    }
    
    return super.onOptionsItemSelected(item);
  }
  
  public void onSend(MenuItem mi){
    Tweet tweet = new Tweet();
    tweet.setBody(etCompose.getText().toString());
    
    if(!Utils.isNetworkAvailable(this)){
      Utils.toastNoNetwork(this);
      return;
    }
    
    client.sendTweet(tweet, new JsonHttpResponseHandler(){     
      
      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(getApplicationContext(), "Network error, try again", Toast.LENGTH_SHORT).show();
      }
      
      @Override
      public void onSuccess(JSONObject arg0) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
      }
    });
  }
}
