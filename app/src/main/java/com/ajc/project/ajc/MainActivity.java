package com.ajc.project.ajc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Api api = new Api("event/get");
        JSONArray events = api.getData();
        refreshEvents(events);
    }

    private void refreshEvents(JSONArray events){
        LinearLayout mainView = (LinearLayout) findViewById(R.id.mainView);
        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject event = events.getJSONObject(i);
                String title="", date="", time="";
                int id=-1;
                try{
                    id = !event.isNull("event_id") ? (int) event.get("event_id") : -1;
                    title = !event.isNull("title") ? (String) event.get("title") : "No title";
                    String datetimeString = !event.isNull("start") ? (String)event.get("start") : "? ?";
                    String[] datetime = datetimeString.split(" ");
                    date = datetime[0];
                    time = datetime[1];
                    mainView.addView(new EventUI(this, id, title, date, time));
                }catch(JSONException e){
                    System.out.println("Cannot cast JSON");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
