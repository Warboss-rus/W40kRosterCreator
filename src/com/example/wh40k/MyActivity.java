package com.example.wh40k;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;

import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.widget.*;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private String [] army = {"Adepta Sororitas",
            "Astra Militarum",
            "Blood Angels",
            "Chaos Daemons",
            "Chaos Space Marines",
            "Dark Angels",
            "Dark Eldar",
            "Eldar",
            "Grey Knights",
            "Harlequins",
            "Imperial Knights",
            "Inquisition",
            "Khorne Daemonkin",
            "Militarum Tempestus",
            "Necrons",
            "Officio Assassinorum",
            "Orks",
            "Space Marines",
            "Space Wolves",
            "Tau Empire",
            "Tyranids"
    };

    private SharedPreferences prefs;
    private Gson gson = new GsonBuilder().create();

    private CodexXMLParser parser = new CodexXMLParser();

    private String [] points = {"500", "750", "1000", "1250", "1500", "1850", "2000", "2500"};

    private void MakeToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private SocialAuthAdapter socialAdapter;
    private ServerCommunicator server = new ServerCommunicator("http://192.168.43.109:8080/CServlet");

    private final class ResponseListener implements DialogListener {
        public void onComplete(Bundle values) {
            Log.d("ShareButton", "Authentication Successful");

            Profile profileMap =  socialAdapter.getUserProfile();
            Log.d("Custom-UI", "First Name = "       + profileMap.getFirstName());
            Log.d("Custom-UI", "Last Name  = "       + profileMap.getLastName());
            Log.d("Custom-UI", "Email      = " + profileMap.getEmail());
            Log.d("Custom-UI", "Profile Image URL  = " + profileMap.getProfileImageURL());

            Map<String, String> userData = new HashMap<String, String>();
            userData.put("firstName", profileMap.getFirstName());
            userData.put("lastName", profileMap.getLastName());
            userData.put("email", profileMap.getEmail());
            server.reportUserLogin(userData);
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            Log.d("ShareButton" , "Error");
        }

        @Override
        public void onCancel() {
            Log.d("ShareButton" , "Cancelled");
        }

        @Override
        public void onBack() {
            Log.d("ShareButton" , "Cancelled");
        }
    }

    public void GetVersionComplete(Integer version) {
        Integer localVersion = Integer.MAX_VALUE;
        try {
            final InputStream is = openFileInput("SpaceMarines.xml");
            localVersion = parser.getCodexVersion(is);
        } catch (IOException e) {
            Log.d("GetVersion", e.getMessage());
        }
        if(version > localVersion) {
            server.downloadCodex("SpaceMarines.xml", getApplicationContext());
        }
    }
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, army);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerArmyList = (Spinner) findViewById(R.id.spinner);
        spinnerArmyList.setAdapter(adapter);
        // заголовок
        spinnerArmyList.setPrompt("Army");
        // адаптер
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, points);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerPointsList = (Spinner) findViewById(R.id.spinner2);
        spinnerPointsList.setAdapter(adapter1);
        // заголовок
        spinnerPointsList.setPrompt("Points");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Integer points = Integer.parseInt(spinnerPointsList.getSelectedItem().toString());
                    UnitSelection selection = new UnitSelection(points);
                    selection.setMaxHq(2);
                    selection.setMinHq(1);
                    selection.setMaxTroops(6);
                    selection.setMinTroops(2);
                    selection.setMaxFastAttack(3);
                    selection.setMaxElites(3);
                    selection.setMaxHeavySupport(3);
                    selection.setMaxFort(1);
                    selection.setMaxLoW(1);
                    List<W40kUnit> roster;
                    CheckBox useMyArmy = (CheckBox)findViewById(R.id.checkBox);
                    if(useMyArmy.isChecked()) {
                        String race = "Space Marines";//((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                        String savedValue = prefs.getString("myArmy" + race, "");
                        List<W40kUnit> units;
                        if(savedValue.equals("")) {
                            units = new ArrayList<W40kUnit>();
                        } else {
                            try {
                                Type listType = new TypeToken<ArrayList<W40kUnit>>(){}.getType();
                                units = gson.fromJson(savedValue, listType);
                            } catch (Exception e) {
                                Log.d("GSON error", e.getLocalizedMessage());
                                units = new ArrayList<W40kUnit>();
                            }
                        }
                        roster = selection.selection(units);//Replace it with another call that will ignore optionSlots
                    } else {
                        final InputStream is = getResources().getAssets().open/*openFileInput*/("SpaceMarines.xml");
                        W40kCodex codex = parser.LoadCodex(is);
                        roster = selection.selection(codex.getUnits());
                    }
                    Intent intent = new Intent(MyActivity.this, Roster.class);
                    intent.putParcelableArrayListExtra("roster", new ArrayList<W40kUnit>(roster));
                    startActivity(intent);
                } catch (IOException e) {
                    MakeToast(e.getLocalizedMessage());
                    Log.d("UnitSelectionError: ", e.getMessage());
                }
            }
        });

        Button login = (Button)findViewById(R.id.button3);
        socialAdapter = new SocialAuthAdapter(new ResponseListener());
        socialAdapter.addProvider(SocialAuthAdapter.Provider.FACEBOOK, R.drawable.facebook);
        socialAdapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
        socialAdapter.addProvider(SocialAuthAdapter.Provider.LINKEDIN, R.drawable.linkedin);
        socialAdapter.addProvider(SocialAuthAdapter.Provider.MYSPACE, R.drawable.myspace);
        socialAdapter.addProvider(SocialAuthAdapter.Provider.GOOGLE, R.drawable.google);
        socialAdapter.addProvider(SocialAuthAdapter.Provider.YAMMER, R.drawable.yammer);
        socialAdapter.addCallBack(SocialAuthAdapter.Provider.TWITTER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
        socialAdapter.addCallBack(SocialAuthAdapter.Provider.YAMMER, "http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

        socialAdapter.enable(login);

        /*Button downloadCodices = (Button)findViewById(R.id.button4);
        downloadCodices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                server.getCodexVersion("SpaceMarines.xml", MyActivity.this);
            }
        });*/

        Button myArmy = (Button)findViewById(R.id.button6);
            myArmy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, ArmyListActivity.class);
                startActivity(intent);
            }
        });
    }
}
