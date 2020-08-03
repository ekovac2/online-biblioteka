package com.example.edina.spirala17933;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.example.edina.spirala17933.BazaOpenHelper.AUTOR_IME;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_NAME;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_AUTOR;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_KATEGORIJA;
import static com.example.edina.spirala17933.BazaOpenHelper.KATEGORIJA_ID;
import static com.example.edina.spirala17933.BazaOpenHelper.KATEGORIJA_NAZIV;

/**
 * Created by Edina on 04.04.2018..
 */

public class ListeFragment extends Fragment {


    String s;
    public static ArrayList<String> mojaListaKategorija = new ArrayList<String>();
    public static ArrayList<String> mojaListaAutora = new ArrayList<String>();
    KontejnerskaKlasaKnjiga mojaListaKnjiga = new KontejnerskaKlasaKnjiga();



    boolean kategorija=false;
    boolean autor=false;

    public static ArrayList<Kategorije> kat= new ArrayList<Kategorije>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        final Button dKategorije = (Button) getView().findViewById(R.id.dKategorije);
        final Button dAutori = (Button) getView().findViewById(R.id.dAutori);
        final Button dPretraga = (Button)getView().findViewById(R.id.dPretraga);
        final Button dDodajKategoriju = (Button)getView().findViewById(R.id.dDodajKategoriju);
        Button dDodajKnjigu = (Button)getView().findViewById(R.id.dDodajKnjigu);
        final EditText tekstPretraga = (EditText)getView().findViewById(R.id.tekstPretraga);
        final ListView listaKategorija = (ListView)getView().findViewById(R.id.listaKategorija);

        Button dDodajOnline = (Button)getView().findViewById(R.id.dDodajOnline​);

        tekstPretraga.setHint(R.string.tekstPretraga);


        dDodajKategoriju.setEnabled(false);


        Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
        while(cr.moveToNext()){
            mojaListaKategorija.add(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)));
            kat.add(new Kategorije(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV))));
        }



        /*for(int i=0;i<KontejnerskaKlasaKnjiga.vratiVelicinuNiza();i++){
            if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).isKojaJeKnjiga()){
            boolean ima=false;
            for(int j=0;j<mojaListaAutora.size();j++){
                if(mojaListaAutora.get(j).equalsIgnoreCase(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora())) ima=true;
            }
            if(!ima) mojaListaAutora.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora());}
            else{
                boolean ima=false;
                for(int k=0;k<KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size();k++){
                for(int j=0;j<mojaListaAutora.size();j++)
                    if(mojaListaAutora.get(j).equalsIgnoreCase(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(k).getImeiPrezime())) ima=true;

                 if(!ima) mojaListaAutora.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(k).getImeiPrezime());
                }
            }
        }*/


        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mojaListaAutora.clear();
                Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                while(aut.moveToNext()){
                    mojaListaAutora.add(aut.getString(aut.getColumnIndex(AUTOR_IME)));
                }
                final customAdapterZaListuAutora mojAdapter = new customAdapterZaListuAutora(getActivity(), mojaListaAutora);
                listaKategorija.setAdapter(mojAdapter);

                autor=true;
                kategorija=false;
                dPretraga.setVisibility(View.GONE);
                dDodajKategoriju.setVisibility(View.GONE);
                tekstPretraga.setVisibility(View.GONE);

            }
        });


        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kat.clear();
                mojaListaKategorija.clear();
                Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                while(cr.moveToNext()){
                    mojaListaKategorija.add(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)));
                    kat.add(new Kategorije(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV))));
                }
                final customAdapter mojAdapter=new customAdapter(getActivity(), mojaListaKategorija);
                listaKategorija.setAdapter(mojAdapter);

                kategorija=true;
                autor=false;

                dPretraga.setVisibility(View.VISIBLE);
                dDodajKategoriju.setVisibility(View.VISIBLE);
                tekstPretraga.setVisibility(View.VISIBLE);

                dPretraga.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        mojAdapter.getFilter().filter(tekstPretraga.getText());

                        boolean ima=false;
                        for(int i=0;i<mojaListaKategorija.size();i++){
                            if(mojaListaKategorija.get(i).toUpperCase().contains(tekstPretraga.getText().toString().toUpperCase())) ima=true;
                        }

                        if(ima==false)  dDodajKategoriju.setEnabled(true);
                        else dDodajKategoriju.setEnabled(false);

                    }
                });


            }
        });

        dPretraga.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mojaListaKategorija.clear();
                kat.clear();
                Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                while(cr.moveToNext()){
                    mojaListaKategorija.add(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)));
                    kat.add(new Kategorije(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV))));
                }
                final customAdapter mojAdapter=new customAdapter(getActivity(), mojaListaKategorija);
                listaKategorija.setAdapter(mojAdapter);
                mojAdapter.getFilter().filter(tekstPretraga.getText());

                boolean ima=false;
                for(int i=0;i<mojaListaKategorija.size();i++){
                    if(mojaListaKategorija.get(i).toUpperCase().contains(tekstPretraga.getText().toString().toUpperCase())) ima=true;
                }

                if(ima==false)  dDodajKategoriju.setEnabled(true);
                else dDodajKategoriju.setEnabled(false);

            }
        });



        dDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentOnline dkf= new FragmentOnline();
                FragmentManager fm = getFragmentManager();

                Bundle argumenti=new Bundle();

                kat.clear();
                Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                while(cr.moveToNext()){
                    //mojaListaKategorija.add(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)));
                    kat.add(new Kategorije(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV))));
                }

                argumenti.putParcelableArrayList("Alista",kat);
                dkf.setArguments(argumenti);


                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==false) fm.beginTransaction().replace(R.id.mjestoF1,dkf).commit();
                else{
                    FrameLayout mjesto2 = (FrameLayout) getActivity().findViewById(R.id.mjestoF2);
                    mjesto2.setVisibility(View.GONE);
                    FrameLayout mjesto1 = (FrameLayout) getActivity().findViewById(R.id.mjestoF1);
                    mjesto1.setVisibility(View.GONE);
                    FrameLayout mjesto3 = (FrameLayout) getActivity().findViewById(R.id.mjestoF3);
                    mjesto3.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.mjestoF3,dkf).commit();
                }
            }
        });


        dDodajKnjigu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                DodavanjeKnjigeFragment dkf= new DodavanjeKnjigeFragment();
                Bundle argumenti=new Bundle();

                kat.clear();
                Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                while(cr.moveToNext()){
                    kat.add(new Kategorije(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV))));
                }
                argumenti.putParcelableArrayList("Alista",kat);
                dkf.setArguments(argumenti);

                FragmentManager fm = getFragmentManager();

                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==false) fm.beginTransaction().replace(R.id.mjestoF1,dkf).commit();
                else{
                    FrameLayout mjesto2 = (FrameLayout) getActivity().findViewById(R.id.mjestoF2);
                    mjesto2.setVisibility(View.GONE);
                    FrameLayout mjesto1 = (FrameLayout) getActivity().findViewById(R.id.mjestoF1);
                    mjesto1.setVisibility(View.GONE);
                    FrameLayout mjesto3 = (FrameLayout) getActivity().findViewById(R.id.mjestoF3);
                    mjesto3.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.mjestoF3,dkf).commit();
                }
            }
        });




        dDodajKategoriju.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String k=tekstPretraga.getText().toString();

                if (k != null && !k.isEmpty()) {
                        Kategorije kateg = new Kategorije(k);
                        kat.add(kateg);
                        boolean ima=false;
                        Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                        while(cr.moveToNext()){
                            if(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)).equalsIgnoreCase(k)) ima=true;
                        }
                        if(!ima){
                            mojaListaKategorija.add(k);
                            long rez = KategorijeAkt.boh.dodajKategoriju(k);
                            if (rez == -1)
                                Toast.makeText(getActivity(), "Greška", Toast.LENGTH_SHORT).show();
                    }

                tekstPretraga.setText("");
                dDodajKategoriju.setEnabled(false);

                dPretraga.performClick();
                }
            }
        });


        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                KontejnerskaKlasaKnjiga.ima=true;
                s = listaKategorija.getItemAtPosition(i).toString();
                ArrayList<String> listaStringova = new ArrayList<String>();
                if(kategorija) listaStringova.add("kategorija");
                else listaStringova.add("autor");
                listaStringova.add(s);

                KnjigeFragment dkf= new KnjigeFragment();
                Bundle argumenti=new Bundle();

                argumenti.putStringArrayList("lista", listaStringova);
                dkf.setArguments(argumenti);

                FragmentManager fm = getFragmentManager();

                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==true)
                fm.beginTransaction().replace(R.id.mjestoF2,dkf).commit();
                else fm.beginTransaction().replace(R.id.mjestoF1,dkf).commit();
            }
        });



    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.liste_fragment, container,false);
    }

}
