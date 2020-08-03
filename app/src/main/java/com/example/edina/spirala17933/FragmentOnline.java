package com.example.edina.spirala17933;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

import static com.example.edina.spirala17933.BazaOpenHelper.AUTORSTVO_ID_AUTORA;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTORSTVO_ID_KNJIGE;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTOR_ID;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTOR_IME;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_AUTOR;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_AUTORSTVO;

/**
 * Created by Edina on 17.05.2018..
 */

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, customResultResiver.Receiver {

    boolean jednaRijec=true;


    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    ArrayList<String> listanova1 = new ArrayList<String>();

    KontejnerskaKlasaKnjiga k = new KontejnerskaKlasaKnjiga();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container,false);
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        postaviSpinnerKategorije();

        final Spinner sRezultat = (Spinner) getView().findViewById(R.id.sRezultat);
        final Spinner sKategorije = (Spinner) getView().findViewById(R.id.sKategorije);
        Button dRun = (Button) getView().findViewById(R.id.dRun);
        final EditText tekstUpit = (EditText)getView().findViewById(R.id.tekstUpit);


        Button dAdd = (Button)getView().findViewById(R.id.dAdd);


        dRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String tekstUkucan = tekstUpit.getText().toString();

                if(tekstUkucan.contains("autor:")){
                    String str="", rijecZaSlanje="";
                    boolean poslijeDvotacke=false;
                    for(int i=0;i<tekstUkucan.length();i++){
                        if(tekstUkucan.charAt(i)!=':' && poslijeDvotacke==false) str+=tekstUkucan.charAt(i);
                        else if(tekstUkucan.charAt(i)==':'){poslijeDvotacke=true; i++;}

                        if(poslijeDvotacke && i<tekstUkucan.length()) rijecZaSlanje+=tekstUkucan.charAt(i);
                    }

                    if(str.equals("autor")) new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone)FragmentOnline.this).execute(rijecZaSlanje);
                }
                else if(tekstUkucan.contains("korisnik:")){
                    String str="", rijecZaSlanje="";
                    boolean poslijeDvotacke=false;
                    for(int i=0;i<tekstUkucan.length();i++){
                        if(tekstUkucan.charAt(i)!=':' && poslijeDvotacke==false) str+=tekstUkucan.charAt(i);
                        else if(tekstUkucan.charAt(i)==':'){poslijeDvotacke=true; i++;}

                        if(poslijeDvotacke && i<tekstUkucan.length()) rijecZaSlanje+=tekstUkucan.charAt(i);
                    }

                   if(str.equals("korisnik")){

                       Intent intent = new Intent(Intent.ACTION_SYNC,null,getActivity(),KnjigePoznanika.class);

                        customResultResiver mReceiver = new customResultResiver(new Handler());
                        mReceiver.setReceiver(FragmentOnline.this);

                        intent.putExtra("id",rijecZaSlanje);
                        intent.putExtra("receiver", mReceiver);

                        mReceiver.send(KnjigePoznanika.STATUS_START,Bundle.EMPTY);
                        getActivity().startService(intent);

                    }
                }
                else if(tekstUkucan.contains(";")){
                    listanova1.clear();
                    ArrayList<String> listaRijeci = new ArrayList<String>();

                        String str="";
                        for(int i=0;i<tekstUkucan.length();i++){
                            if(tekstUkucan.charAt(i)!=';') str+=tekstUkucan.charAt(i);
                            else{
                                listaRijeci.add(str);
                                str="";
                            }
                        }
                            listaRijeci.add(str);
                            str="";

                    for(int i=0;i<listaRijeci.size();i++) new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(listaRijeci.get(i));

                }
                else{
                    listanova1.clear();
                    String str="";
                    for(int i=0;i<tekstUkucan.length();i++)
                        str+=tekstUkucan.charAt(i);

                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(str);
                }
            }
        });


        Button dPovratak = (Button)getView().findViewById(R.id.dPovratak);

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KnjigeFragment dkf= new KnjigeFragment();
                ListeFragment lf=new ListeFragment();
                Bundle argumenti=new Bundle();
                FragmentManager fm = getFragmentManager();
                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit){
                    FrameLayout mjesto3 = (FrameLayout) getActivity().findViewById(R.id.mjestoF3);
                    mjesto3.removeAllViews();
                    mjesto3.setVisibility(View.GONE);
                    FrameLayout mjesto2 = (FrameLayout) getActivity().findViewById(R.id.mjestoF2);
                    mjesto2.setVisibility(View.VISIBLE);
                    FrameLayout mjesto1 = (FrameLayout) getActivity().findViewById(R.id.mjestoF1);
                    mjesto1.setVisibility(View.VISIBLE);
                }
                else fm.beginTransaction().replace(R.id.mjestoF1,lf).commit();
            }
        });


        dAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(sKategorije.getSelectedItem()==null || sRezultat.getSelectedItem()==null)  Toast.makeText(getActivity(),R.string.Podaci,Toast.LENGTH_SHORT).show();
               else{
                   Kategorije kategorija = new Kategorije(sKategorije.getSelectedItem().toString());
                   int t = sRezultat.getSelectedItemPosition();
                   String filtriraneKnjige = sRezultat.getSelectedItem().toString();

                   String s="";
                   if(knjige.get(t).getAutori().size()!=0) s=knjige.get(t).getAutori().get(0).getImeiPrezime();
                   Knjiga kk= knjige.get(t);
                   Knjiga k = new Knjiga(kk.getId(),kk.getNaziv(), kk.getAutori(), kk.getOpis(), kk.getDatumObjavljivanja(), kk.getSlika(), kk.getBrojStranica(), kategorija);


                   for(int j=0;j<k.getAutori().size();j++){
                       boolean tu=false;
                       for(int i=0;i<ListeFragment.mojaListaAutora.size();i++){
                           if(ListeFragment.mojaListaAutora.get(i).equalsIgnoreCase(k.getAutori().get(j).getImeiPrezime())) tu=true;
                       }
                       if(!tu){
                           ListeFragment.mojaListaAutora.add(k.getAutori().get(j).getImeiPrezime());
                           KategorijeAkt.boh.dodajAutora(k.getAutori().get(j).getImeiPrezime());
                       }
                   }


                   boolean ima=false;
                   for(int i=0;i<KontejnerskaKlasaKnjiga.vratiVelicinuNiza();i++){
                       if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv().equalsIgnoreCase(k.getNaziv())) {
                           if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size() == k.getAutori().size()) {
                               int brojac = 0;
                               for (int j = 0; j < KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size(); j++) {
                                   if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(j).getImeiPrezime().equalsIgnoreCase(k.getAutori().get(j).getImeiPrezime()))
                                       brojac++;
                               }
                               if (brojac == KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size())
                                   ima = true;
                           } else {
                               if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).kojaJeKnjiga) {
                                   for (int j = 0; j < k.getAutori().size(); j++) {
                                       if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora().equalsIgnoreCase(k.getAutori().get(j).getImeiPrezime()))
                                           ima = true;
                                   }
                               }

                           }
                       }
                   }


                   if(ima) Toast.makeText(getActivity(), R.string.postoji, Toast.LENGTH_SHORT).show();
                   else{
                       long rez = KategorijeAkt.boh.dodajKnjigu(k);
                       if (rez == -1)
                           Toast.makeText(getActivity(), "Greška", Toast.LENGTH_SHORT).show();
                       KontejnerskaKlasaKnjiga.ubaciNovuKnjigu(k);


                       int indeks = 0;
                       Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                       while(aut.moveToNext()){
                           for(int i=0;i<k.getAutori().size();i++){
                                if(aut.getString(aut.getColumnIndex(AUTOR_IME)).equalsIgnoreCase(k.getAutori().get(i).getImeiPrezime())) {
                                    indeks = aut.getInt(aut.getColumnIndex(AUTOR_ID));

                                    ContentValues cAutorstvo = new ContentValues();
                                    cAutorstvo.put(AUTORSTVO_ID_AUTORA, indeks);
                                    cAutorstvo.put(AUTORSTVO_ID_KNJIGE, rez);

                                    SQLiteDatabase sb = KategorijeAkt.boh.getWritableDatabase();
                                    sb.insert(DATABASE_TABLE_AUTORSTVO,null, cAutorstvo);
                                }
                           }
                       }



                       Toast.makeText(getActivity(),R.string.Uspjesno,Toast.LENGTH_SHORT).show();
                   }

               tekstUpit.setText("");
               sKategorije.setSelection(0);
               sRezultat.setSelection(0);

               }

            }
        });

    }


    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> k) {

        Spinner sRezultat = (Spinner) getActivity().findViewById(R.id.sRezultat);
        knjige=k;

        ArrayList<String> listanova = new ArrayList<String>();
        for(int i=0;i<k.size();i++) listanova.add(k.get(i).getNaziv());

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listanova);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapterZaSpiner);

    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> k) {


        Spinner sRezultat = (Spinner) getActivity().findViewById(R.id.sRezultat);
        knjige=k;

        for(int i=0;i<k.size();i++) listanova1.add(k.get(i).getNaziv());

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listanova1);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRezultat.setAdapter(adapterZaSpiner);

    }

    public void postaviSpinnerKategorije(){

        Spinner sKategorije = (Spinner) getView().findViewById(R.id.sKategorije);

        ArrayList<Kategorije> listaZaSpiner = new ArrayList<Kategorije>();
        listaZaSpiner.clear();
        listaZaSpiner =  getArguments().getParcelableArrayList("Alista");


        ArrayList<String> listanova = new ArrayList<String>();
        for(int i=0;i<listaZaSpiner.size();i++) listanova.add(listaZaSpiner.get(i).getNaziv());

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listanova);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKategorije.setAdapter(adapterZaSpiner);
    }

    @Override
    public void onReceiveResult(int resulCode, Bundle resultData) {
        if(resulCode == KnjigePoznanika.STATUS_FINISH) {
            final ArrayList<Knjiga> lista = resultData.getParcelableArrayList("listaKnjiga");

            knjige=lista;
            Spinner sRezultat = (Spinner) getActivity().findViewById(R.id.sRezultat);

            ArrayList<String> listanova = new ArrayList<String>();
            for (int i = 0; i < lista.size(); i++) listanova.add(lista.get(i).getNaziv());

            ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listanova);
            adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sRezultat.setAdapter(adapterZaSpiner);
        }
    }
}
