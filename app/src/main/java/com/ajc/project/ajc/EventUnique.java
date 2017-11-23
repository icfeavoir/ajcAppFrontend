package com.ajc.project.ajc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.nio.file.Paths.get;
import static java.util.Objects.isNull;

public class EventUnique extends AppCompatActivity {

    private int event_id;
    private String creator;
    private String title;
    private String startDate;
    private String startHour;
    private String endDate;
    private String endHour;
    private String description;
    private int price;

    private int participate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_unique);

        Bundle b = getIntent().getExtras();
        this.event_id = -1; // or other values
        if(b != null) {
            this.event_id = b.getInt("event_id");
        }
        Api api = new Api("event/get");
        api.addData("event_id", event_id);
        try {
            JSONObject resp = api.getData().getJSONObject(0);

            Api creator = new Api("user/get");
            int creator_id = !resp.isNull("creator") ? (int) resp.get("creator") : 0;
            creator.addData("user_id", creator_id);
            JSONObject creatorJson = creator.getData().getJSONObject(0);
            this.creator = !creatorJson.isNull("firstName") ? ""+creatorJson.get("firstName") : "?";

            Api participateApi = new Api("participation/get");
            participateApi.addData("user_id", 1);
            participateApi.addData("event_id", this.event_id);
            JSONArray participateJsonArray = participateApi.getData();
            if(participateJsonArray.length() != 0) {
                JSONObject participateJson = participateJsonArray.getJSONObject(0);
                this.participate = !participateJson.isNull("participate") ? (int) participateJson.get("participate") : 0;
            }

            this.title = !resp.isNull("title") ? ""+resp.get("title") : "";

            String start = !resp.isNull("start") ? ""+resp.get("start") : "";
            String[] startArr = start.split(" ");
            this.startDate = startArr[0];
            this.startHour = startArr.length > 0 ? startArr[1] : "";

            String end = !resp.isNull("end") ? ""+resp.get("end") : "";
            String[] endArr = start.split(" ");
            this.endDate = endArr[0];
            this.endHour = endArr.length > 0 ? endArr[1] : "";

            this.description = !resp.isNull("description") ? ""+resp.get("description") : "";
            this.price = !resp.isNull("price") ? (int) resp.get("price") : 0;
        }catch(JSONException e){
            System.err.println("Cannot get event data. Error: "+e);
        }

        TextView tv = (TextView) findViewById(R.id.eventUniqueTitle);
        tv.setText(this.title);
        tv = (TextView) findViewById(R.id.eventUniqueDate);
        tv.setText(this.startDate);
        tv = (TextView) findViewById(R.id.eventUniqueHour);
        tv.setText(this.startHour);
        tv = (TextView) findViewById(R.id.eventUniqueDescription);
        tv.setText(this.description);
        tv = (TextView) findViewById(R.id.eventUniqueCreator);
        tv.setText("created by "+this.creator);
        tv = (TextView) findViewById(R.id.eventUniquePrice);
        tv.setText(this.price !=0 ? this.price+"€" : "Free!");

        final EventUnique it = this;
        final TextView[] buttons = {null, (TextView)findViewById(R.id.eventUniqueYes), (TextView)findViewById(R.id.eventUniqueNo), (TextView)findViewById(R.id.eventUniqueMaybe)};

        for(int i=0; i<buttons.length; i++) {
            TextView selectedButton = buttons[i];
            //default value (color)
            if (this.participate != 0 && this.participate == i) {
                selectedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if (selectedButton != null) {
                final int count = i;
                selectedButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int[] textColors = {0, getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimaryDark)};
                        textColors[count] = getResources().getColor(R.color.colorPrimary);

                        TextView byes = (TextView) findViewById(R.id.eventUniqueYes);
                        byes.setTextColor(textColors[1]);
                        TextView bno = (TextView) findViewById(R.id.eventUniqueNo);
                        bno.setTextColor(textColors[2]);
                        TextView bmaybe = (TextView) findViewById(R.id.eventUniqueMaybe);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle item selection
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
