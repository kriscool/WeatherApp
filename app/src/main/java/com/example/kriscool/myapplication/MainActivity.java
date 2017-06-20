package com.example.kriscool.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String cityUrl = "london";
    Button confirm;
    EditText latitudeText;
    EditText longtitudeText;
    EditText refreshText;
    EditText city;
    TextView message;
    double latitude = 51.75;
    double longitude = 19.46;
    int refreshtime = 15;
    AlertDialog alterdiagog;
    ViewPager view;
    List<String> fragments;
    View dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (ViewPager) findViewById(R.id.viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragments = new ArrayList<>();
        fragments.add(Sun.class.getName());
        fragments.add(moon.class.getName());
        fragments.add(info.class.getName());
        PagerAdapter pagerAdapter = new MainActivity.MyPagerAdapter(getSupportFragmentManager());
        try{
            view.setAdapter(pagerAdapter);
        }catch(Exception e) {}
        setSupportActionBar(toolbar);
        getDataFromInternet();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(view.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            view.setCurrentItem(view.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }



 private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog, null);
        dialogBuilder.setView(dialog);


        alterdiagog = dialogBuilder.create();
        confirm = (Button) dialog.findViewById(R.id.ConfirmButton);
         latitudeText = (EditText) dialog.findViewById(R.id.Latitude);
     city = (EditText) dialog.findViewById(R.id.city);
     longtitudeText = (EditText) dialog.findViewById(R.id.Longitude);
     refreshText = (EditText) dialog.findViewById(R.id.Refresh);
     message = (TextView) dialog.findViewById(R.id.mess);
     latitudeText.setText(Double.toString(latitude));
     longtitudeText.setText(Double.toString(longitude));
     refreshText.setText(Integer.toString(refreshtime));

     confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cityUrl = city.getText().toString();
                latitude = Double.parseDouble(latitudeText.getText().toString());
                longitude = Double.parseDouble(longtitudeText.getText().toString());
                refreshtime = Integer.parseInt(refreshText.getText().toString());
                if(latitude > -181 && longitude > -81 && latitude<181 && longitude<81 && refreshtime > 0 && refreshtime != 0) {
                    alterdiagog.dismiss();
                    getDataFromInternet();
                }else{
                    message.setText("Wprowadzono złę dane");
                }
            }
        });
     alterdiagog.show();
    }




    private class MyPagerAdapter extends FragmentPagerAdapter {

        public List<String> fragmentsList = new ArrayList<>();
        private int Page = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragmentsList = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(getBaseContext(), fragmentsList.get(position));
        }
        @Override
        public int getCount() {
            return Page;
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putDouble("latitude",latitude);
        outState.putDouble("longitudes",longitude);
        outState.putInt("refreshtime",refreshtime);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        latitude = savedInstanceState.getDouble("latitude");
        longitude = savedInstanceState.getDouble("longitudes");
        refreshtime = savedInstanceState.getInt("refreshtime");
    }
    String d1;
    String d2;
    String d3;
    String d4;
    Weather w = new Weather();
    public void getDataFromInternet(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+cityUrl+"%22)and%20u=\"c\"&format=json",
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        String response = new String(responseBody);
                        //writeToFile(response, Context.);

                        try{
                            JSONObject responsee = new JSONObject(response);
                            JSONObject query = responsee.getJSONObject("query");
                            JSONObject results = query.getJSONObject("results");
                            JSONObject channel = results.getJSONObject("channel");

                            JSONObject item = channel.getJSONObject("item");
                            JSONObject condition = item.getJSONObject("condition");
                            JSONObject loc = channel.getJSONObject("location");
                            JSONObject atmosphere = channel.getJSONObject("atmosphere");
                            JSONObject wind = channel.getJSONObject("wind");


                            String descript = item.getString("description");
                            String[] descArr  = descript.split("<BR />");


                            d1 = descArr[6];
                           d2 = descArr[7];
                           d3 = descArr[8];
                           d4 = descArr[9];


                            w.setMWnazwaMiejsc(loc.getString("city"));
                            w.setMNkraj(loc.getString("country"));
                            //w.setMWcisnienie(atmosphere.getString("pressure"));
                            w.setMWdlugosc(item.getString("lat"));
                            w.setMWszerokosc(item.getString("long"));
                            w.setMWtemperatura(condition.getString("temp"));
                            w.setMWdesc(item.getString("description"));



                           w.setWIwiatrKierunek(wind.getString("direction"));
                            w.setWIwiatrSila(wind.getString("speed"));
                          w.setWIwidocznosc(atmosphere.getString("visibility"));
                           w.setWIwilgotnosc(atmosphere.getString("humidity"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
    public Weather getWheather(){return w;}
    public int getRefTime() { return refreshtime;}
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
}



