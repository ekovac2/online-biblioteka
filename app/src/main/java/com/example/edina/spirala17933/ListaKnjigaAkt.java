package com.example.edina.spirala17933;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaKnjigaAkt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);


       /* Button povratak = (Button) findViewById(R.id.dPovratak);
        povratak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String s = getIntent().getStringExtra("knj");
        final ArrayList<Knjiga> listaKnjigaPoKategorijama = new ArrayList<Knjiga>();

        for(int k=0; k<KontejnerskaKlasaKnjiga.vratiVelicinuNiza(); k++) {
            if (KontejnerskaKlasaKnjiga.vratiKategorijuKnjige(k).equalsIgnoreCase(s))
            {
                listaKnjigaPoKategorijama.add(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(k));
            }
        }

        final ListView listaKnjiga = (ListView)findViewById(R.id.listaKnjiga);
        final customAdapterListaKnjiga mojAdapter=new customAdapterListaKnjiga(this, listaKnjigaPoKategorijama);
        listaKnjiga.setAdapter(mojAdapter);


        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterView.getChildAt(i).setBackgroundColor(0xFFAABBED);
                if(listaKnjigaPoKategorijama.get(i).isObojena()==false) listaKnjigaPoKategorijama.get(i).setObojena(true);
            }
        });

*/
    }
}
