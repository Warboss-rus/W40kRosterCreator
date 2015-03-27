package com.example.wh40k;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.InputStream;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import android.widget.*;
import android.view.View;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    String [] army = {"Adepta Sororitas",
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

    private CodexXMLParser parser = new CodexXMLParser();

    String [] points = {"500", "750", "1000", "1250", "1500", "1850", "2000", "2500"};

    private void MakeToast(final String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

                    final InputStream is = getResources().getAssets().open("SpaceMarines.xml");
                    W40kCodex codex = parser.LoadCodex(is);
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
                    List<W40kUnit> roster = selection.selection(codex.getUnits());
                    Intent intent = new Intent(MyActivity.this, Roster.class);
                    intent.putParcelableArrayListExtra("roster", new ArrayList<W40kUnit>(roster));

                    startActivity(intent);
                } catch (Exception e) {
                    MakeToast(e.getLocalizedMessage());
                    Log.d("UnitSelectionError: ", e.getMessage());
                }
            }
        });


    }
}
