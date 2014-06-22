package madhur.codepath.tweetortweak;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;

import madhur.codepath.tweetortweak.models.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
  
  public TweetArrayAdapter(Context context, List<Tweet> tweets){
    super(context, 0, tweets);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = getItem(position);
    
    View tweetView;
    if(convertView == null){
      LayoutInflater inflator = LayoutInflater.from(getContext());
      tweetView = inflator.inflate(R.layout.tweet_item, parent, false);
    }else{
      tweetView = convertView;
    }
    
    ImageView ivProfileImageView = (ImageView)tweetView.findViewById(R.id.ivProfileImage);
    TextView tvScreenName = (TextView)tweetView.findViewById(R.id.tvScreenName);
    TextView tvBody = (TextView)tweetView.findViewById(R.id.tvBody);
    
    ivProfileImageView.setImageResource(android.R.color.transparent);
    ImageLoader loader = ImageLoader.getInstance();
    loader.displayImage(tweet.getUser().getProfileImg(), ivProfileImageView);
    
    tvScreenName.setText(tweet.getUser().getScreenName());
    tvBody.setText(tweet.getBody());
    
    return tweetView;
  }
}
