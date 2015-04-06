package com.example.wh40k;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dertkaes on 3/26/2015.
 */
public class note extends Activity {

    private static JsonFactory JSON_FACTORY = new JacksonFactory();
    private static HttpTransport httpTransport = new NetHttpTransport();
    private static List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/calendar", "https://www.googleapis.com/auth/calendar.readonly");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);
    }

    public void onEventCreateClick(View v) {
        try {
            Credential credential = authorize();
            /*GoogleAccountCredential credential =
                    GoogleAccountCredential.usingOAuth2(this, scopes);
            SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
            credential.setSelectedAccountName(settings.getString("rostergovna@gmail.com", null));
            // Tasks client
            Task service =
                    new com.google.api.services.tasks.Tasks.Builder(httpTransport, jsonFactory, credential)
                            .setApplicationName("Google-TasksAndroidSample/1.0").build();*/

            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName("applicationName/1.0").build();

            Event event = new Event();
            EditText desc = (EditText)findViewById(R.id.editText);
            DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);

            event.setSummary(desc.getText().toString());
            event.setLocation("Somewhere");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(datePicker);

            Date startDate = new Date();
            DateTime start = new DateTime(dateString);
            event.setStart(new EventDateTime().setDateTime(start));

            Date endDate = new Date(start.getValue() + 3600000);
            DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
            event.setEnd(new EventDateTime().setDateTime(end));

            // Insert the new event
            Event createdEvent = service.events().insert("primary", event).execute();

        } catch (Exception e) {
            Log.d("Calendar event creation error: ", e.getMessage());
        }
    }
    private static Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(note.class.getResourceAsStream("client_secrets.json")));
        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                scopes)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }
}