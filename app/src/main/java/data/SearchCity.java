package data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SearchCity {

    SharedPreferences preferences;

    public SearchCity (Activity activity){

        preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public  String getCity(){

        return preferences.getString("city", "Belgrade");
    }

    public void setCity(String city){

        preferences.edit().putString("city", city).commit();
    }

}
