package com.example.edina.spirala17933;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTORSTVO_ID_AUTORA;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTORSTVO_ID_KNJIGE;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTOR_ID;
import static com.example.edina.spirala17933.BazaOpenHelper.AUTOR_IME;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_AUTOR;
import static com.example.edina.spirala17933.BazaOpenHelper.DATABASE_TABLE_AUTORSTVO;
import static com.example.edina.spirala17933.BazaOpenHelper.KATEGORIJA_ID;

/**
 * Created by Edina on 04.04.2018..
 */

public class DodavanjeKnjigeFragment extends Fragment {

    Bitmap pomocnaSlika = null;
    ImageView naslovnaStrana;
    int zahtjev=1;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dodavanje_knjige_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        naslovnaStrana = (ImageView) getView().findViewById(R.id.naslovnaStr);
        final EditText imeAutora = (EditText) getView().findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText) getView().findViewById(R.id.nazivKnjige);
        Button nadjiSliku = (Button) getView().findViewById(R.id.dNadjiSliku);
        Button upisiKnjigu = (Button) getView().findViewById(R.id.dUpisiKnjigu);
        final Spinner kategorijaKnjige = (Spinner)getView().findViewById(R.id.sKategorijaKnjige);
        Button ponisti = (Button) getView().findViewById(R.id.dPonisti);

        ArrayList<Kategorije> listaZaSpiner = new ArrayList<Kategorije>();

        listaZaSpiner =  getArguments().getParcelableArrayList("Alista");


        ArrayList<String> listanova = new ArrayList<String>();
        for(int i=0;i<listaZaSpiner.size();i++) listanova.add(listaZaSpiner.get(i).getNaziv());

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listanova);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorijaKnjige.setAdapter(adapterZaSpiner);


        upisiKnjigu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(kategorijaKnjige.getSelectedItem()==null || imeAutora.getText().toString().isEmpty() || nazivKnjige.getText().toString().isEmpty()) Toast.makeText(getActivity(),R.string.Podaci,Toast.LENGTH_SHORT).show();
                else{
                Kategorije k=new Kategorije(kategorijaKnjige.getSelectedItem().toString());
                Knjiga knjiga=new Knjiga(pomocnaSlika, imeAutora.getText().toString(), nazivKnjige.getText().toString(),k);


               boolean ima=false;
                for(int i=0;i<KontejnerskaKlasaKnjiga.vratiVelicinuNiza();i++){
                    if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).kojaJeKnjiga) {
                        if (KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv().equalsIgnoreCase(knjiga.getNaziv()) && KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora().equalsIgnoreCase(knjiga.getImeAutora()))
                            ima = true;
                    }
                    else{
                        if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv().equalsIgnoreCase(knjiga.getNaziv()) && KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size()==1 && KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(0).getImeiPrezime().equalsIgnoreCase(knjiga.getImeAutora())) ima=true;

                    }
                }


                if(!ima) {
                    long rez = KategorijeAkt.boh.dodajKnjigu(knjiga);
                    if (rez == -1)
                        Toast.makeText(getActivity(), "Greška", Toast.LENGTH_SHORT).show();

                    KontejnerskaKlasaKnjiga.ubaciNovuKnjigu(knjiga);

                    int indeks = 0;
                    Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                    while(aut.moveToNext()){
                        if(aut.getString(aut.getColumnIndex(AUTOR_IME)).equalsIgnoreCase(knjiga.getImeAutora())){
                            indeks = aut.getInt(aut.getColumnIndex(AUTOR_ID));
                            ContentValues cAutorstvo = new ContentValues();
                            cAutorstvo.put(AUTORSTVO_ID_AUTORA, indeks);
                            cAutorstvo.put(AUTORSTVO_ID_KNJIGE, rez);

                            SQLiteDatabase sb = KategorijeAkt.boh.getWritableDatabase();
                            sb.insert(DATABASE_TABLE_AUTORSTVO,null, cAutorstvo);
                        }
                    }




                }
                else Toast.makeText(getActivity(), R.string.postoji, Toast.LENGTH_SHORT).show();


                    imeAutora.setText("");
                    nazivKnjige.setText("");
                    naslovnaStrana.setImageDrawable(null);
                }


            }
        });

        ponisti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ListeFragment dkf= new ListeFragment();
                Bundle argumenti=new Bundle();
                FragmentManager fm = getFragmentManager();

                if(KontejnerskaKlasaKnjiga.trebaLSeProsirit==true){
                    FrameLayout mjesto2 = (FrameLayout) getActivity().findViewById(R.id.mjestoF2);
                    mjesto2.setVisibility(View.VISIBLE);
                    FrameLayout mjesto1 = (FrameLayout) getActivity().findViewById(R.id.mjestoF1);
                    mjesto1.setVisibility(View.VISIBLE);
                    FrameLayout mjesto3 = (FrameLayout) getActivity().findViewById(R.id.mjestoF3);
                    mjesto3.setVisibility(View.GONE);
                    mjesto3.removeAllViews();

                }
                fm.beginTransaction().replace(R.id.mjestoF1,dkf).commit();

            }
        });

        nadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(i.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Izbor slike"), zahtjev);
            }
        });


    }


   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==zahtjev && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri = data.getData();
            InputStream strim= null;
            try{
                strim=getActivity().getApplicationContext().getContentResolver().openInputStream(uri);
                Bitmap slika = null;
                slika = BitmapFactory.decodeStream(strim);
                pomocnaSlika=slika;
                naslovnaStrana.setImageBitmap(slika);
            }
            catch(FileNotFoundException exc){
            }
            finally {
                if(strim!=null){
                    try{
                        strim.close();
                    }
                    catch(IOException ex){}
                }
            }
        }
    }

}
