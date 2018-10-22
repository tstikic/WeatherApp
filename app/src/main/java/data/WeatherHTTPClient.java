package data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import Util.Utils;

public class WeatherHTTPClient {

    public String getWeatherDataCity(String cityName) {
     String url = Utils.BASE_URL + "q=" + cityName + Utils.apiId;
     String weatherData = getWeatherData(url);
     return weatherData;
    }

    public String getWeatherDataCoord(float lat, float lon) {
    String url = Utils.BASE_URL + "lat=" + lat + "&lon=" + lon + Utils.apiId;
    String weatherData = getWeatherData(url);
    return  weatherData;
    }

    public String getWeatherData (String url){

        HttpsURLConnection conection = null;
        InputStream inputStream = null;
        try {
            conection = (HttpsURLConnection) (new URL(url).openConnection());
            conection.setRequestMethod("GET");
            conection.setDoInput(true);
            conection.setDoOutput(true);
            conection.connect();

            //readData

            StringBuffer stringBuffer = new StringBuffer();
            inputStream = conection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");

                inputStream.close();
                conection.disconnect();

                return stringBuffer.toString();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] getImage(String code) {
        HttpsURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpsURLConnection) ( new URL("https://openweathermap.org/img/w/10d.png").openConnection());
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

}
