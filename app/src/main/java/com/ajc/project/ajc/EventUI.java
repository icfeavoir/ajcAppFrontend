package com.ajc.project.ajc;

import android.content.Context;
import android.content.SharedPreferences;
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
import static android.content.Context.MODE_PRIVATE;
import static java.nio.file.Paths.get;

/**
 * Created by pierre on 2017-11-07.
 */

public class EventUI extends LinearLayout {

    private int MY_ID;

    private int event_id;
    private String title;
    private String date;
    private String time;
    private int participate;

    private Context context;

    public EventUI(Context context, int event_id, String title, String date, String time) {
        super(context);
        this.context = context;
        this.event_id = event_id;
        this.title = title;
        this.date = date;
        this.time = time;

        SharedPreferences prefs = context.getSharedPreferences("AJC_VAR", MODE_PRIVATE);
        this.MY_ID = prefs.getInt("MY_ID", 0);

        this.MY_ID = 1;
        Api participate = new Api("participation/get");
        participate.addData("event_id", this.event_id);
        participate.addData("user_id", this.MY_ID);
        try {
            JSONObject resp = participate.getData().getJSONObject(0);
            this.participate = !resp.isNull("participate") ? (int) resp.get("participate") : 0;
        }catch(JSONException e){
            this.participate = 0;
        }
        this.create();
    }

    private void create(){
        View.inflate(this.context, R.layout.event_layout, this);
        TextView tv = findViewById(R.id.eventTitle);
        tv.setText(this.title);
        tv = findViewById(R.id.eventDate);
        tv.setText(this.date);
        tv = findViewById(R.id.eventHeure);
        tv.setText(this.time);

        final EventUI it = this;
        final TextView[] buttons = {null, findViewById(R.id.eventYes), findViewById(R.id.eventNo), findViewById(R.id.eventMaybe)};

        for(int i=1; i<buttons.length; i++){
            TextView b = buttons[i];
            //default value (color)
            if(this.participate == i) {
                b.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if(b != null){
                final int count = i;
                b.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        int[] textColors = {0, getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark)};
                        textColors[count] = getResources().getColor(R.color.colorPrimary);

                        TextView byes = findViewById(R.id.eventYes);
                        byes.setTextColor(textColors[1]);
                        TextView bno = findViewById(R.id.eventNo);
                        bno.setTextColor(textColors[2]);
                        TextView bmaybe = findViewById(R.id.eventMaybe);
                        bmaybe.setTextColor(textColors[3]);
                        Api api = new Api("participation/insert");
                        api.addData("event_id", it.event_id);
                        api.addData("user_id", it.MY_ID);
                        api.addData("participate", count);
                        api.call();
                    }
                });
            }
        }
    }
}
