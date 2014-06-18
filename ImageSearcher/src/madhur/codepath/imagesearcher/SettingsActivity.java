package madhur.codepath.imagesearcher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

  Spinner spColor;
  Spinner spType;
  Spinner spSize;
  EditText etSite;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    setupViews();
    
    String color = SearchSettings.getSetting(this, SearchSettings.colorFilter);
    setSpinnerValue(spColor, color);
    
    String size = SearchSettings.getSetting(this, SearchSettings.sizeFilter);
    setSpinnerValue(spSize, size);
    
    String type = SearchSettings.getSetting(this, SearchSettings.typeFilter);
    setSpinnerValue(spType, type);
    
    String site = SearchSettings.getSetting(this, SearchSettings.siteFilter);
    etSite.setText(site);
  }
  
  private void setSpinnerValue(Spinner spinner, String value){
    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
    int pos = adapter.getPosition(value);
    spinner.setSelection(pos);
  }
  
  public void setupViews(){
    spColor = (Spinner)findViewById(R.id.spColor);
    spType = (Spinner)findViewById(R.id.spType);
    spSize = (Spinner)findViewById(R.id.spSize);
    etSite = (EditText)findViewById(R.id.etSite);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        Intent intent = NavUtils.getParentActivityIntent(this); 
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP); 
        NavUtils.navigateUpTo(this, intent);
        return true;        
    }
    
    return super.onOptionsItemSelected(item);
  }

  public void onSave(View v){

    String color = spColor.getSelectedItem().toString();
    String type = spType.getSelectedItem().toString();
    String size = spSize.getSelectedItem().toString();
    String site = etSite.getText().toString();
    
    SearchSettings.setSetting(this, SearchSettings.colorFilter, color);
    SearchSettings.setSetting(this, SearchSettings.typeFilter, type);
    SearchSettings.setSetting(this, SearchSettings.sizeFilter, size);
    SearchSettings.setSetting(this, SearchSettings.siteFilter, site);
    
    Intent i = new Intent();
    //i.putExtra("params", params);
    setResult(RESULT_OK, i);
    finish();
  }
}
