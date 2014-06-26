package madhur.codepath.tweetortweak.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "items")
public class Tweet extends Model{
  
  @Column(name = "body")
  private String body;
  
  @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.IGNORE)
  private long tweetId;
  
  @Column(name = "created_at")
  private String createdAt;
  
  @Column(name = "user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
  private User user;  
  
  public Tweet(){
     super();
  }
  
  public static Tweet fromJSON(JSONObject json){
    
    Tweet tweet = new Tweet();
    
    try{
      tweet.body = json.getString("text");
      tweet.tweetId = json.getLong("id");
      tweet.createdAt = json.getString("created_at");
      tweet.user = User.fromJSON(json.getJSONObject("user"));
    }catch(JSONException e){
      e.printStackTrace();
      return null;
    }
    return tweet;    
  }
  

  public static List<Tweet> fromJSONArray(JSONArray json) {
    
    List<Tweet> tweets = new ArrayList<Tweet>(json.length());    
    for(int i = 0; i < json.length(); ++i){
      JSONObject tweetJson = null;
      try{
        tweetJson = json.getJSONObject(i);
      }catch(JSONException e){
        e.printStackTrace();
        continue;
      }      
      Tweet t = Tweet.fromJSON(tweetJson);
      if(t != null){
        tweets.add(t);
      }
    }
    return tweets;
  }

  public void setBody(String text){
    this.body = text;
  }
  
  public String getBody() {
    return body;
  }

  public long getTweetId() {
    return tweetId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public User getUser() {
    return user;
  }

  public String toString(){
    return user.getScreenName() + ": " + body;        
  }
  
  public static List<Tweet> getSavedTweets() {
    return new Select()
      .from(Tweet.class)
      .orderBy("created_at DESC")
      .execute();
  }
}
