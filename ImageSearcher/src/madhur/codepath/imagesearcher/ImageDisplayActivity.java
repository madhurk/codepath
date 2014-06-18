package madhur.codepath.imagesearcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

  private ShareActionProvider mShareActionProvider;
  private String fullUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_display);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    Intent i = getIntent();
    ImageResult image = (ImageResult)i.getSerializableExtra("image");
    
    int notFoundImgId = getResources().getIdentifier("default_image" , 
        "drawable", getPackageName());

    SmartImageView iv = (SmartImageView)findViewById(R.id.ivResult);
    iv.setImageUrl(image.fullUrl, notFoundImgId);
    fullUrl = image.fullUrl;
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate menu resource file.
      getMenuInflater().inflate(R.menu.display_menu, menu);

      // Locate MenuItem with ShareActionProvider
      MenuItem item = menu.findItem(R.id.action_share);

      // Fetch and store ShareActionProvider
      mShareActionProvider = (ShareActionProvider) item.getActionProvider();
      
      Intent myIntent = new Intent();
      myIntent.setAction(Intent.ACTION_SEND);
      myIntent.putExtra(Intent.EXTRA_TEXT, fullUrl );
      myIntent.setType("text/plain");

      mShareActionProvider.setShareIntent(myIntent);

      // Return true to display menu
      return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        Intent intent = NavUtils.getParentActivityIntent(this); 
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        NavUtils.navigateUpTo(this, intent);
        return true;        
    }
    
    return super.onOptionsItemSelected(item);
  }

  // Call to update the share intent
  private void setShareIntent(Intent shareIntent) {
      if (mShareActionProvider != null) {
          mShareActionProvider.setShareIntent(shareIntent);
      }
  }
}
