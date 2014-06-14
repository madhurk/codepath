package madhur.codepath.imagesearcher;

import android.content.Context;
import android.content.SharedPreferences;

public class SearchSettings {

  public final static String colorFilter = "color";
  public final static String sizeFilter = "size";
  public final static String typeFilter = "type";
  public final static String siteFilter = "site";

  private static SharedPreferences getPrefs(Context context) {
      return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
  }

  public static String getSetting(Context context, String setting) {
      return getPrefs(context).getString(setting, "all");
  }


  public static void setSetting(Context context, String name, String value) {
      getPrefs(context).edit().putString(name, value).commit();
  }

}
