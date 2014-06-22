package madhur.codepath.tweetortweak;

import org.json.JSONObject;

import madhur.codepath.tweetortweak.models.Tweet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    
    etCompose = (EditText)findViewById(R.id.etComposeTweet);
    client = TwitterApplication.getRestClient();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_compose, menu);
    return super.onCreateOptionsMenu(menu);
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
