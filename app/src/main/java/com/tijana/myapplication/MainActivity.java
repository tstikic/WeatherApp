package com.tijana.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.DataFormatException;

import Util.Utils;
import data.JSONWeather;
import data.SearchCity;
import data.WeatherHTTPClient;
import model.CurrentCondition;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private Bitmap iconData;

    public enum Days implements Serializable {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient client;


    Weather weather = new Weather();
    ArrayList<Weather> weatherList = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchCity searchCity = new SearchCity(MainActivity.this);

        client = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
           // renderWeatherData(searchCity.getCity());
            client.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            // Got last known location
                            if (location != null) {
                                double lat = location.getLatitude();
                                double lon = location.getLongitude();

                                renderWeatherData("lat=" + lat + "&lon=" + lon);
                            } else {
                                renderWeatherData(Utils.DEFAULT_CITY);
                                Log.w("loc:", "Cannot find location");

                            }
                        }
                    });
        }

    }


    
    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return (weatherList.size() - 1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);

            long timeStamp = weatherList.get(position+1).getDate();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timeStamp);

            int dayNum = c.get(Calendar.DAY_OF_WEEK);

            Days day = Days.values()[position+1];

            ImageView imageView = convertView.findViewById(R.id.imageView);
            TextView textView_name = convertView.findViewById(R.id.textView_name);
            TextView textView_max = convertView.findViewById(R.id.textView_max);
            TextView textView_min = convertView.findViewById(R.id.textView_min);


            textView_name.setText(day.toString());
            new GetImageFromURL(imageView).execute(Utils.ICON_URL + (weatherList.get(position)).currentCondition.getIcon() + ".png");
            textView_max.setText((int) (weatherList.get(position + 1)).currentCondition.getMaxTemo() + "");
            textView_min.setText((int) (weatherList.get(position + 1)).currentCondition.getMinTemp() + "");

            return convertView;
        }


    }





    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //if permission is granted
                    client.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    // Got last known location
                                    if (location != null) {
                                        double lat = location.getLatitude();
                                        double lon = location.getLongitude();

                                        renderWeatherData("lat=" + lat + "&lon=" + lon);
                                    } else {
                                        renderWeatherData(Utils.DEFAULT_CITY);
                                        Log.w("loc:", "Cannot find location");

                                    }
                                }
                            });

                } else {
                    //permission denied, default city shown
                    renderWeatherData(Utils.DEFAULT_CITY);
                }
                return;
            }
        }
    }

  public void WeatherDataLocation(float lon, float lat){


  }

    public void renderWeatherData (String city){

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }

    private class WeatherTask extends AsyncTask<String, Void, ArrayList<Weather>>{

        @Override
        protected ArrayList<Weather> doInBackground(String[] strings) {

            String data = ((new WeatherHTTPClient()).getWeatherDataCity(strings[0]));
            if (data==null){
                data = ((new WeatherHTTPClient()).getWeatherDataCity(Utils.DEFAULT_CITY + "&units=metric"));
            }
            weatherList = JSONWeather.getWeather(data);
            Log.v("Data:", (weatherList.get(0)).city.getName());

            return weatherList;
        }

        @Override
        protected void onPostExecute(ArrayList<Weather> weather) {
            super.onPostExecute(weather);

     //       DateFormat df = DateFormat.getTimeInstance();
       //     String day = df.format(new Date(weather.getDate()));

            ImageView mainImage = findViewById(R.id.image_view);
            TextView cityName = findViewById(R.id.search_view);
            TextView currentTemp = findViewById(R.id.degree);
            TextView humidity = findViewById(R.id.humidity);
            TextView pressure = findViewById(R.id.pressure);
            TextView condition = findViewById(R.id.condition);

            cityName.setText((weatherList.get(0)).city.getName());
            currentTemp.setText((int)(weatherList.get(0)).currentCondition.getTemp() + Utils.degree);
            new GetImageFromURL(mainImage).execute(Utils.ICON_URL + (weatherList.get(0)).currentCondition.getIcon() + ".png");

            condition.setText("Today: " + weatherList.get(0).currentCondition.getDescription());
            humidity.setText("Humidity: " + (int)weatherList.get(0).currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + (int)weatherList.get(0).currentCondition.getPressure() + "pHa");

            ListView lview = findViewById(R.id.listView);
            CustomAdapter customAdapter = new CustomAdapter();

            lview.setAdapter(customAdapter);



            Log.w("numberList:", String.valueOf(weatherList.size()));

        }


    }
    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap>{
        ImageView imgV;

        public GetImageFromURL(ImageView imgV){
            this.imgV = imgV;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            iconData = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                iconData = BitmapFactory.decodeStream(srt);
            } catch (Exception e){
                e.printStackTrace();
            }
            return iconData;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgV.setImageBitmap(bitmap);
        }

    }

    public void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search City");

        final EditText cityInput = new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Belgrade");
        builder.setView(cityInput);
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SearchCity searchCity = new SearchCity(MainActivity.this);
                searchCity.setCity(cityInput.getText().toString());

                String newCity = searchCity.getCity();
                renderWeatherData("q=" + newCity);
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.search){

            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }


}
