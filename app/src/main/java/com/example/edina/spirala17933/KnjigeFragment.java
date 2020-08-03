package com.example.edina.spirala17933;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Edina on 05.04.2018..
 */

public class KnjigeFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button povratak = (Button) getView().findViewById(R.id.dPovratak);
        povratak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                KnjigeFragment dkf= new KnjigeFragment();
                ListeFragment lf=new ListeFragment();
                Bundle argumenti=new Bundle();
                FragmentManager fm = getFragmentManager();
                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit) fm.beginTransaction().replace(R.id.mjestoF2,dkf).commit();
                else fm.beginTransaction().replace(R.id.mjestoF1,lf).commit();

            }
        });

        if(KontejnerskaKlasaKnjiga.ima==true) {
            KontejnerskaKlasaKnjiga.ima=false;
            ArrayList<String> KiliA = getArguments().getStringArrayList("lista");
            String s = KiliA.get(1);
            final ArrayList<Knjiga> listaKnjigaPoKiliA = new ArrayList<Knjiga>();

            if (KiliA.get(0).equalsIgnoreCase("kategorija")) {

                int indeks=0;
                Cursor kat = KategorijeAkt.boh.getAllData(KategorijeAkt.boh.DATABASE_TABLE_KATEGORIJA);
                while(kat.moveToNext()) {
                    String naziv = kat.getString(kat.getColumnIndex(KategorijeAkt.boh.KATEGORIJA_NAZIV));
                    if(naziv.equalsIgnoreCase(s)) indeks = kat.getInt(kat.getColumnIndex(KategorijeAkt.boh.KATEGORIJA_ID));
                }
                try {
                    ArrayList<Knjiga> knj = KategorijeAkt.boh.knjigeKategorije(indeks);

                    for(int i=0;i<knj.size();i++) listaKnjigaPoKiliA.add(knj.get(i));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

               /* for (int k = 0; k < KontejnerskaKlasaKnjiga.vratiVelicinuNiza(); k++) {
                    if (KontejnerskaKlasaKnjiga.vratiKategorijuKnjige(k).equalsIgnoreCase(s)) {
                        listaKnjigaPoKiliA.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k));
                    }
                }*/
            }
            else {
                int indeks=0;
                Cursor kat = KategorijeAkt.boh.getAllData(KategorijeAkt.boh.DATABASE_TABLE_AUTOR);
                while(kat.moveToNext()) {
                    String naziv = kat.getString(kat.getColumnIndex(KategorijeAkt.boh.AUTOR_IME));
                    if(naziv.equalsIgnoreCase(s)) indeks = kat.getInt(kat.getColumnIndex(KategorijeAkt.boh.AUTOR_ID));
                }

                try {
                    ArrayList<Knjiga> knj = KategorijeAkt.boh.knjigeAutora(indeks);

                    for(int i=0;i<knj.size();i++) listaKnjigaPoKiliA.add(knj.get(i));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                /*
                for (int k = 0; k < KontejnerskaKlasaKnjiga.vratiVelicinuNiza(); k++) {
                    if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k).kojaJeKnjiga){
                        if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k).getImeAutora().equalsIgnoreCase(s)) {
                            listaKnjigaPoKiliA.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k));
                        }
                    }
                    else{
                        for(int p=0;p<KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k).getAutori().size();p++)
                            if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k).getAutori().get(p).getImeiPrezime().equalsIgnoreCase(s))
                                listaKnjigaPoKiliA.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k));
                    }

                }*/
            }

            final ListView listaKnjiga = (ListView) getView().findViewById(R.id.listaKnjiga);
            final customAdapterListaKnjiga mojAdapter = new customAdapterListaKnjiga(getActivity(), listaKnjigaPoKiliA);
            listaKnjiga.setAdapter(mojAdapter);


            listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapterView.getChildAt(i-adapterView.getFirstVisiblePosition()).setBackgroundResource(R.color.Plava);

                    int indeks=0;
                    Cursor cr = KategorijeAkt.boh.getAllData(KategorijeAkt.boh.DATABASE_TABLE_KNJIGA);
                    while(cr.moveToNext()){
                        if(cr.getString(cr.getColumnIndex(KategorijeAkt.boh.KNJIGA_NAZIV)).equalsIgnoreCase(listaKnjigaPoKiliA.get(i).getNaziv())){
                            indeks=cr.getInt(cr.getColumnIndex(KategorijeAkt.boh.KNJIGA_ID));

                        }
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(KategorijeAkt.boh.KNJIGA_PREGLEDANA, 1);

                    SQLiteDatabase db = KategorijeAkt.boh.getWritableDatabase();
                    db.update(KategorijeAkt.boh.DATABASE_TABLE_KNJIGA, cv, "_id="+indeks, null);

                    if (listaKnjigaPoKiliA.get(i).isObojena() == false)
                        listaKnjigaPoKiliA.get(i).setObojena(true);
                }
            });

        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.knjige_fragment, container,false);
    }

}
