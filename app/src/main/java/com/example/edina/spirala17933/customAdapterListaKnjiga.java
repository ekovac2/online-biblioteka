package com.example.edina.spirala17933;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Edina on 26.03.2018..
 */

public class customAdapterListaKnjiga extends BaseAdapter{

    Context konetkst;
    ArrayList<Knjiga> originalnaLista;



    public customAdapterListaKnjiga(Context kontekst, ArrayList<Knjiga> nizKnjiga) {
        this.konetkst=kontekst;
        this.originalnaLista=nizKnjiga;
    }

    @Override
    public int getCount() {
        return originalnaLista.size();
    }

    @Override
    public Object getItem(int i) {
        return originalnaLista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {

        LayoutInflater inflejter = (LayoutInflater) konetkst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View pogled=inflejter.inflate(R.layout.element_lista_knjiga,null);

        TextView imeAutora=(TextView) pogled.findViewById(R.id.eAutor);
        TextView nazivKnjige=(TextView) pogled.findViewById(R.id.eNaziv);
        ImageView naslovnaStrana=(ImageView) pogled.findViewById(R.id.eNaslovna);
        TextView datumObjavljivanja=(TextView) pogled.findViewById(R.id.eDatumObjavljivanja);
        TextView brojStranica=(TextView) pogled.findViewById(R.id.eBrojStranica);
        TextView opis=(TextView) pogled.findViewById(R.id.eOpis);

        Button dPreporuci = (Button) pogled.findViewById(R.id.dPreporuci);

        imeAutora.setText("Nepoznat autor!");
        nazivKnjige.setText("Nepoznata knjiga!");
        datumObjavljivanja.setText("");
        brojStranica.setText("");
        opis.setText("");






        final int p = i;
        dPreporuci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentPreporuci dkf= new FragmentPreporuci();
                FragmentManager fm =((Activity)konetkst).getFragmentManager();

                Bundle argumenti=new Bundle();


                argumenti.putParcelableArrayList("lista",originalnaLista);;
                argumenti.putInt("knjiga", p);
                dkf.setArguments(argumenti);
                /*if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==true){
                    FrameLayout mjesto2 = (FrameLayout) pogled.findViewById(R.id.mjestoF2);
                    mjesto2.setVisibility(View.VISIBLE);
                    FrameLayout mjesto1 = (FrameLayout) pogled.findViewById(R.id.mjestoF1);
                    mjesto1.setVisibility(View.VISIBLE);
                    FrameLayout mjesto3 = (FrameLayout) pogled.findViewById(R.id.mjestoF3);
                    mjesto3.setVisibility(View.GONE);
                    mjesto3.removeAllViews();

                }*/


                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==false) fm.beginTransaction().replace(R.id.mjestoF1,dkf).commit();
                else{
                    KategorijeAkt.mjestoo2.setVisibility(View.GONE);
                    KategorijeAkt.mjestoo1.setVisibility(View.GONE);
                    KategorijeAkt.mjestoo3.setVisibility(View.VISIBLE);
                    fm.beginTransaction().replace(R.id.mjestoF3,dkf).commit();
                }
            }
        });


        if(originalnaLista.get(i).isKojaJeKnjiga()) imeAutora.setText(originalnaLista.get(i).getImeAutora().toString());
        else {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < originalnaLista.get(i).getAutori().size(); k++) {
                if (k != originalnaLista.get(i).getAutori().size() - 1)
                    sb.append(originalnaLista.get(i).getAutori().get(k).getImeiPrezime() + ", ");
                else sb.append(originalnaLista.get(i).getAutori().get(k).getImeiPrezime());
            }
            imeAutora.setText(sb.toString());


            datumObjavljivanja.setText(originalnaLista.get(i).getDatumObjavljivanja());
            String s=String.valueOf(originalnaLista.get(i).getBrojStranica());
            brojStranica.setText(s);
            opis.setText(originalnaLista.get(i).getOpis());

        }


        nazivKnjige.setText(originalnaLista.get(i).getNaziv().toString());

        if(originalnaLista.get(i).kojaJeKnjiga == false) {
            URL slika = originalnaLista.get(i).getSlika();
            Picasso.with(konetkst).load(slika.toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(naslovnaStrana, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        else naslovnaStrana.setImageBitmap(originalnaLista.get(i).getNaslovnaStrana());




        if(originalnaLista.get(i).isObojena()==true) pogled.setBackgroundResource(R.color.Plava);

        return pogled;
    }



}
