package com.ajc.project.ajc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pierre on 2017-11-07.
 */

public class EventUI extends LinearLayout {

    private int event_id;
    private String title;
    private String date;
    private String time;

    private Context context;

    public EventUI(Context context, int event_id, String title, String date, String time){
        super(context);
        this.context = context;
        this.event_id = event_id;
        this.title = title;
        this.date = date;
        this.time = time;

        this.create();
    }

    private void create(){
        View.inflate(this.context, R.layout.event_layout, this);
        TextView tv = (TextView) findViewById(R.id.eventTitle);
        tv.setText(this.title);
        tv = (TextView) findViewById(R.id.eventDate);
        tv.setText(this.date);
        tv = (TextView) findViewById(R.id.eventHeure);
        tv.setText(this.time);

        Button buttonYes = (Button) findViewById(R.id.eventYes);
        final EventUI it = this;
        buttonYes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Api api = new Api("participation/insert");
                api.addData("event_id", it.event_id);
                api.addData("user_id", 1);
                api.addData("participe", 1);
                System.out.println(api.call());
            }
        });
    }
}
