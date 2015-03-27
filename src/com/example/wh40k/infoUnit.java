package com.example.wh40k;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dertkaes on 3/25/2015.
 */

public class infoUnit extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_upgrades);

        W40kUnit unit = getIntent().getParcelableExtra("unit");
        ListView upgrades = (ListView)findViewById(R.id.listView2);
        View header = getLayoutInflater().inflate(R.layout.info_header, null);
        upgrades.addHeaderView(header);

        TextView name = (TextView)findViewById(R.id.textView3);
        name.setText(unit.getName());
        TextView description = (TextView)findViewById(R.id.textView4);
        description.setText(unit.getDescription());

        TableLayout stats = (TableLayout)findViewById(R.id.statsTable);
        stats.setStretchAllColumns(true);
        stats.bringToFront();
        W40kModelType.BasicType lastType = null;
        for(W40kModel model : unit.getModels()) {
            if(model.getType().getBasicType() != W40kModelType.BasicType.VEHICLE) {
                if(lastType == null || lastType == W40kModelType.BasicType.VEHICLE) {
                    addInfantryHeader(stats);
                }
                TableRow tr =  new TableRow(this);
                AddText(tr, model.getDefaultCount().toString());
                AddText(tr, model.getName());
                AddText(tr, model.getWeaponSkill().toString());
                AddText(tr, model.getBallisticSkill().toString());
                AddText(tr, model.getStrength().toString());
                AddText(tr, model.getToughness().toString());
                AddText(tr, model.getWounds().toString());
                AddText(tr, model.getInitiative().toString());
                AddText(tr, model.getAttacks().toString());
                AddText(tr, model.getLeadership().toString());
                AddText(tr, model.getSave().toString() + "+");
                //AddText(tr, ""model.getType().toString());
                stats.addView(tr);
            } else {
                if(lastType == null || lastType != W40kModelType.BasicType.VEHICLE) {
                    addVehicleHeader(stats);
                }
                boolean isWalker = model.getType().getModifiers().contains(W40kModelType.TypeModifiers.WALKER);
                TableRow tr =  new TableRow(this);
                AddText(tr, model.getDefaultCount().toString());
                AddText(tr, model.getName());
                AddText(tr, (isWalker) ? model.getWeaponSkill().toString() : "");
                AddText(tr, model.getBallisticSkill().toString());
                AddText(tr, (isWalker)?model.getStrength().toString() : "");
                AddText(tr, model.getFrontArmour().toString());
                AddText(tr, model.getSideArmour().toString());
                AddText(tr, model.getRearArmour().toString());
                AddText(tr, model.getHullPoints().toString());
                AddText(tr, (isWalker)?model.getInitiative().toString() : "");
                AddText(tr, (isWalker)?model.getAttacks().toString() : "");
                //AddText(tr, ""model.getType().toString());
                stats.addView(tr);
            }
            lastType = model.getType().getBasicType();
        }

        List<W40kOption> options = unit.getOptions();
        List<Map<String, String>> items = new ArrayList<Map<String, String>>();
        for(W40kOption option : options) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("name", option.getName());
            item.put("description", option.getDescription());
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, new String[]{"name", "description"}, new int[]{android.R.id.text1, android.R.id.text2});
        upgrades.setAdapter(adapter);
        upgrades.requestLayout();
        new DownloadImageTask((ImageView)findViewById(R.id.imageView)).execute(unit.getImagePath());
    }

    private void addVehicleHeader(TableLayout view) {
        TableRow tr = new TableRow(this);
        AddText(tr, "#");
        AddText(tr, "Name");
        AddText(tr, "WS");
        AddText(tr, "BS");
        AddText(tr, "S");
        AddText(tr, "FA");
        AddText(tr, "SA");
        AddText(tr, "RA");
        AddText(tr, "HP");
        AddText(tr, "I");
        AddText(tr, "A");
        //AddText(tr, "type");
        view.addView(tr);
    }

    private void addInfantryHeader(TableLayout view) {
        TableRow tr = new TableRow(this);
        AddText(tr, "#");
        AddText(tr, "Name");
        AddText(tr, "WS");
        AddText(tr, "BS");
        AddText(tr, "S");
        AddText(tr, "T");
        AddText(tr, "W");
        AddText(tr, "I");
        AddText(tr, "A");
        AddText(tr, "Ld");
        AddText(tr, "Sv");
        //AddText(tr, "type");
        view.addView(tr);
    }

    private void AddText(TableRow tr, String text) {
        TextView tw = new TextView(this);
        tw.setText(text);
        tr.addView(tw);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            InputStream in = null;
            try {
                in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            } finally {
                if(in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}