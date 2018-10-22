package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Util.Utils;
import model.City;
import model.Weather;

public class JSONWeather {
    public static ArrayList<Weather> getWeather(String data){

        try {
            JSONObject jsonObject = new JSONObject(data);
            City city = new City();
            ArrayList<Weather> weatherList = new ArrayList();
            JSONArray listArray = jsonObject.getJSONArray("list");
            //set city
            JSONObject cityObject = Utils.getObject("city", jsonObject);
            JSONObject coordObject = Utils.getObject("coord", cityObject);
            city.setName(Utils.getString("name", cityObject));
            city.setLat(Utils.getFloat("lat", coordObject));
            city.setLon(Utils.getFloat("lon", coordObject));
            for (int i =0; i<listArray.length(); i+=8) {
                    Weather weather = new Weather();
                    JSONObject listObject = listArray.getJSONObject(i);

                    JSONObject mainObject = Utils.getObject("main", listObject);
                    JSONArray weatherArray = listObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    //set current condition
                    weather.currentCondition.setTemp(Utils.getFloat("temp", mainObject));
                    weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObject));
                    weather.currentCondition.setMaxTemo(Utils.getFloat("temp_max", mainObject));
                    weather.currentCondition.setHumidity(Utils.getFloat("humidity", mainObject));
                    weather.currentCondition.setCondition(Utils.getString("main", weatherObject));
                    weather.currentCondition.setDescription(Utils.getString("description", weatherObject));
                    weather.currentCondition.setIcon(Utils.getString("icon", weatherObject));
                    weather.currentCondition.setPressure(Utils.getFloat("pressure", mainObject));
                    weather.setDate(Utils.getDate("dt", listObject));
                    weather.city = city;
                    weatherList.add(weather);
            }





            //set date

        return weatherList;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  null;
    }

}
