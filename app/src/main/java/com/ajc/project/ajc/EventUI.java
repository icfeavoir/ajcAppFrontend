package com.ajc.project.ajc;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.button;

/**
 * Created by pierre on 2017-11-07.
 */

public class EventUI extends LinearLayout {

    private int event_id;
    private String title;
    private String date;
    private String time;
    private int participate;

    private Context context;

    public EventUI(Context context, int event_id, String title, String date, String time, int participate){
        super(context);
        this.context = context;
        this.event_id = event_id;
        this.title = title;
        this.date = date;
        this.time = time;

        this.create();
    }

    public EventUI(Context context, int event_id, String title, String date, String time){
        this(context, event_id, title, date, time, 0);
    }

    private void create(){
        View.inflate(this.context, R.layout.event_layout, this);
        TextView tv = (TextView) findViewById(R.id.eventTitle);
        tv.setText(this.title);
        tv = (TextView) findViewById(R.id.eventDate);
        tv.setText(this.date);
        tv = (TextView) findViewById(R.id.eventHeure);
        tv.setText(this.time);

        final EventUI it = this;
        final Button[] buttons = {null, (Button) findViewById(R.id.eventYes), (Button) findViewById(R.id.eventNo), (Button) findViewById(R.id.eventMaybe)};
        for(int i=0; i<buttons.length; i++){
            Button b = buttons[i];
            if(b != null){
                final int count = i;
                b.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        int[] textColors = {0, getResources().getColor(R.color.black), getResources().getColor(R.color.black), getResources().getColor(R.color.black)};
                        int[] backgroundColors = {0, getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark)};
                        textColors[count] = getResources().getColor(R.color.white);
                        backgroundColors[count] = getResources().getColor(R.color.colorPrimary);

                        Button byes = (Button) findViewById(R.id.eventYes);
                        byes.setBackgroundColor(backgroundColors[1]);
                        byes.setTextColor(textColors[1]);
                        Button bno = (Button) findViewById(R.id.eventNo);
                        bno.setBackgroundColor(backgroundColors[2]);
                        bno.setTextColor(textColors[2]);
                        Button bmaybe = (Button) findViewById(R.id.eventMaybe);
                        bmaybe.setBackgroundColor(backgroundColors[3]);
                        bmaybe.setTextColor(textColors[3]);
                        Api api = new Api("participation/insert");
                        api.addData("event_id", it.event_id);
                        api.addData("user_id", 1);
                        api.addData("participate", count);
                        api.call();
                    }
                });
            }
        }
//        Button buttonYes = (Button) findViewById(R.id.eventYes);
//
//        buttonYes.
//        final Button buttonMaybe = (Button) findViewById(R.id.eventMaybe);
//        buttonMaybe.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Button byes = (Button) findViewById(R.id.eventYes);
//                byes.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                byes.setTextColor(getResources().getColor(R.color.black));
//                Button bno = (Button) findViewById(R.id.eventNo);
//                bno.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                bno.setTextColor(getResources().getColor(R.color.black));
//                Button bmaybe = (Button) findViewById(R.id.eventMaybe);
//                bmaybe.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                bmaybe.setTextColor(getResources().getColor(R.color.white));
//                Api api = new Api("participation/insert");
//                api.addData("event_id", it.event_id);
//                api.addData("user_id", 1);
//                api.addData("participate", 2);
//                api.call();
//            }
//        });
//        final Button buttonNo = (Button) findViewById(R.id.eventNo);
//        buttonNo.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Button byes = (Button) findViewById(R.id.eventYes);
//                byes.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                byes.setTextColor(getResources().getColor(R.color.black));
//                Button bno = (Button) findViewById(R.id.eventNo);
//                bno.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                bno.setTextColor(getResources().getColor(R.color.white));
//                Button bmaybe = (Button) findViewById(R.id.eventMaybe);
//                bmaybe.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                bmaybe.setTextColor(getResources().getColor(R.color.black));
//                Api api = new Api("participation/insert");
//                api.addData("event_id", it.event_id);
//                api.addData("user_id", 1);
//                api.addData("participate", 3);
//                api.call();
//            }
//        });
    }
}
