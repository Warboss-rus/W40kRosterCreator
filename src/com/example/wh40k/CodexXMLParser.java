package com.example.wh40k;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Urgak_000 on 24.03.2015.
 */
public class CodexXMLParser {
    public W40kCodex LoadCodex(InputStream stream) throws XmlPullParserException, IOException {
        W40kCodex codex = new W40kCodex();
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, "", "units");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if(name == null) continue;
            if (name.equals("unit")) {
                W40kUnit unit = ParseUnit(parser, codex);
                codex.addUnit(unit);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, "", "units");
        return codex;
    }

    W40kUnit ParseUnit(XmlPullParser parser, W40kCodex codex) throws XmlPullParserException, IOException{
        W40kUnit unit = new W40kUnit();
        parser.require(XmlPullParser.START_TAG, "", "unit");
        unit.setSlot(parser.getAttributeValue(null, "slot"));
        unit.setName(parser.getAttributeValue(null, "name"));
        unit.setBasicCost(Integer.parseInt(parser.getAttributeValue(null, "cost")));
        unit.setBasicValue(Integer.parseInt(parser.getAttributeValue(null, "value")));
        unit.setUnique(Boolean.parseBoolean(parser.getAttributeValue(null, "unique")));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if(name.equals("model")) {
                W40kModel model = ParseModel(parser);
                unit.addModel(model);
            } else if(name.equals("options")) {
                W40kOptionSlot optionSlot = ParseOptions(parser);
                unit.addOptionSlot(optionSlot);
            }  else if(name.equals("image")) {
                parser.require(XmlPullParser.START_TAG, "", "image");
                String imagePath = readText(parser, "image");
                if(imagePath != null) unit.setImagePath(imagePath);
                parser.require(XmlPullParser.END_TAG, "", "image");
            } else if(name.equals("description")) {
                parser.require(XmlPullParser.START_TAG, "", "description");
                String description = readText(parser, "description");
                if(description != null) unit.setDescription(description);
                parser.require(XmlPullParser.END_TAG, "", "description");
            } else if(name.equals("combo")) {
                parser.require(XmlPullParser.START_TAG, "", "combo");
                String comboUnit = parser.getAttributeValue("", "name");
                Float multiplier = Float.parseFloat(parser.getAttributeValue("", "multiplier"));
                unit.addCombo(codex.getUnitByName(comboUnit), multiplier);
                parser.next();
                parser.require(XmlPullParser.END_TAG, "", "combo");
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, "", "unit");
        return unit;
    }

    W40kModel ParseModel(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, "", "model");
        W40kModel model = new W40kModel();
        model.setType(parser.getAttributeValue(null, "type"));
        model.setName(parser.getAttributeValue(null, "name"));
        String ws = parser.getAttributeValue(null, "WS");
        if(ws != null) model.setWeaponSkill(Integer.parseInt(ws));
        model.setBallisticSkill(Integer.parseInt(parser.getAttributeValue(null, "BS")));
        if(model.getType().getBasicType() != W40kModelType.BasicType.VEHICLE) {
            model.setStrength(Integer.parseInt(parser.getAttributeValue(null, "S")));
            model.setToughness(Integer.parseInt(parser.getAttributeValue(null, "T")));
            model.setWounds(Integer.parseInt(parser.getAttributeValue(null, "W")));
            model.setInitiative(Integer.parseInt(parser.getAttributeValue(null, "I")));
            model.setAttacks(Integer.parseInt(parser.getAttributeValue(null, "A")));
            model.setLeadership(Integer.parseInt(parser.getAttributeValue(null, "Ld")));
            model.setSave(Integer.parseInt(parser.getAttributeValue(null, "Sv")));
        } else {
            model.setFrontArmour(Integer.parseInt(parser.getAttributeValue(null, "FA")));
            model.setSideArmour(Integer.parseInt(parser.getAttributeValue(null, "SA")));
            model.setRearArmour(Integer.parseInt(parser.getAttributeValue(null, "RA")));
            model.setHullPoints(Integer.parseInt(parser.getAttributeValue(null, "HP")));
        }
        model.setDefaultCount(Integer.parseInt(parser.getAttributeValue(null, "count")));
        model.setMaxCount(Integer.parseInt(parser.getAttributeValue(null, "max")));
        String cost = parser.getAttributeValue(null, "cost");
        if(cost != null) model.setCost(Integer.parseInt(cost));
        String value = parser.getAttributeValue(null, "value");
        if(value != null) model.setValue(Integer.parseInt(value));
        parser.next();
        parser.require(XmlPullParser.END_TAG, "", "model");
        return model;
    }

    W40kOptionSlot ParseOptions(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, "", "options");
        W40kOptionSlot slot = new W40kOptionSlot();
        slot.setMax(Integer.parseInt(parser.getAttributeValue(null, "max")));
        String requredModels = parser.getAttributeValue(null, "requiresModels");
        slot.setUpgradesPerModels((requredModels != null) ? Integer.parseInt(requredModels) : 0);
        String onePerModel = parser.getAttributeValue(null, "onePerModel");
        slot.setOnePerModel((onePerModel != null) ? Boolean.parseBoolean(onePerModel) : false);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;
            String name = parser.getName();
            if (name.equals("option")) {
                parser.require(XmlPullParser.START_TAG, "", "option");
                W40kOption option = new W40kOption();
                option.setName(parser.getAttributeValue(null, "name"));
                option.setCost(Integer.parseInt(parser.getAttributeValue(null, "cost")));
                option.setValue(Integer.parseInt(parser.getAttributeValue(null, "value")));
                option.setDescription(parser.getAttributeValue(null, "description"));
                slot.addOption(option);
                parser.next();
                parser.require(XmlPullParser.END_TAG, "", "option");
            }  else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, "", "options");
        return slot;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(XmlPullParser parser, String field) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "", field);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, "", field);
        return result;
    }
}
