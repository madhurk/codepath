package madhur.codepath.tipcalculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {

  final int step = 5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tip_calculator);

    TipListener tipListener = new TipListener();

    Button btnPlus = (Button) findViewById(R.id.btn_plus);
    btnPlus.setOnClickListener(tipListener);

    Button btnMinus = (Button) findViewById(R.id.btn_minus);
    btnMinus.setOnClickListener(tipListener);
    
    EditText etAmount = (EditText) findViewById(R.id.etAmount);
    etAmount.setOnKeyListener(tipListener);
    etAmount.setOnFocusChangeListener(tipListener);
    
    EditText etTipPercent = (EditText) findViewById(R.id.etTipPercent);
    etTipPercent.setOnKeyListener(tipListener);
    etTipPercent.setOnFocusChangeListener(tipListener);
    
    if(savedInstanceState != null){
      
      if(savedInstanceState.getSerializable("tip") != null){
        String tipVal = (String)savedInstanceState.getSerializable("tip");
        ((EditText)findViewById(R.id.etTipPercent)).setText(tipVal);
      }
      
      if(savedInstanceState.getSerializable("amount") != null){
        String amountStr = (String)savedInstanceState.getSerializable("amount");
        ((EditText)findViewById(R.id.etAmount)).setText(amountStr);
      }
      
      tipListener.calculateTip();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
    
    EditText etTip = (EditText) findViewById(R.id.etTipPercent);
    String tipStr = etTip.getText().toString();
    
    EditText etAmount = (EditText) findViewById(R.id.etAmount);
    String amountStr = etAmount.getText().toString();
    
    state.putSerializable("amount", amountStr);
    state.putSerializable("tip", tipStr);
  }

  class TipListener implements OnClickListener, OnKeyListener, OnFocusChangeListener{

    @Override
    public void onClick(View v) {
      int id = v.getId();
      EditText etTip = (EditText) findViewById(R.id.etTipPercent);
      String prevValStr = etTip.getText().toString();
      
      if(prevValStr == null || prevValStr.trim().length() == 0){
        prevValStr = "0";
      }
                  
      int prevVal;      
      try{
        prevVal = Integer.parseInt(prevValStr);
      }catch(NumberFormatException nfe){
        prevVal = 0;
      }
      
      int newVal = prevVal;
      if (id == ((Button) findViewById(R.id.btn_minus)).getId()) {
        newVal = prevVal - 5;
        if (newVal < 0)
          newVal = 0;
      } else if (id == ((Button) findViewById(R.id.btn_plus)).getId()) {
        newVal = prevVal + 5;
        if (newVal > 100)
          newVal = 100;
      }
      etTip.setText(String.valueOf(newVal));
      calculateTip();
    }

    private void calculateTip() {
      
      EditText etAmount = (EditText) findViewById(R.id.etAmount);
      String amountStr = etAmount.getText().toString();  
      float amount;
      int tipPercent;
      try {
        amount = Float.parseFloat(amountStr);
      } catch (NumberFormatException nfe) {
        amount = -1;
      }
      
      EditText etTip = (EditText) findViewById(R.id.etTipPercent);
      String tipStr = etTip.getText().toString();
      try{
        tipPercent = Integer.parseInt(tipStr);
      }catch(NumberFormatException nfe){
        tipPercent = 0;
      }

      TextView tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
      TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
      if (amount > -1) {
        float tip = amount * tipPercent / 100;
        tvTipAmount.setText(String.valueOf(tip));
        tvTotalAmount.setText(String.valueOf(tip + amount));
      } else {
        tvTipAmount.setText("");
        tvTotalAmount.setText("");
      }
    }

    private void hideKeyboard(){
      InputMethodManager inputManager = (InputMethodManager)
          getSystemService(Context.INPUT_METHOD_SERVICE);
      
      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
             InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
      if ((event.getAction() == KeyEvent.ACTION_DOWN) && 
          (keyCode == KeyEvent.KEYCODE_ENTER)) {

        calculateTip();
        EditText etTip = (EditText) findViewById(R.id.etTipPercent);
        if(v.getId() == etTip.getId()){
          findViewById(R.id.dummyView).requestFocus();
          hideKeyboard();
        }
     }
      
      return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      if(v.getId() == findViewById(R.id.etTipPercent).getId()){
        if(hasFocus){
          ((Button)findViewById(R.id.btn_minus)).setVisibility(View.VISIBLE);
          ((Button)findViewById(R.id.btn_plus)).setVisibility(View.VISIBLE);
        }else{
          ((Button)findViewById(R.id.btn_minus)).setVisibility(View.INVISIBLE);
          ((Button)findViewById(R.id.btn_plus)).setVisibility(View.INVISIBLE);          
        }
      }else if(v.getId() == findViewById(R.id.etAmount).getId()){
        if(!hasFocus){
          calculateTip();
        }
      }
    }
  }

}
