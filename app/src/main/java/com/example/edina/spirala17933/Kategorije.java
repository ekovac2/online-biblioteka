package com.example.edina.spirala17933;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Edina on 24.03.2018..
 */

public class Kategorije implements Parcelable {
    String naziv;


    public Kategorije(String naziv) {
        this.naziv = naziv;
    }

    public Kategorije(Parcel in) {
       naziv = in.readString();
    }
    public static final  Creator<Kategorije> CREATOR = new Creator<Kategorije>() {
        @Override
        public Kategorije createFromParcel(Parcel parcel) {
            return new Kategorije(parcel);
        }

        @Override
        public Kategorije[] newArray(int i) {
            return new Kategorije[i];
        }
    };


    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(naziv);
    }

    @Override
    public int describeContents() {
        return 0;
    }





}
