package raynjire.com.myweatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherAPI extends AppCompatActivity implements View.OnClickListener
{
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "6c9b4f7c030427e1638ad5a53484348f";
    Button btnGetWeather;
    EditText txtCity;
    TextView lblWeatherSubtitle, lblTemp1, lblHumidity1, lblDescription1, lblWindSpeed1, lblCloudiness1, lblPressure1, lblTemp2, lblHumidity2, lblDescription2, lblWindSpeed2, lblCloudiness2, lblPressure2;
    AlertDialog.Builder builder;
    DecimalFormat df = new DecimalFormat("#.##");
    ProgressBar pbarWeather;
    String tempURL, city;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_api);
        
        btnGetWeather = findViewById(R.id.BTNGetWeather);
        txtCity = findViewById(R.id.TXTCity);
        lblWeatherSubtitle = findViewById(R.id.LBLWeatherSubTitle);
        lblTemp1 = findViewById(R.id.LBLTemp1);
        lblHumidity1 = findViewById(R.id.LBLHumidity1);
        lblDescription1 = findViewById(R.id.LBLDescription1);
        lblWindSpeed1 = findViewById(R.id.LBLWindSpeed1);
        lblCloudiness1 = findViewById(R.id.LBLCloudiness1);
        lblPressure1 = findViewById(R.id.LBLPressure1);
        lblTemp2 = findViewById(R.id.LBLTemp2);
        lblHumidity2 = findViewById(R.id.LBLHumidity2);
        lblDescription2 = findViewById(R.id.LBLDescription2);
        lblWindSpeed2 = findViewById(R.id.LBLWindSpeed2);
        lblCloudiness2 = findViewById(R.id.LBLCloudiness2);
        lblPressure2 = findViewById(R.id.LBLPressure2);
        pbarWeather = findViewById(R.id.PBarWeather);
        builder = new AlertDialog.Builder(this);
        
    }
    
    //Clear focus on touch outside for all EditText inputs
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            
            if(v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                
                if(!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    
                }
            }
        }
        
        return super.dispatchTouchEvent(event);
        
    }
    
    @Override
    public void onClick(View objClicked)
    {
        switch(objClicked.getId())
        {
            case R.id.BTNGetWeather:
                pbarWeather.setVisibility(View.VISIBLE);
                tempURL = "";
                city = txtCity.getText().toString().trim();
                
                if(city.equals(""))
                {
                    lblWeatherSubtitle.setVisibility(View.VISIBLE);
                    lblWeatherSubtitle.setText("City field cannot be empty");
                    
                }
                
                else
                {
                    tempURL = url + "?q=" + city + "&appid=" + appid;
                    
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, tempURL, new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                String description = jsonObjectWeather.getString("description");
                                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                double tempK = jsonObjectMain.getDouble("temp");
                                double tempC = tempK - 273.15;
                                double tempF = tempC * 9 / 5 + 32;
                                float pressure = jsonObjectMain.getInt("pressure");
                                int humidity = jsonObjectMain.getInt("humidity");
                                JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");
                                JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                                String clouds = jsonObjectClouds.getString("all");
                                JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                                String countryName = jsonObjectSys.getString("country");
                                String cityName = jsonObject.getString("name");
                                
                                lblWeatherSubtitle.setText("Current weather in " + cityName + " (" + countryName + ")");
                                lblTemp2.setText(df.format(tempC) + "°C / " + df.format(tempF) + "°F / " + df.format(tempK) + "°K  ");
                                lblHumidity2.setText(humidity + "%");
                                lblDescription2.setText(description);
                                lblWindSpeed2.setText(wind + "m/s (meters per second)");
                                lblCloudiness2.setText(clouds + "%");
                                lblPressure2.setText(pressure + " hPa (hecto Pascals)\n");
                                
                                lblWeatherSubtitle.setVisibility(View.VISIBLE);
                                lblTemp1.setVisibility(View.VISIBLE);
                                lblHumidity1.setVisibility(View.VISIBLE);
                                lblDescription1.setVisibility(View.VISIBLE);
                                lblWindSpeed1.setVisibility(View.VISIBLE);
                                lblCloudiness1.setVisibility(View.VISIBLE);
                                lblPressure1.setVisibility(View.VISIBLE);
                                lblTemp2.setVisibility(View.VISIBLE);
                                lblHumidity2.setVisibility(View.VISIBLE);
                                lblDescription2.setVisibility(View.VISIBLE);
                                lblWindSpeed2.setVisibility(View.VISIBLE);
                                lblCloudiness2.setVisibility(View.VISIBLE);
                                lblPressure2.setVisibility(View.VISIBLE);
                                
                            }
                            
                            catch(JSONException e)
                            {
                                e.printStackTrace();
                                
                            }
                        }
                    },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error)
                                {
                                    if(error.toString().contains("com.android.volley.ClientError"))
                                    {
                                        builder.setTitle("Unknown City")
                                                .setMessage("City '" + txtCity.getText().toString().trim() + "' Not Found")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setCancelable(false)
                                                
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                    {
                                                        //Do Nothing
                                                        
                                                    }
                                                }).show();
                                        
                                    }
                                    
                                    else if(error.toString().contains("com.android.volley.NoConnectionError"))
                                    {
                                        builder.setTitle("Internet Connection Error")
                                                .setMessage("Internet connection lost. Please refresh the page or check the connection")
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setCancelable(false)
                                                
                                                .setPositiveButton("Open WiFi Settings", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                    {
                                                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                                        
                                                    }
                                                })
                                                
                                                .setNegativeButton("Open Mobile Data Settings", new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                    {
                                                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                                                        
                                                    }
                                                })
                                                
                                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(DialogInterface dialogInterface, int i)
                                                    {
                                                        //Do Nothing
                                                        
                                                    }
                                                }).show();
                                    }
                                }
                            });
                    
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);
                    
                }
                
                pbarWeather.setVisibility(View.INVISIBLE);
                
                break;
            
        }
    }
}