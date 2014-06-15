package madhur.codepath.imagesearcher;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

  private static class ViewHolder {
    SmartImageView siv;
  }

  public ImageResultArrayAdapter(Context context, List<ImageResult> imageResults) {
    super(context, R.layout.item_image_result, imageResults);    
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageResult image = getItem(position);
    
    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater inflator = LayoutInflater.from(getContext());
      convertView = inflator.inflate(R.layout.item_image_result, parent, false);
      viewHolder.siv = (SmartImageView)convertView.findViewById(R.id.ivGridImage);
      convertView.setTag(viewHolder);
    }else{
      viewHolder = (ViewHolder)convertView.getTag();
    }
    
    viewHolder.siv.setImageUrl(image.thumbUrl);
    return convertView;
  }
  
}
