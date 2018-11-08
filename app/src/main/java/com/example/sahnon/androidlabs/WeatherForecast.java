package com.example.sahnon.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {
    private TextView windSpeedView;
    private TextView minView;
    private TextView maxView;
    private TextView currTempView;
    private ImageView tempImageView;
    private ProgressBar progressBar;

    private String windSpeed;
    private String min;
    private String max;
    private String currTemp;
    private String icon;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        windSpeedView = (TextView)findViewById(R.id.windSpeed);
        minView = (TextView)findViewById(R.id.min);
        maxView = (TextView)findViewById(R.id.max);
        currTempView = (TextView)findViewById(R.id.currTemp);
        tempImageView = (ImageView)findViewById(R.id.tempImage);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();
    }

    //credit to http://www.java2s.com/Code/Android/2D-Graphics/GetBitmapfromUrlwithHttpURLConnection.htm
    class HttpUtils {
        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    class ForecastQuery extends AsyncTask<String, Integer, String> {
        public String doInBackground(String ... args){
            try {
                //connection
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                //xml parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                //get info from xml
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    switch(xpp.getEventType())
                    {
                        case XmlPullParser.START_TAG:
                            String name = xpp.getName();
                            if(name.equals("temperature")) {
                                currTemp = xpp.getAttributeValue(null, "value");
                                publishProgress(20);
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(40);
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(60);
                            }
                            else if(name.equals("speed")) {
                                windSpeed = xpp.getAttributeValue(null, "value");
                                publishProgress(80);
                            }
                            else if(name.equals("weather")) {
                                icon = xpp.getAttributeValue(null, "icon");
                            }
                            Log.i("read XML tag:" , name);
                            break;

                        case XmlPullParser.TEXT:
                            break;
                    }

                    xpp.next();
                }

                if(fileExistance(icon + ".png")){
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(icon + ".png");
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(fis);
                    publishProgress(100);
                    Log.i("WeatherForecast", "Image file taken from local storage.");
                } else {
                    String ImageURL = "http://openweathermap.org/img/w/" + icon + ".png";

                    HttpUtils HTTPUtils = new HttpUtils();
                    bitmap = HTTPUtils.getImage(ImageURL);
                    FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    publishProgress(100);
                    Log.i("WeatherForecast", "Image has been downloaded.");
                }
            }
            catch (Exception e) {
                Log.i("Exception", e.getMessage());
            }
            return "";
        }
        public void onProgressUpdate(Integer ... args) {
            progressBar.setProgress(args[0]);
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onPostExecute(String result) {
            windSpeedView.setText(windSpeed);
            minView.setText(min);
            maxView.setText(max);
            currTempView.setText(currTemp);
            tempImageView.setImageBitmap(bitmap);
        }
    }
}

