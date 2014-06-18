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
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

  GridView gvSearchResults;
  String currentQuery;
  
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
    
    gvSearchResults.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
          // Triggered only when new data needs to be appended to the list
          // Add whatever code is needed to append new items to your AdapterView
          //customLoadMoreDataFromApi(page); 
          customLoadMoreDataFromApi(totalItemsCount); 
      }
      });
    
    gvSearchResults.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
        ImageResult imageResult = imageResults.get(position);
        i.putExtra("image", imageResult);
        startActivity(i);
      }
    });
    
    if(savedInstanceState != null){
      currentQuery = savedInstanceState.getString("query");
    }
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putString("query", currentQuery);
    super.onSaveInstanceState(outState);
  }
  
  public void customLoadMoreDataFromApi(int offset) {
    // This method probably sends out a network request and appends new data items to your adapter. 
    // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
    // Deserialize API response and then construct new objects to append to the adapter
    doSearch(null, offset);
  }
  
  public void doSearch(String query, int offset){
    //String query = etQuery.getText().toString();
    if(query == null)
      query = currentQuery;
    
    if(query == null || query.trim().length() == 0)
      return;
    
    String queryEncoded = Uri.encode(query);

    StringBuilder sb = new StringBuilder("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&");
    String apiUrl = null;

    int pageSize = 8;
    //int start = (offset / pageSize);
    //Toast.makeText(this, "loading items from = " + offset, Toast.LENGTH_SHORT).show();
    sb.append("rsz=").append(pageSize).append("&");
    sb.append("start=").append(offset).append("&");

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
      public void onFailure(Throwable e, JSONArray errorResponse){
        Toast.makeText(getApplicationContext(), "Error getting results, please check your Internet connection", Toast.LENGTH_SHORT).show();
      }
      
      @Override
      public void onFailure ( Throwable e, JSONObject errorResponse ) {
        Toast.makeText(getApplicationContext(), "Error getting results, please check your Internet connection", Toast.LENGTH_SHORT).show();
      }
      
      @Override
      public void onFailure ( Throwable e, String errorResponse ) {
        Toast.makeText(getApplicationContext(), "Error getting results, please check your Internet connection", Toast.LENGTH_SHORT).show();
      }
      
      @Override
      public void onSuccess(JSONObject response){
        
        if(response == null){
          return;
        }
        
        JSONArray jsonImageResults = null;
        try{
          jsonImageResults = response.getJSONObject("responseData").getJSONArray("results");
          imageAdapter.addAll(ImageResult.fromJsonArray(jsonImageResults));
        }catch(JSONException e){
          //Toast.makeText(getApplicationContext(), "Error parsing results from google", Toast.LENGTH_SHORT).show();
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
    gvSearchResults = (GridView)findViewById(R.id.gvSearchResults);
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search_menu, menu);

    MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        imageAdapter.clear();
        int offset = 0;
        currentQuery = query;
        doSearch(query, offset);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    if(currentQuery != null){
      searchItem.expandActionView();
      searchView.setQuery(currentQuery, true);      
    }
    
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST_CODE) {
      //ImageSeachParams params = (ImageSeachParams)data.getSerializableExtra("params");
      imageAdapter.clear();
      doSearch(null, 0);
    }
  } 
  
  public void onSettings(MenuItem mi){
    Intent i = new Intent(this, SettingsActivity.class);
    startActivityForResult(i, SETTINGS_REQUEST_CODE);
  }
}