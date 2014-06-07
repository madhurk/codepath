package madhur.codepath.tipcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  public void calculateTip(View v){
    
    InputMethodManager inputManager = (InputMethodManager)
        getSystemService(Context.INPUT_METHOD_SERVICE); 

    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
           InputMethodManager.HIDE_NOT_ALWAYS);
    
    int id = v.getId();
    float tipPercent = 0;
    switch(id){
    case R.id.btTip1:
      tipPercent = 10;
      break;
    case R.id.btTip2:
      tipPercent = 15;
      break;
    case R.id.btTip3:
      tipPercent = 20;
      break;
    }
    
    EditText etAmount = (EditText)findViewById(R.id.etAmount);
    String amountStr = etAmount.getText().toString();
    TextView tvTip = (TextView)findViewById(R.id.tvTipAmount);

    if(amountStr == null || amountStr.trim().length() == 0){
      tvTip.setText("");
      return;
    }
    
    float amount;
    try{
      amount = Float.parseFloat(amountStr);
    }catch(NumberFormatException ne){
      tvTip.setText("");
      return;
    }
    
    float tipAmount = amount*tipPercent/100;    
    tvTip.setText(String.valueOf(tipAmount));
  }
}
