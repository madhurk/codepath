package madhur.codepath.imagesearcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

  EditText etQuery;
  Button btnSearch;
  GridView gvSearchResults;
  
  List<ImageResult> imageResults = new ArrayList<ImageResult>();
  ImageResultArrayAdapter imageAdapter;
  
  public static final int SETTINGS_REQUEST_CODE = 0;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    setupViews();
    
    imageAdapter = new ImageResultArrayAdapter(this, imageResults);
    gvSearchResults.setAdapter(imageAdapter);
    
    gvSearchResults.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
        ImageResult imageResult = imageResults.get(position);
        i.putExtra("image", imageResult);
        startActivity(i);
      }
    });
  }
  
  public void onSearch(View v){
    doSearch();
  }
  
  public void doSearch(){
    String query = etQuery.getText().toString();
    String queryEncoded = Uri.encode(query);
        
    StringBuilder sb = new StringBuilder("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&");
    String apiUrl = null;

      sb.append("rsz=8&").append("start=0&");
      String color = SearchSettings.getSetting(this, SearchSettings.colorFilter); 
      if(valid(color))
        sb.append("imgcolor=").append(color).append("&");
      
      String size = SearchSettings.getSetting(this, SearchSettings.sizeFilter); 
      if(valid(size))
        sb.append("imgsz=").append(size).append("&");
      
      String type = SearchSettings.getSetting(this, SearchSettings.typeFilter); 
      if(valid(type))
        sb.append("imgtype=").append(type).append("&");
      
      String site = SearchSettings.getSetting(this, SearchSettings.siteFilter); 
      if(valid(site))
        sb.append("as_sitesearch=").append(site).append("&");      
      
      apiUrl = sb.append("q=").append(queryEncoded).toString();

    
    AsyncHttpClient httpClient = new AsyncHttpClient();
    httpClient.get(apiUrl, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(JSONObject response){
        JSONArray jsonImageResults = null;
        try{
          jsonImageResults = response.getJSONObject("responseData").getJSONArray("results");
          imageAdapter.clear();
          imageAdapter.addAll(ImageResult.fromJsonArray(jsonImageResults));
        }catch(JSONException e){
          Toast.makeText(getApplicationContext(), "Error parsing results from google", Toast.LENGTH_SHORT).show();
          e.printStackTrace();
        }
      }      
    });
  }
  
  private boolean valid(String val){
    if(val != null && val.trim().length() > 0 && !"all".equalsIgnoreCase(val))
      return true;
    
    return false;
  }
  
  private void setupViews(){
    etQuery = (EditText)findViewById(R.id.etQueryTerm);
    btnSearch = (Button)findViewById(R.id.btnSearch);
    gvSearchResults = (GridView)findViewById(R.id.gvSearchResults);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.search_menu, menu);
      return true;
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST_CODE) {
      //ImageSeachParams params = (ImageSeachParams)data.getSerializableExtra("params");
      doSearch();
    }
  } 
  
  public void onSettings(MenuItem mi){
    Intent i = new Intent(this, SettingsActivity.class);
    startActivityForResult(i, SETTINGS_REQUEST_CODE);
  }
}