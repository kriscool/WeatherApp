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


public class moon extends Fragment {

    TextView MoonRise;
    TextView MoonSet;
    TextView NewMoon;
    TextView FullMoon;
    TextView MoonPhase;
    TextView lunear;
    View view;
    public TextView clock;
    Calendar cal;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    int timezoneOffset;
    AstroCalculator calculator;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.moon, container, false);
        setData(51.75, 19.46, view);
        clock = (TextView) view.findViewById(R.id.TextTimeM);

        CountDownTimer timer = new CountDownTimer(300000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar calendar = Calendar.getInstance();
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
                    } catch (Exception e) {
                    }
                }
            }, 0, 300 * ((MainActivity) getActivity()).getRefTime());
        } catch (Exception e) {
        }

        return view;
    }


    public void getDate(double latitude, double longitude){
        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        timezoneOffset = cal.get(Calendar.ZONE_OFFSET);

        calculator = new AstroCalculator(new AstroDateTime(year, month, day + 1, hour, minute, second, timezoneOffset, true),
                new AstroCalculator.Location(latitude, longitude));
    }

    public void setData(double latitude, double longitude, View view) {
        MoonRise = (TextView) view.findViewById(R.id.TimeMoonRise);
        MoonSet = (TextView) view.findViewById(R.id.TimeMoonSet);
        NewMoon = (TextView) view.findViewById(R.id.TextNewMoon);
        FullMoon = (TextView) view.findViewById(R.id.TextFullMoon);
        MoonPhase = (TextView) view.findViewById(R.id.TextMoonPhase);
        lunear = (TextView) view.findViewById(R.id.TextDayOfMoon);

        getDate(latitude,longitude);

        MoonRise.setText(calculator.getMoonInfo().getMoonrise().toString().substring(10, 16));
        MoonSet.setText(calculator.getMoonInfo().getMoonset().toString().substring(10, 16));
        NewMoon.setText(calculator.getMoonInfo().getNextNewMoon().toString().substring(0, 10));
        FullMoon.setText(calculator.getMoonInfo().getNextFullMoon().toString().substring(0, 10));
        MoonPhase.setText(Double.toString(Math.floor(calculator.getMoonInfo().getIllumination() * 100) / 100) + "%");
        lunear.setText(Double.toString(Math.floor(calculator.getMoonInfo().getAge() * 100) / 100));

    }


}