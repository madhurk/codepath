package madhur.codepath.tweetortweak;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utils {

  
  public static void toastNoNetwork(Context ctxt){
    Toast.makeText(ctxt, "Sorry, Internet connection is not available", Toast.LENGTH_SHORT).show();
  }  
  
  public static boolean isNetworkAvailable(Context ctxt) {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
  
}
