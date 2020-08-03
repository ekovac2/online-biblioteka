package com.example.edina.spirala17933;

import android.content.Context;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.FilterReader;
import java.util.ArrayList;

/**
 * Created by Edina on 24.03.2018..
 */

public class customAdapter extends BaseAdapter implements Filterable{

    Context konetkst;
    ArrayList<String> originalnaLista, pomocnaLista;
    customFilter filterPom;

    public customAdapter(Context kontekst, ArrayList<String> nizKategorija) {
        this.konetkst=kontekst;
        this.originalnaLista=nizKategorija;
        this.pomocnaLista=nizKategorija;
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
        View pogled=inflejter.inflate(R.layout.element_liste_kategorije,null);

        TextView tekst = (TextView) pogled.findViewById(R.id.elementListe);
        tekst.setText(originalnaLista.get(i).toString());
        return pogled;
    }

    @Override
    public Filter getFilter() {
        if(filterPom==null){
            filterPom=new customFilter();
        }
        return filterPom;
    }

    class customFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults rezultati= new FilterResults();

            if(charSequence!=null && charSequence.length()>0){
                charSequence=charSequence.toString().toLowerCase();

            ArrayList<String> filteri = new ArrayList<String>();

            for(int i=0;i<pomocnaLista.size();i++){

                if(pomocnaLista.get(i).toString().toLowerCase().contains(charSequence)){
                    String k= pomocnaLista.get(i).toString();
                    filteri.add(k);
                    }
                }

                rezultati.count=filteri.size();
                rezultati.values=filteri;
            }
            else{

                rezultati.count=pomocnaLista.size();
                rezultati.values=pomocnaLista;
            }

            return rezultati;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                originalnaLista=(ArrayList<String>)filterResults.values;
                notifyDataSetChanged();
        }
    }

}
