package madhur.codepath.tipcalculator;

import android.app.Application;

public class App extends Application {
  
  @Override
  public void onCreate(){
      super.onCreate();
      TypefaceManager.initialize(this, R.xml.fonts);
  }
  
}
