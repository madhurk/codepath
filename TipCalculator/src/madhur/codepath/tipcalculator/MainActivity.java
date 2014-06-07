package madhur.codepath.tipcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }
  
  public void calculateTip(View v){
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
    float amount = Float.parseFloat(amountStr);
    float tipAmount = amount*tipPercent/100;
    
    TextView tvTip = (TextView)findViewById(R.id.tvTipAmount);
    tvTip.setText(String.valueOf(tipAmount));
  }
}
