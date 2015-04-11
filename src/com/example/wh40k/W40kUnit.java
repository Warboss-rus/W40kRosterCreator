package com.example.wh40k;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Urgak_000 on 21.03.2015.
 */

public class W40kUnit implements Cloneable, Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(basicCost);
        parcel.writeSerializable(slot);
        parcel.writeString(imagePath);
        parcel.writeString(description);
        parcel.writeInt(unique ? 1 : 0);
        parcel.writeList(options);
        parcel.writeList(models);
        parcel.writeList(optionSlots);
    }

    @Override
    public W40kUnit clone() {
        try {
            W40kUnit clone = (W40kUnit)super.clone();
            clone.options = new ArrayList<W40kOption>(this.options);
            clone.models = new ArrayList<W40kModel>(this.models);
            for(int i = 0; i < clone.models.size(); ++i) {
                clone.models.set(i, (W40kModel)clone.models.get(i).clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new W40kUnit();
    }

    public W40kUnitSlot getSlot() {
        return slot;
    }

    public void setSlot(final W40kUnitSlot slot) {
        this.slot = slot;
    }

    public void setSlot(final String slot) {
        if(slot.equals("hq")) {
            this.slot = W40kUnitSlot.HQ;
        } else if(slot.equals("troops")) {
            this.slot = W40kUnitSlot.TROOPS;
        } if(slot.equals("transport")) {
            this.slot = W40kUnitSlot.TRANSPORT;
        } if(slot.equals("elite")) {
            this.slot = W40kUnitSlot.ELITE;
        } if(slot.equals("fast_attack")) {
            this.slot = W40kUnitSlot.FAST_ATTACK;
        } if(slot.equals("heavy_support")) {
            this.slot = W40kUnitSlot.HEAVY_SUPPORT;
        } if(slot.equals("lord_of_war")) {
            this.slot = W40kUnitSlot.LORD_OF_WAR;
        } if(slot.equals("fortification")) {
            this.slot = W40kUnitSlot.FORTIFICATION;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBasicCost() {
        return basicCost;
    }

    public void setBasicCost(Integer basicCost) {
        this.basicCost = basicCost;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Integer getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(Integer basicValue) {
        this.basicValue = basicValue;
    }

    public List<W40kModel> getModels() {
        return models;
    }

    public void addModel(W40kModel model) {
        models.add(model);
    }

    public List<W40kOptionSlot> getOptionSlots() {
        return optionSlots;
    }

    public void addOptionSlot(W40kOptionSlot optionSlot) {
        optionSlots.add(optionSlot);
    }

    public double getBasicEfficiency() {
        return (basicCost != 0) ? basicValue / basicCost : basicValue / 0.1;
    }

    public List<W40kOption> getOptions() {
        return options;
    }

    public void addOption(W40kOption option) {
        this.options.add(option);
    }

    public Integer getCost() {
        Integer cost = this.basicCost;
        for(W40kOption option : options) {
            cost += option.getCost();
        }
        return cost;
    }

    public Integer getValue() {
        Integer value = this.basicValue;
        for(W40kOption option : options) {
            value += option.getValue();
        }
        return value;
    }

    public double getEfficiency() {
        return (double)getValue() / ((getCost() == 0)? 0.1 : (double)getCost());
    }

    public String toString() {
        return name + ": " + getCost().toString() + " pts.";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<W40kCombo> getCombos() {return combos;}

    public void addCombo(W40kUnit unit, Float multiplier) {
        combos.add(new W40kCombo(unit, multiplier));
    }

    public W40kModel getModelByName(String name) {
        for(W40kModel model : models) {
            if(model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }

    public Integer getNumberOfModels() {
        Integer num = 0;
        for(W40kModel model : models) {
            num += model.getDefaultCount();
        }
        return num;
    }

    public static enum W40kUnitSlot {
        HQ,
        TROOPS,
        TRANSPORT,
        ELITE,
        FAST_ATTACK,
        HEAVY_SUPPORT,
        LORD_OF_WAR,
        FORTIFICATION,
    }

    private W40kUnitSlot slot;
    private String name;
    private Integer basicCost;
    private Integer basicValue;
    private Boolean unique;
    private List<W40kModel> models = new ArrayList<W40kModel>();
    private List<W40kOptionSlot> optionSlots = new ArrayList<W40kOptionSlot>();
    private List<W40kCombo> combos = new ArrayList<W40kCombo>();
    private List<W40kOption> options = new ArrayList<W40kOption>();
    private String imagePath;
    private String description;

    public static final Parcelable.Creator<W40kUnit> CREATOR = new Parcelable.Creator<W40kUnit>(){
        @Override
        public W40kUnit createFromParcel(Parcel parcel) {
            W40kUnit unit = new W40kUnit();
            unit.name = parcel.readString();
            unit.basicCost = parcel.readInt();
            unit.slot = (W40kUnitSlot)parcel.readSerializable();
            unit.imagePath = parcel.readString();
            unit.description = parcel.readString();
            unit.unique = (parcel.readInt() == 1);
            parcel.readList(unit.options, W40kOption.class.getClassLoader());
            parcel.readList(unit.models, W40kModel.class.getClassLoader());
            parcel.readList(unit.optionSlots, W40kOptionSlot.class.getClassLoader());
            return unit;
        }

        @Override
        public W40kUnit[] newArray(int i) {
            return new W40kUnit[i];
        }
    };
}
