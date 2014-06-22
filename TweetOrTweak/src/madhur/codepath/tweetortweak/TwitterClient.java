package madhur.codepath.tweetortweak;

import madhur.codepath.tweetortweak.models.Tweet;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.widget.Toast;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; 
    public static final String REST_URL = "https://api.twitter.com/1.1"; 
    public static final String REST_CONSUMER_KEY = "bM8mbuS3N64sESjXnLdBeoIWo";       
    public static final String REST_CONSUMER_SECRET = "a0s8d4kEmnecehYd6whwY6XlOvEijFIRsCFQs2gpnwed26igvE"; 
    public static final String REST_CALLBACK_URL = "oauth://tweetortweak";
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void sendTweet(Tweet tweet, AsyncHttpResponseHandler handler){
      String apiUrl = getApiUrl("statuses/update.json");
      
      RequestParams params = new RequestParams();
      params.put("status", tweet.getBody());

      client.post(apiUrl, params, handler);
    }
    
    public void getSelfInfo(AsyncHttpResponseHandler handler){
      String apiUrl = getApiUrl("account/verify_credentials.json");
      client.get(apiUrl, null, handler);
    }    
    
    public void getHomeTimeline(long maxId, Context context, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "50");
        if(maxId > 0)
          params.put("max_id", String.valueOf(maxId));
        //Toast.makeText(context, "Requesting maxid=" + maxId, Toast.LENGTH_SHORT).show();
        client.get(apiUrl, params, handler);
    }    
}