package madhur.codepath.imagesearcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
  }
  
  public void onSave(View v){
    ImageSeachParams params = new ImageSeachParams();
    params.color = ((Spinner)findViewById(R.id.spColor)).getSelectedItem().toString();
    params.type = ((Spinner)findViewById(R.id.spType)).getSelectedItem().toString();
    params.size = ((Spinner)findViewById(R.id.spSize)).getSelectedItem().toString();
    params.site = ((EditText)findViewById(R.id.etSite)).getText().toString();
    
    Intent i = new Intent();
    i.putExtra("params", params);
    setResult(RESULT_OK, i);
    finish();
  }
}
