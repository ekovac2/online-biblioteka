package com.example.edina.spirala17933;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Edina on 05.04.2018..
 */

public class customAdapterZaListuAutora extends BaseAdapter{
    Context konetkst;
    ArrayList<String> originalnaLista;


    public customAdapterZaListuAutora(Context kontekst, ArrayList<String> nizAutora) {
        this.konetkst=kontekst;
        this.originalnaLista=nizAutora;

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
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflejter = (LayoutInflater) konetkst.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pogled=inflejter.inflate(R.layout.element_liste_autora,null);

        TextView imeAutora=(TextView) pogled.findViewById(R.id.elementListe);
        TextView brojKnjiga=(TextView) pogled.findViewById(R.id.brojKnjiga);

        imeAutora.setText(originalnaLista.get(i).toString());

        int brojac=0;
        for(int j=0;j<KontejnerskaKlasaKnjiga.vratiVelicinuNiza();j++){
            if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(j).isKojaJeKnjiga()){
                if(originalnaLista.get(i).equalsIgnoreCase(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(j).getImeAutora())) brojac++;
            }
            else{
                for(int k=0;k<KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(j).getAutori().size();k++){
                    if(originalnaLista.get(i).equalsIgnoreCase(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(j).getAutori().get(k).getImeiPrezime())) brojac++;
                }
            }
        }

        String s=String.valueOf(brojac);
        brojKnjiga.setText(s);

        return pogled;
    }


}
