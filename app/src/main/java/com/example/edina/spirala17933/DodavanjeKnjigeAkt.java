package com.example.edina.spirala17933;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    //Bitmap pomocnaSlika = null;
    //ImageView naslovnaStrana;
    //int zahtjev=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);


        /*naslovnaStrana = (ImageView) findViewById(R.id.naslovnaStr);
        final EditText imeAutora = (EditText) findViewById(R.id.imeAutora);
        final EditText nazivKnjige = (EditText) findViewById(R.id.nazivKnjige);
        Button nadjiSliku = (Button) findViewById(R.id.dNadjiSliku);
        Button upisiKnjigu = (Button) findViewById(R.id.dUpisiKnjigu);
        final Spinner kategorijaKnjige = (Spinner)findViewById(R.id.sKategorijaKnjige);
        Button ponisti = (Button) findViewById(R.id.dPonisti);


        ArrayList<String> lista1 =  (ArrayList<String>)getIntent().getSerializableExtra("lista");
        ArrayList<String> listaZaSpiner=new ArrayList<String>();

        for(int i=0;i< lista1.size();i++){
            listaZaSpiner.add(lista1.get(i).toString());
        }

        naslovnaStrana.setImageDrawable(null);

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaZaSpiner);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorijaKnjige.setAdapter(adapterZaSpiner);


        upisiKnjigu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Kategorije k=new Kategorije(kategorijaKnjige.getSelectedItem().toString());
                Knjiga knjiga=new Knjiga(pomocnaSlika, imeAutora.getText().toString(), nazivKnjige.getText().toString(),k);

                KontejnerskaKlasaKnjiga.ubaciNovuKnjigu(knjiga);
                imeAutora.setText("");
                nazivKnjige.setText("");
                naslovnaStrana.setImageDrawable(null);
            }
        });

        ponisti.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
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
*/
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==zahtjev && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Uri uri = data.getData();
            InputStream strim= null;
            try{
                strim=getContentResolver().openInputStream(uri);
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
    }*/
}
