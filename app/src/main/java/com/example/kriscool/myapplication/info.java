package com.example.kriscool.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kriscool on 19.06.2017.
 */

public class info extends Fragment {

    Weather w = new Weather();
    View view;
    TextView wiatrSila;
    TextView wiatrKierunek;
    TextView wilgotnosc;
    TextView widocznosc;
    TextView miastoo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.info, container, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        update();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        return view;
    }


    public void update(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setData(((MainActivity) getActivity()).getWheather(),
                        view);
            }
        });
    }



    public void initializeElements(View view){
        wiatrSila = (TextView) view.findViewById(R.id.tWiatrSilaWart);
        wiatrKierunek = (TextView) view.findViewById(R.id.tKierunekWart);
        wilgotnosc = (TextView) view.findViewById(R.id.tWilgotWart);
        widocznosc = (TextView) view.findViewById(R.id.tWidoczWart);
        miastoo = (TextView) view.findViewById(R.id.tMiastoInfo);
    }

    public void setData(Weather w,View view){


        initializeElements(view);
        wiatrSila.setText(w.getWIwiatrSila() + " mph");
        wiatrKierunek.setText(w.getWIwiatrKierunek());
        wilgotnosc.setText(w.getWIwilgotnosc());
        widocznosc.setText(w.getWIwidocznosc());
        miastoo.setText(w.getMWnazwaMiejsc() + ", " + w.getMNkraj());

    }

}