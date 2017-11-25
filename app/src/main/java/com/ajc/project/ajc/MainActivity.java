package com.ajc.project.ajc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private int MY_ID = 1;
    SwipeRefreshLayout refresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences.Editor editor = getSharedPreferences("AJC_VAR", MODE_PRIVATE).edit();
        editor.putInt("MY_ID", MY_ID);
        editor.apply();

        this.refresher = (SwipeRefreshLayout) findViewById(R.id.activity_main_refresh);
        this.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
            }
        });

        getEvents();
    }

    private void getEvents(){
        Api api = new Api("event/get");
        api.addData("order_by", "start, desc");
        api.addData("limit", 5);
        JSONArray events = api.getData();
        showEvents(events);
    }

    private void showEvents(JSONArray events){
        LinearLayout mainView = (LinearLayout) findViewById(R.id.mainView);
        mainView.removeAllViews();
        LinearLayout eventLayout;
        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject event = events.getJSONObject(i);
                String title, date, time;
                int id;
                try{
                    id = !event.isNull("event_id") ? (int) event.get("event_id") : -1;
                    title = !event.isNull("title") ? (String) event.get("title") : "No title";
                    String datetimeString = !event.isNull("start") ? (String)event.get("start") : "? ?";
                    String[] datetime = datetimeString.split(" ");
                    date = datetime[0];
                    time = datetime[1];
                    eventLayout = new EventUI(this, id, title, date, time);
                    final Context it = this;
                    final int event_id = id;
                    eventLayout.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(it, EventUnique.class);
                            Bundle b = new Bundle();
                            b.putInt("event_id", event_id);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                    mainView.addView(eventLayout);
                }catch(JSONException e){
                    System.out.println("Cannot cast JSON");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.refresher.setRefreshing(false);
    }
}
