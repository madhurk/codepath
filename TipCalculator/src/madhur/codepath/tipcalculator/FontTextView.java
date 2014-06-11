package madhur.codepath.tipcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView  {

    public FontTextView(Context context){
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs){
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        TypefaceManager.applyFont(this, attrs, defStyle);
    }

}