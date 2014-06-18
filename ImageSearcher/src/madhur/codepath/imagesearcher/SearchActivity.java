package madhur.codepath.imagesearcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
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
    
    // Get the intent, verify the action and get the query
    handleIntent(getIntent());
  }
  
  @Override
  protected void onNewIntent(Intent intent) {
      //Toast.makeText(this, "got new intent", Toast.LENGTH_SHORT).show();
      setIntent(intent);
      handleIntent(intent);
  }

  private void handleIntent(Intent intent) {
      if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        
        String query = intent.getStringExtra(SearchManager.QUERY);
        currentQuery = query;
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
            MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
        suggestions.saveRecentQuery(query, null);
        
        imageAdapter.clear();
        doSearch(query, 0);
      }
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    outState.putString("query", currentQuery);
    super.onSaveInstanceState(outState);
  }
  
  public void customLoadMoreDataFromApi(int offset) {
    doSearch(currentQuery, offset);
  }
  
  public void doSearch(String query, int offset){
    if(query == null || query.trim().length() == 0)
      return;
    
    setTitle(query);
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

    final MenuItem searchItem = menu.findItem(R.id.action_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    
    // Get the SearchView and set the searchable configuration
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    
    // Assumes current activity is the searchable activity
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    
    searchView.setOnSuggestionListener(new OnSuggestionListener() {
      
      @Override
      public boolean onSuggestionSelect(int position) {
        return false;
      }
      
      @Override
      public boolean onSuggestionClick(int position) {
        if (searchItem != null) {
          searchItem.collapseActionView();
        }
        return false;
      }
    });
    
    searchView.setOnQueryTextListener(new OnQueryTextListener() {

      public boolean onQueryTextChange(String arg0) {
          return false;
      }

      public boolean onQueryTextSubmit(String arg0) {
          if (searchItem != null) {
              searchItem.collapseActionView();
          }
          return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    if (resultCode == RESULT_OK && requestCode == SETTINGS_REQUEST_CODE) {
      imageAdapter.clear();
      String query = currentQuery;
      doSearch(query, 0);
    }
  } 
  
  public void onSettings(MenuItem mi){
    Intent i = new Intent(this, SettingsActivity.class);
    startActivityForResult(i, SETTINGS_REQUEST_CODE);
  }
}