package madhur.codepath.tweetortweak;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import madhur.codepath.tweetortweak.models.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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
    TextView tvFullName = (TextView)tweetView.findViewById(R.id.tvFullName);
    TextView tvBody = (TextView)tweetView.findViewById(R.id.tvBody);
    TextView tvTimestamp = (TextView)tweetView.findViewById(R.id.tvTimestamp);
    
    ivProfileImageView.setImageResource(android.R.color.transparent);
    ImageLoader loader = ImageLoader.getInstance();
    loader.displayImage(tweet.getUser().getProfileImg(), ivProfileImageView);
    
    tvScreenName.setText("@" + tweet.getUser().getScreenName());
    tvBody.setText(tweet.getBody());
    tvFullName.setText(tweet.getUser().getName());
    
    String createdAt = tweet.getCreatedAt();
    String ts = getTs(createdAt);
    if(ts != null)
      tvTimestamp.setText(ts);
    else
      tvTimestamp.setText("");
    
    return tweetView;
  }

  private String getTs(String createdAt) {
    
    try{
      DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
      Date created =  df.parse(createdAt);
      Date current = new Date(); 
      long diff = current.getTime()-created.getTime();
      return getFriendlyTs(diff);
    }catch (java.text.ParseException e) {
      e.printStackTrace();
      return null;
    }    
  }
  
  private String getFriendlyTs(long millis){
    long secs = millis / 1000;
    if(secs < 60){
      return secs+"s";
    }
    long mins = secs / 60;
    if(mins < 60){
      return mins+"m";
    }
    long hours = mins / 60;
    if(hours < 24){
      return hours+"h";
    }
    long days = hours/24;
    return days+"d";
  }
}
