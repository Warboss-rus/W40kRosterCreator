package com.example.wh40k;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Urgak_000 on 21.03.2015.
 */
public class W40kModel implements Parcelable {
    private String name;
    private Integer weaponSkill;
    private Integer ballisticSkill;
    private Integer strength;
    private Integer toughness;
    private Integer wounds;
    private Integer initiative;
    private Integer attacks;
    private Integer leadership;
    private Integer save;
    private Integer invulSave;
    private W40kModelType type;
    private Integer defaultCount;
    private Integer maxCount;
    private Integer frontArmour;
    private Integer sideArmour;
    private Integer rearArmour;
    private Integer hullPoints;
    private Integer cost;
    private Integer value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt((weaponSkill == null)?0:weaponSkill);
        parcel.writeInt((ballisticSkill == null)?0:ballisticSkill);
        parcel.writeInt((strength == null)?0:strength);
        parcel.writeInt((toughness == null)?0:toughness);
        parcel.writeInt((wounds == null)?0:wounds);
        parcel.writeInt((initiative == null)?0:initiative);
        parcel.writeInt((attacks == null)?0:attacks);
        parcel.writeInt((leadership == null)?0:leadership);
        parcel.writeInt((save == null)?0:save);
        parcel.writeInt((invulSave == null)?0:invulSave);
        parcel.writeValue(type);
        parcel.writeInt(defaultCount);
        parcel.writeInt(maxCount);
        parcel.writeInt((frontArmour == null)?0:frontArmour);
        parcel.writeInt((sideArmour == null)?0:sideArmour);
        parcel.writeInt((rearArmour == null)?0:rearArmour);
        parcel.writeInt((hullPoints == null)?0:hullPoints);
        parcel.writeInt((cost == null)?0:cost);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeaponSkill() {
        return weaponSkill;
    }

    public void setWeaponSkill(Integer weaponSkill) {
        this.weaponSkill = weaponSkill;
    }

    public Integer getBallisticSkill() {
        return ballisticSkill;
    }

    public void setBallisticSkill(Integer ballisticSkill) {
        this.ballisticSkill = ballisticSkill;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }

    public Integer getToughness() {
        return toughness;
    }

    public void setToughness(Integer toughness) {
        this.toughness = toughness;
    }

    public Integer getWounds() {
        return wounds;
    }

    public void setWounds(Integer wounds) {
        this.wounds = wounds;
    }

    public Integer getInitiative() {
        return initiative;
    }

    public void setInitiative(Integer initiative) {
        this.initiative = initiative;
    }

    public Integer getAttacks() {
        return attacks;
    }

    public void setAttacks(Integer attacks) {
        this.attacks = attacks;
    }

    public Integer getLeadership() {
        return leadership;
    }

    public void setLeadership(Integer leadership) {
        this.leadership = leadership;
    }

    public Integer getSave() {
        return save;
    }

    public void setSave(Integer save) {
        this.save = save;
    }

    public Integer getInvulSave() {
        return invulSave;
    }

    public void setInvulSave(Integer invulSave) {
        this.invulSave = invulSave;
    }

    public W40kModelType getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = new W40kModelType(type);
    }

    public Integer getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(Integer defaultCount) {
        this.defaultCount = defaultCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getFrontArmour() {
        return frontArmour;
    }

    public void setFrontArmour(Integer frontArmour) {
        this.frontArmour = frontArmour;
    }

    public Integer getSideArmour() {
        return sideArmour;
    }

    public void setSideArmour(Integer sideArmour) {
        this.sideArmour = sideArmour;
    }

    public Integer getRearArmour() {
        return rearArmour;
    }

    public void setRearArmour(Integer rearArmour) {
        this.rearArmour = rearArmour;
    }

    public Integer getHullPoints() {
        return hullPoints;
    }

    public void setHullPoints(Integer hullPoints) {
        this.hullPoints = hullPoints;
    }

    public static final Parcelable.Creator<W40kModel> CREATOR = new Parcelable.Creator<W40kModel>(){
        @Override
        public W40kModel createFromParcel(Parcel parcel) {
            W40kModel model = new W40kModel();
            model.name = parcel.readString();
            model.weaponSkill = parcel.readInt();
            model.ballisticSkill = parcel.readInt();
            model.strength = parcel.readInt();
            model.toughness = parcel.readInt();
            model.wounds = parcel.readInt();
            model.initiative = parcel.readInt();
            model.attacks = parcel.readInt();
            model.leadership = parcel.readInt();
            model.save = parcel.readInt();
            model.invulSave = parcel.readInt();
            model.type = (W40kModelType)parcel.readValue(W40kModelType.class.getClassLoader());
            model.defaultCount = parcel.readInt();
            model.maxCount = parcel.readInt();
            model.frontArmour = parcel.readInt();
            model.sideArmour = parcel.readInt();
            model.rearArmour = parcel.readInt();
            model.hullPoints = parcel.readInt();
            model.cost = parcel.readInt();
            return model;
        }

        @Override
        public W40kModel[] newArray(int i) {
            return new W40kModel[i];
        }
    };

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
