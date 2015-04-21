package com.example.wh40k;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by user on 12.04.2015.
 */
public class GoogleAuthorizeTask extends AsyncTask<Void, Void, Void>{

    note mActivity;
    String mScope;
    String mEmail;

    public GoogleAuthorizeTask(note activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            //String token = fetchToken();
            //if (token != null) {
                // Insert the good stuff here.
                // Use the token to access the user's Google data.

                Event event = new Event();
                EditText desc = (EditText)mActivity.findViewById(R.id.editText);
                EditText location = (EditText)mActivity.findViewById(R.id.editText2);
                DatePicker datePicker = (DatePicker)mActivity.findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker)mActivity.findViewById(R.id.timePicker);

                event.setSummary(desc.getText().toString());
                event.setLocation(location.getText().toString());

                //DateTime start = new DateTime(datePicker.getCalendarView().getDate());
                String dateStart = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " " +
                        timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                DateTime start = new DateTime(format.parse(dateStart));
                event.setStart(new EventDateTime().setDateTime(start));

                Date endDate = new Date(start.getValue() + 4*60*60*1000);
                DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
                event.setEnd(new EventDateTime().setDateTime(end));

                // Insert the new event
                Event createdEvent = mActivity.mService.events().insert("primary", event).execute();
                createdEvent.getDescription();
                //Toast.makeText(mActivity.getApplicationContext(), "Заметка успешно добавлена", Toast.LENGTH_LONG).show();
        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(), mActivity.REQUEST_CODE_RECOVER_FROM_AUTH_ERROR);
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        this.cancel(true);
    }
}
