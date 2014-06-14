package madhur.codepath.imagesearcher;

import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ImageDisplayActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_display);
    
    Intent i = getIntent();
    ImageResult image = (ImageResult)i.getSerializableExtra("image");
    
    SmartImageView iv = (SmartImageView)findViewById(R.id.ivResult);
    iv.setImageUrl(image.fullUrl);
  }
}
