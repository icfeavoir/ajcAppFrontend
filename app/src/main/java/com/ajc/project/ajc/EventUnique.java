package com.ajc.project.ajc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventUnique extends AppCompatActivity {

    private int MY_ID;

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

    SwipeRefreshLayout refresher;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_unique);

        SharedPreferences prefs = this.getSharedPreferences("AJC_VAR", MODE_PRIVATE);
        this.MY_ID = prefs.getInt("MY_ID", 0);

        Bundle b = getIntent().getExtras();
        this.event_id = -1;
        if (b != null) {
            this.event_id = b.getInt("event_id");
        }

        this.refresher = (SwipeRefreshLayout) findViewById(R.id.activity_event_unique_refresh);
        this.refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvent();
            }
        });
        getEvent();
    }

    private void getEvent(){
        Api api = new Api("event/get");
        api.addData("event_id", this.event_id);
        try {
            JSONObject resp = api.getData().getJSONObject(0);

            Api creator = new Api("user/get");
            int creator_id = !resp.isNull("creator") ? (int) resp.get("creator") : 0;
            creator.addData("user_id", creator_id);
            JSONObject creatorJson = creator.getData().getJSONObject(0);
            this.creator = !creatorJson.isNull("firstName") ? ""+creatorJson.get("firstName") : "?";

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

        // PARTICIPATIONS
        Api participations = new Api("participation/get");
        participations.addData("event_id", this.event_id);
        JSONArray participationsJson = participations.getData();

        GridLayout grid = (GridLayout)findViewById(R.id.eventUniqueParticipateGrid);
        grid.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        int[] color_participate = {0, R.color.success, R.color.danger, R.color.warning};
        String[] string_participate = {null, "Oui", "Non", "Peut-être"};
        for(int i=0; i<participationsJson.length(); i++) {
            try {
                JSONObject user_participate = participationsJson.getJSONObject(i);
                if(!user_participate.isNull("user_id") && !user_participate.isNull("participate")) {
                    int user_id = user_participate.getInt("user_id");
                    int participate = user_participate.getInt("participate");
                    if(user_id == this.MY_ID){
                        this.participate = participate;
                    }
                    Api user_api = new Api("user/get");
                    user_api.addData("user_id", user_id);
                    JSONObject userJson = user_api.getData().getJSONObject(0);
                    if(user_id != this.MY_ID && !userJson.isNull("firstName")) {
                        String user_name = ""+userJson.get("firstName");
                        LinearLayout ll = new LinearLayout(this);
                        View view = inflater.inflate(R.layout.activity_event_unique, grid, false);
                        LinearLayout row = view.findViewById(R.id.eventUniqueParticipateList);
                        TextView name = view.findViewById(R.id.eventUniqueParticipateName);
                        TextView answer = view.findViewById(R.id.eventUniqueParticipateAnswer);
                        if (name.getParent() != null) {
                            ((ViewGroup) name.getParent()).removeView(name);
                        }
                        if (answer.getParent() != null) {
                            ((ViewGroup) answer.getParent()).removeView(answer);
                        }
                        name.setText(user_name);
                        answer.setText(string_participate[participate]);
                        answer.setTextColor(getResources().getColor(color_participate[participate]));
                        row.addView(name);
                        row.addView(answer);
                        if (row.getParent() != null) {
                            ((ViewGroup) row.getParent()).removeView(row);
                        }
                        System.out.println("ok "+i);
                        grid.addView(row);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
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

        for(int i=1; i<buttons.length; i++) {
            TextView selectedButton = buttons[i];
            //default value (color)
            if (this.participate != 0 && this.participate == i) {
                selectedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else{
                selectedButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
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
                        api.addData("user_id", it.MY_ID);
                        api.addData("participate", count);
                        api.call();
                    }
                });
            }
        }
        this.refresher.setRefreshing(false);
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
