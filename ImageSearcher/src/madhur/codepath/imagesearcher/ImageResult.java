package madhur.codepath.imagesearcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{
  private static final long serialVersionUID = 1L;
  public String fullUrl = null;
  public String thumbUrl = null;
  
  public ImageResult(JSONObject obj){
    try{
      fullUrl = obj.getString("url");
      thumbUrl = obj.getString("tbUrl");
    }catch(JSONException e){
      e.printStackTrace();
    }
  }

  public static List<ImageResult> fromJsonArray(JSONArray jsonImageResults) {
    List<ImageResult> imageResults = new ArrayList<ImageResult>();
    for(int i = 0; i < jsonImageResults.length(); ++i){
      try{
        imageResults.add(new ImageResult(jsonImageResults.getJSONObject(i)));
      }catch(JSONException e){
        e.printStackTrace();
      }
    }
    return imageResults;
  }
  
  @Override
  public String toString() {
    return thumbUrl;
  }
}
