package Util;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

    public static  final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?";
    public static  final String ICON_URL = "https://openweathermap.org/img/w/";
    public static final String apiId = "&appid=f0d7bcfe612d2d55b5831911602b80dd";
    public static final String DEFAULT_CITY = "q=London";
    public static final String degree = "\u2103";

    public static JSONObject getObject (String tagName, JSONObject jsonObject)throws JSONException{

        JSONObject jObj = jsonObject.getJSONObject(tagName);
        return  jObj;
    }

    public static  String getString (String tagName, JSONObject jsonObject) throws JSONException{
         return jsonObject.getString(tagName);

    }

    public static float getFloat (String tagName, JSONObject jsonObject) throws JSONException{

        return (float) jsonObject.getDouble(tagName);
    }

    public static long getDate (String tagName, JSONObject jsonObject) throws JSONException{
        return (long) jsonObject.getLong(tagName);

    }

    public static double getDouble (String tagName, JSONObject jsonObject) throws JSONException{

        return (float) jsonObject.getDouble(tagName);
    }

    public static int getInt (String tagName, JSONObject jsonObject) throws JSONException{

        return jsonObject.getInt(tagName);
    }

}
