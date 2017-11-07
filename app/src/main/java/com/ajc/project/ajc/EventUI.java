package com.ajc.project.ajc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pierre on 2017-11-07.
 */

public class EventUI extends LinearLayout {

    public EventUI(Context context, String title){
        super(context);

        View.inflate(context, R.layout.event_layout, this);
        TextView tv = (TextView) findViewById(R.id.titleText);
        tv.setText(title);

//        TableLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 50, 50, 0);
//        this.setLayoutParams(lp);
//
//        GradientDrawable border = new GradientDrawable();
//        border.setStroke(5, 0xFF000000); //black border with full opacity
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            this.setBackgroundDrawable(border);
//        } else {
//            this.setBackground(border);
//        }
//
//        TableRow titleRow = new TableRow(context);
//        TextView titleView = new TextView(context);
//        titleView.setText(title);
//        titleView.setGravity(Gravity.CENTER);
//        border.setStroke(3, 0xFFE3FF00); //black border with full opacity
//
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            titleRow.setBackgroundDrawable(border);
//        } else {
//            titleRow.setBackground(border);
//        }
//        titleView.setTextColor(getResources().getColor(R.color.white));
//        titleRow.addView(titleView);
//
//        this.addView(titleRow);
    }
}
