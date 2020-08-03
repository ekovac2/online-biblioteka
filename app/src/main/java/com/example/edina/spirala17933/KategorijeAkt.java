package com.example.edina.spirala17933;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity {
    //String s;
    //public static ArrayList<String> mojaListaKategorija;
    //KontejnerskaKlasaKnjiga mojaListaKnjiga = new KontejnerskaKlasaKnjiga();

    public static BazaOpenHelper boh;
    boolean siliL = false;

    public static FrameLayout mjestoo2;
    public static FrameLayout mjestoo1;
    public static FrameLayout mjestoo3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

         boh = new BazaOpenHelper(this);

       SQLiteDatabase sql = boh.getWritableDatabase();
       //boh.onUpgrade(sql,0,0);

        try {
            KategorijeAkt.boh.ucitajKnjige();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_kategorije_akt);

        FrameLayout ldetalji = (FrameLayout)findViewById(R.id.mjestoF2);
        FragmentManager fm = getFragmentManager();

        mjestoo1 = (FrameLayout)findViewById(R.id.mjestoF1);
        mjestoo2 = ldetalji;
        FrameLayout mjesto3 = (FrameLayout)findViewById(R.id.mjestoF3);
        mjestoo3=mjesto3;

       if(ldetalji!=null) {
            KontejnerskaKlasaKnjiga.trebaLSeProsirit=true;
            FragmentTransaction ft = fm.beginTransaction();

           mjesto3.setVisibility(View.INVISIBLE);

            KnjigeFragment lf = new KnjigeFragment();
            ft.replace(R.id.mjestoF2, lf);

           ListeFragment fl = new ListeFragment();

           ft.replace(R.id.mjestoF1, fl, "Lista");
           ft.commit();
        }

        else{
        KontejnerskaKlasaKnjiga.trebaLSeProsirit=false;
       ListeFragment fl = (ListeFragment) fm.findFragmentByTag("Lista");

        if (fl == null) {
            fl = new ListeFragment();
            fm.beginTransaction().replace(R.id.mjestoF1, fl, "Lista").commit();
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
       }
    }
}




