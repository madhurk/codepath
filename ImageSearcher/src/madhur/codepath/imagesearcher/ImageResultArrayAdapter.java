package madhur.codepath.imagesearcher;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

  public ImageResultArrayAdapter(Context context, List<ImageResult> imageResults) {
    super(context, R.layout.item_image_result, imageResults);    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageResult image = getItem(position);
    SmartImageView ivImage;
    
    if (convertView == null) {
      LayoutInflater inflator = LayoutInflater.from(getContext());
      ivImage = (SmartImageView)inflator.inflate(R.layout.item_image_result, parent, false);
    }else{
      ivImage = (SmartImageView)convertView;
      ivImage.setImageResource(android.R.color.transparent);
    }
    
    ivImage.setImageUrl(image.thumbUrl);
    return ivImage;
  }
  
}
