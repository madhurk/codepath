package madhur.codepath.tipcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HorizontalNumberPicker extends LinearLayout {
    
  public HorizontalNumberPicker(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    LayoutInflater layoutInflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    layoutInflater.inflate(R.layout.horizontal_number_picker, this);
  }
}