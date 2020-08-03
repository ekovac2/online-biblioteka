package com.example.edina.spirala17933;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Edina on 27.03.2018..
 */

public class KontejnerskaKlasaKnjiga {

    public static ArrayList<Knjiga> knjige = new ArrayList();


    public static boolean ima=false;
    public static boolean trebaLSeProsirit=false;

    public KontejnerskaKlasaKnjiga() {

    }

    public static String vratiKategorijuKnjige(int i){
        return knjige.get(i).getKategorijaKnjige().getNaziv();
    }

    public static int vratiVelicinuNiza(){
        return knjige.size();
    }

    public static void ubaciNovuKnjigu(Knjiga k){
        knjige.add(k);
    }

    public static Knjiga vratiKnjiguNaPoziciji(int position){
        return knjige.get(position);
    }

    public static void resetuj(){
        knjige.clear();
    }

}
