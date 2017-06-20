package com.example.kriscool.myapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;



public class Sun extends Fragment {

    TextView sunrise;
    TextView azymuth;
    TextView sunset;
    TextView sunsetAzymuth;
    TextView twilight;
    TextView civilDawn;
    View view;
    TextView clock;
    Calendar c;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    int timezoneOffset;
    AstroCalculator calculator;
    String a;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.sun, container, false);
        setData(51.75, 19.46, view );

        clock = (TextView) view.findViewById(R.id.TimeSun);
        CountDownTimer timer = new CountDownTimer(300000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar calendar = Calendar.getInstance();
                a = String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                clock.setText(String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
            }
            public void onFinish() {
            }
        };
        timer.start();

        try {
            Timer update = new Timer();
            update.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                setData(((MainActivity) getActivity()).getLatitude(), ((MainActivity) getActivity()).getLongitude(), view);
                            }
                        });
                    } catch(Exception e) {}
                }

            }, 0, 300*((MainActivity) getActivity()).getRefTime());
        } catch(Exception e) {}
        return view;
    }



    public void getDate(double latitude, double longitude){
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        second = c.get(Calendar.SECOND);
        timezoneOffset = c.get(Calendar.ZONE_OFFSET);

        calculator = new AstroCalculator(new AstroDateTime(year, month, day + 1, hour, minute, second, timezoneOffset, true),
                new AstroCalculator.Location(latitude, longitude));
    }

    public void setData(double latitude, double longitude, View view){
        sunrise = (TextView) view.findViewById(R.id.TimeSunRise);
        azymuth = (TextView) view.findViewById(R.id.TextAzymuth);
        sunset = (TextView) view.findViewById(R.id.TimeSunSet);
        sunsetAzymuth = (TextView) view.findViewById(R.id.AzymuthSunSet);
        twilight = (TextView) view.findViewById(R.id.Twilight);
        civilDawn = (TextView) view.findViewById(R.id.CivilDawn);
        getDate(latitude,longitude);

        sunsetAzymuth.setText(Double.toString(Math.floor(calculator.getSunInfo().getAzimuthSet()) ));
        sunset.setText(calculator.getSunInfo().getSunset().toString().substring(10,16));
        sunrise.setText(calculator.getSunInfo().getSunrise().toString().substring(10,16));
        azymuth.setText(Double.toString(Math.floor(calculator.getSunInfo().getAzimuthRise())));
        twilight.setText(calculator.getSunInfo().getTwilightMorning().toString().substring(10,16));
        civilDawn.setText(calculator.getSunInfo().getTwilightEvening().toString().substring(10,16));

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(a, a);
        super.onSaveInstanceState(outState);

    }
}