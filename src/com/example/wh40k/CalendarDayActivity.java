package com.example.wh40k;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.Calendar;

/**
 * Created by Urgak_000 on 27.04.2015.
 */
public class CalendarDayActivity extends Fragment {
    Calendar date;
    MyActivity parent;

    void setDate(Calendar calendar) {
        date = calendar;
    }

    void setContext(MyActivity context) {
        parent = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar, container, false);


        TextView header = (TextView)rootView.findViewById(R.id.textView);
        header.setText(date.toString());
        Log.d("date", date.toString());

        ButtonFloat button = (ButtonFloat)rootView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parent, note.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
