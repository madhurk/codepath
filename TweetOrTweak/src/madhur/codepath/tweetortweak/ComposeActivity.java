package madhur.codepath.tweetortweak;

import org.json.JSONObject;

import madhur.codepath.tweetortweak.models.Tweet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

public class ComposeActivity extends Activity {

  private TwitterClient client;
  EditText etCompose;  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compose);
    
    Intent i = getIntent();
    String fullName = i.getStringExtra("name");
    String screenName = i.getStringExtra("screen_name");
    String image = i.getStringExtra("profile_image");
        
    TextView tvFullName = (TextView)findViewById(R.id.tvComposeFullName);
    tvFullName.setText(fullName);
    
    TextView tvScreenName = (TextView)findViewById(R.id.tvComposeScreenName);
    tvScreenName.setText(screenName);    
    
    ImageView ivProfileImg = (ImageView)findViewById(R.id.ivComposeProfileImg);
    ivProfileImg.setImageResource(android.R.color.transparent);
    ImageLoader loader = ImageLoader.getInstance();
    loader.displayImage(image, ivProfileImg);

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
    
    client = TwitterApplication.getRestClient();
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
    client.sendTweet(tweet, new JsonHttpResponseHandler(){     
      
      @Override
      public void onSuccess(JSONObject arg0) {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
      }
    });
  }
}
