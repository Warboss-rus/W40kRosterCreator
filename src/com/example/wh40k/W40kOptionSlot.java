package com.example.wh40k;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Urgak_000 on 21.03.2015.
 */
public class W40kOptionSlot implements Parcelable {
    private List<W40kOption> options = new ArrayList<W40kOption>();
    private Integer max;
    private Boolean onePerModel;
    private Integer upgradesPerModels;
    private W40kModel model = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(max);
        parcel.writeInt(onePerModel?1:0);
        parcel.writeInt(upgradesPerModels);
        parcel.writeList(options);
    }

    public final List<W40kOption> getOptions() {
        return options;
    }

    public void addOption(W40kOption option) {
        options.add(option);
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Boolean getOnePerModel() {
        return onePerModel;
    }

    public void setOnePerModel(Boolean onePerModel) {
        this.onePerModel = onePerModel;
    }

    public W40kModel getModel() { return model; }

    public void setModel(W40kModel model) { this.model = model; }

    public Integer getUpgradesPerModels() {
        return upgradesPerModels;
    }

    public void setUpgradesPerModels(Integer upgradesPerModels) {
        this.upgradesPerModels = upgradesPerModels;
    }

    public static final Parcelable.Creator<W40kOptionSlot> CREATOR = new Parcelable.Creator<W40kOptionSlot>(){
        @Override
        public W40kOptionSlot createFromParcel(Parcel parcel) {
            W40kOptionSlot slot = new W40kOptionSlot();
            slot.max = parcel.readInt();
            slot.onePerModel = (parcel.readInt() == 1);
            slot.upgradesPerModels = parcel.readInt();
            parcel.readList(slot.options, W40kOption.class.getClassLoader());
            return slot;
        }

        @Override
        public W40kOptionSlot[] newArray(int i) {
            return new W40kOptionSlot[i];
        }
    };
}
