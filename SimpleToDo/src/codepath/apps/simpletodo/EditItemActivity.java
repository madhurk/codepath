package codepath.apps.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

  int editPos = -1;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_item);
    String origText = getIntent().getStringExtra("origText");
    editPos = getIntent().getIntExtra("pos", -1);
    EditText et = (EditText)findViewById(R.id.etEditItem);
    et.setText(origText);
    et.setSelection(origText.length());
  }
  
  
  public void saveEditedItem(View v){
    EditText et = (EditText)findViewById(R.id.etEditItem);
    String editedText = et.getText().toString();
    Intent data = new Intent();
    data.putExtra("editedText", editedText);
    data.putExtra("pos", editPos);
    setResult(RESULT_OK, data);
    finish();
  }

}
