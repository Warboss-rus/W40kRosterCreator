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
        parcel.writeList(modifiers);
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
        if(lowerStr.contains("jump infantry")) {
            type=BasicType.JUMP_INFANTRY;
        }
        if(lowerStr.contains("jetpack")) {
            type=BasicType.JETPACK_INFANTRY;
        }
        if(lowerStr.contains("bike")) {
            type=BasicType.BIKE;
        }
        if(lowerStr.contains("jetbike")) {
            type=BasicType.JETBIKE;
        }
        if(lowerStr.contains("beast")) {
            type=BasicType.BEAST;
        }
        if(lowerStr.contains("calvary")) {
            type=BasicType.CALVARY;
        }
        if(lowerStr.contains("artillery")) {
            type=BasicType.ARTILLERY;
        }
        if(lowerStr.contains("flying monstrous creature")) {
            type=BasicType.FLYING_MONSTROUS_CREATURE;
        } else if(lowerStr.contains("monstrous creature")) {
            type=BasicType.MONSTROUS_CREATURE;
        }
        if(lowerStr.contains("super-heavy")) {
            type=BasicType.SUPER_HEAVY_VEHICLE;
        }
        modifiers = new ArrayList<TypeModifiers>();
        if(lowerStr.contains("walker")) {
            modifiers.add(TypeModifiers.WALKER);
        }
        if(lowerStr.contains("character")) {
            modifiers.add(TypeModifiers.CHARACTER);
        }
        if(lowerStr.contains("fast")) {
            modifiers.add(TypeModifiers.FAST);
        }
        if(lowerStr.contains("skimmer")) {
            modifiers.add(TypeModifiers.SKIMMER);
        }
        if(lowerStr.contains("tank")) {
            modifiers.add(TypeModifiers.TANK);
        }
        if(lowerStr.contains("open-topped")) {
            modifiers.add(TypeModifiers.OPEN_TOPPED);
        }
        if(lowerStr.contains("transport")) {
            modifiers.add(TypeModifiers.TRANSPORT);
        }
        if(lowerStr.contains("flyer")) {
            modifiers.add(TypeModifiers.FLYER);
        }
        if(lowerStr.contains("hover")) {
            modifiers.add(TypeModifiers.HOVER);
        }
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

    public String toString() {
        String result = type.toString() + " (";
        for(TypeModifiers modifier : modifiers) {
            result += modifier.toString() + ", ";
        }
        result = result.substring(0, result.length() - 2);
        if(modifiers.size() > 0) result += ")";
        return result;
    }

    public static final Parcelable.Creator<W40kModelType> CREATOR = new Parcelable.Creator<W40kModelType>(){
        @Override
        public W40kModelType createFromParcel(Parcel parcel) {
            W40kModelType type = new W40kModelType("");
            type.type = (BasicType)parcel.readSerializable();
            parcel.readList(type.modifiers, TypeModifiers.class.getClassLoader());
            return type;
        }

        @Override
        public W40kModelType[] newArray(int i) {
            return new W40kModelType[i];
        }
    };
}
