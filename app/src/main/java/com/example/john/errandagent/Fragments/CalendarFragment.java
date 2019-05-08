package com.example.john.errandagent.Fragments;

import android.content.ContentValues;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.john.errandagent.R;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CalendarFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //CalendarView calendarView = getView().findViewById(R.id.calendarView);
        //calendarView.add

        ExtendedCalendarView calendar = (ExtendedCalendarView) getView().findViewById(R.id.calendar);

        ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_RED);
        values.put(CalendarProvider.DESCRIPTION, "Some Description");
        values.put(CalendarProvider.LOCATION, "Some location");
                values.put(CalendarProvider.EVENT, "Event name");

                        Calendar cal = Calendar.getInstance();
//
//        cal.set(CAL);
//        values.put(CalendarProvider.START, cal.getTimeInMillis());
//        values.put(CalendarProvider.START_DAY, julianDay);
//        TimeZone tz = TimeZone.getDefault();
//
//        cal.set(endDayYear, endDayMonth, endDayDay, endTimeHour, endTimeMin);
//        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//
//        values.put(CalendarProvider.END, cal.getTimeInMillis());
//        values.put(CalendarProvider.END_DAY, endDayJulian);
//
//        Uri uri = getContentResolver().insert(CalendarProvider.CONTENT_URI, values);


        super.onActivityCreated(savedInstanceState);
    }
}
