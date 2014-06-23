package madhur.codepath.tweetortweak.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model{
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "user_id")
  private long userId;
  
  @Column(name = "profile_img")
  private String profileImg;
  
  @Column(name = "screen_name")
  private String screenName;
  
  public User(){
    super();
 }
  
  public static User fromJSON(JSONObject json) {
    User user = new User();
    
    try{
      user.name = json.getString("name");
      user.userId = json.getLong("id");
      user.screenName = json.getString("screen_name");
      user.profileImg = json.getString("profile_image_url");
    }catch(JSONException e){
      e.printStackTrace();
      return null;
    }
    return user;
  }


  public String getName() {
    return name;
  }


  public long getUserId() {
    return userId;
  }


  public String getProfileImg() {
    return profileImg;
  }


  public String getScreenName() {
    return screenName;
  }
  
  // Used to return items from another table based on the foreign key
  public List<Tweet> items() {
      return getMany(Tweet.class, "User");
  }
}
