package com.example.wh40k;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Urgak_000 on 21.03.2015.
 */
public class W40kModelType implements Parcelable {

    private BasicType type;
    private List<TypeModifiers> modifiers;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(type);
       // parcel.writeList(modifiers);
    }

    public BasicType getBasicType() {
        return type;
    }

    public List<TypeModifiers> getModifiers() {
        return modifiers;
    }

    W40kModelType(final String str) {
        String lowerStr = str.toLowerCase();
        type = BasicType.INFANTRY;
        if(lowerStr.contains("vehicle")) {
            type=BasicType.VEHICLE;
        }
        modifiers = new ArrayList<TypeModifiers>();
    }

    public enum BasicType {
        INFANTRY,
        JUMP_INFANTRY,
        JETPACK_INFANTRY,
        BIKE,
        JETBIKE,
        BEAST,
        CALVARY,
        ARTILLERY,
        MONSTROUS_CREATURE,
        FLYING_MONSTROUS_CREATURE,
        VEHICLE,
        SUPER_HEAVY_VEHICLE,
    }

    public enum TypeModifiers {
        //non-vehicle modifiers
        CHARACTER,
        //vehicle modifiers
        FAST,
        SKIMMER,
        TANK,
        OPEN_TOPPED,
        TRANSPORT,
        WALKER,
        FLYER,
        HOVER,
    }

    public static final Parcelable.Creator<W40kModelType> CREATOR = new Parcelable.Creator<W40kModelType>(){
        @Override
        public W40kModelType createFromParcel(Parcel parcel) {
            W40kModelType type = new W40kModelType("");
            type.type = (BasicType)parcel.readSerializable();
            //parcel.readList(type.modifiers, TypeModifiers.class.getClassLoader());
            return type;
        }

        @Override
        public W40kModelType[] newArray(int i) {
            return new W40kModelType[i];
        }
    };
}
