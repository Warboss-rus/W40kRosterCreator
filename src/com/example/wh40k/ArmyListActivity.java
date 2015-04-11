package com.example.wh40k;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.IOException;
import java.io.InputStream;
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

    private String [] army = {
            "Space Marines"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.army_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, army);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerArmyList = (Spinner) findViewById(R.id.spinnerRace);
        spinnerArmyList.setAdapter(adapter);

        List<W40kUnit> roster = new ArrayList<W40kUnit>();
        this.units = roster;

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
                startActivity(intent);
            }
        });

        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for(W40kUnit unit : roster) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("name", unit.toString());
            item.put("options", unit.getOptions().toString());
            items.add(item);
        }

        ListView lw = (ListView)findViewById(R.id.listView3);
        SimpleAdapter mAdapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, new String[]{"name", "options"}, new int[]{android.R.id.text1, android.R.id.text2});
        lw.setAdapter(mAdapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                W40kUnit unit = units.get(position);
                Intent intent = new Intent(ArmyListActivity.this, infoUnit.class);
                intent.putExtra("unit", unit);
                startActivity(intent);
            }
        });
    }
}
