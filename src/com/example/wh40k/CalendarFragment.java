package com.example.wh40k;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Urgak_000 on 26.04.2015.
 */
public class CalendarFragment extends Fragment {

    MyActivity parent;

    public void setContext(MyActivity context) {
        parent = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.calendar, container, false);

        CalendarView calendarView = (CalendarView)rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar date = new GregorianCalendar(year, month, dayOfMonth);
                Intent intent = new Intent(parent, CalendarDayActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
