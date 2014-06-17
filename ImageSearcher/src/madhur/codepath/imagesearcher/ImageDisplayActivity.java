package madhur.codepath.imagesearcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    
    Intent i = getIntent();
    ImageResult image = (ImageResult)i.getSerializableExtra("image");
    
    SmartImageView iv = (SmartImageView)findViewById(R.id.ivResult);
    iv.setImageUrl(image.fullUrl);
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

  // Call to update the share intent
  private void setShareIntent(Intent shareIntent) {
      if (mShareActionProvider != null) {
          mShareActionProvider.setShareIntent(shareIntent);
      }
  }
}
