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

        Button test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api api = new Api("event/get");
                JSONArray events = api.getData();
                refreshEvents(events);
            }
        });
    }

    private void refreshEvents(JSONArray events){
        LinearLayout mainView = (LinearLayout) findViewById(R.id.mainView);
        for (int i = 0; i < events.length(); i++) {
            try {
                JSONObject event = events.getJSONObject(i);
//                TextView title = new TextView(this);
//                title.setText(""+event.get("title"));
//                title.setGravity(Gravity.CENTER);
//                title.setTextColor(getResources().getColor(R.color.white));
                mainView.addView(new EventUI(this, ""+event.get("title")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
