package com.example.wh40k;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Urgak_000 on 11.04.2015.
 */
public class ArmyListActivity extends Activity {

    private List<W40kUnit> units;
    private W40kCodex codex;
    private Gson gson = new GsonBuilder().create();
    private SharedPreferences prefs;

    private String [] army = {
            "Space Marines"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.army_list);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, army);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerArmyList = (Spinner) findViewById(R.id.spinnerRace);
        spinnerArmyList.setAdapter(adapter);

        LoadMyArmy("Space Marines");

        CodexXMLParser parser = new CodexXMLParser();
        try {
            final InputStream is = getResources().getAssets().open/*openFileInput*/("SpaceMarines.xml");
            codex = parser.LoadCodex(is);
        } catch (IOException e) {
            Log.d("Codex parsing error", e.getMessage());
        }

        final Spinner unitSelector = (Spinner) findViewById(R.id.spinner3);
        unitSelector.setAdapter(new ArrayAdapter<W40kUnit>(this, android.R.layout.simple_list_item_1, codex.getUnits()));

        Button addNew = (Button) findViewById(R.id.button5);
        addNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArmyListActivity.this, ArmyUnitOptionsActivity.class);
                intent.putExtra("unit", codex.getUnits().get(unitSelector.getSelectedItemPosition()));
                startActivityForResult(intent, 0);
            }
        });

        UpdateUnitList();

        ListView lw = (ListView)findViewById(R.id.listView3);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                W40kUnit unit = units.get(position);
                Intent intent = new Intent(ArmyListActivity.this, ArmyUnitOptionsActivity.class);
                intent.putExtra("unit", unit);
                intent.putExtra("index", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void LoadMyArmy(String armyName) {
        String savedValue = prefs.getString("myArmy" + armyName, "");
        if(savedValue.equals("")) {
            units = new ArrayList<W40kUnit>();
        } else {
            try {
                Type listType = new TypeToken<ArrayList<W40kUnit>>(){}.getType();
                units = gson.fromJson(savedValue, listType);
            } catch (Exception e) {
                Log.d("GSON error", e.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                units = new ArrayList<W40kUnit>();
            }
        }
    }

    private void UpdateUnitList() {
        //Save to context
        Spinner spinnerArmyList = (Spinner) findViewById(R.id.spinnerRace);
        String jsonUnits = gson.toJson(units);
        prefs.edit().putString("myArmy" + spinnerArmyList.getSelectedItem().toString(), jsonUnits).apply();
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for(W40kUnit unit : units) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("name", unit.toString());
            item.put("options", unit.getOptionsString());
            items.add(item);
        }

        ListView lw = (ListView)findViewById(R.id.listView3);
        SimpleAdapter mAdapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, new String[]{"name", "options"}, new int[]{android.R.id.text1, android.R.id.text2});
        lw.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        W40kUnit unit = data.getParcelableExtra("unit");
        if(unit == null) {
            if(requestCode != 0) {
                units.remove(data.getIntExtra("index", 0));
            }
        } else {
            if (requestCode == 0) {//new unit
                units.add(unit);
            } else {
                units.set(data.getIntExtra("index", 0), unit);
            }
        }
        UpdateUnitList();
    }
}
