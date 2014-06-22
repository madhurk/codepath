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
  
  private static class ViewHolder {
    ImageView ivProfileImageView;
    TextView tvScreenName;
    TextView tvFullName;
    TextView tvBody;
    TextView tvTimestamp;    
  }

  public TweetArrayAdapter(Context context, List<Tweet> tweets){
    super(context, 0, tweets);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = getItem(position);
    
    ViewHolder viewHolder;
    if(convertView == null){
      
      viewHolder = new ViewHolder();
      LayoutInflater inflator = LayoutInflater.from(getContext());
      convertView = inflator.inflate(R.layout.tweet_item, parent, false);
      
      viewHolder.ivProfileImageView = (ImageView)convertView.findViewById(R.id.ivProfileImage);
      viewHolder.tvScreenName = (TextView)convertView.findViewById(R.id.tvScreenName);
      viewHolder.tvFullName = (TextView)convertView.findViewById(R.id.tvFullName);
      viewHolder.tvBody = (TextView)convertView.findViewById(R.id.tvBody);
      viewHolder.tvTimestamp = (TextView)convertView.findViewById(R.id.tvTimestamp);
      convertView.setTag(viewHolder);
    }else{
      viewHolder = (ViewHolder)convertView.getTag();
    }
    
    viewHolder.ivProfileImageView.setImageResource(android.R.color.transparent);
    ImageLoader loader = ImageLoader.getInstance();
    loader.displayImage(tweet.getUser().getProfileImg(), viewHolder.ivProfileImageView);
    
    viewHolder.tvScreenName.setText("@" + tweet.getUser().getScreenName());
    viewHolder.tvBody.setText(tweet.getBody());
    viewHolder.tvFullName.setText(tweet.getUser().getName());
    
    String createdAt = tweet.getCreatedAt();
    String ts = getTs(createdAt);
    if(ts != null)
      viewHolder.tvTimestamp.setText(ts);
    else
      viewHolder.tvTimestamp.setText("");
    
    return convertView;
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
