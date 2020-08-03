package com.example.edina.spirala17933;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Edina on 23.05.2018..
 */

public class FragmentPreporuci extends Fragment{

    ArrayList<String> imejl = new ArrayList<String>();
    ArrayList<String> imena = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preporuci, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView imeAutora=(TextView) getView().findViewById(R.id.eAutor);
        TextView nazivKnjige=(TextView) getView().findViewById(R.id.eNaziv);
        ImageView naslovnaStrana=(ImageView) getView().findViewById(R.id.eNaslovna);
        TextView datumObjavljivanja=(TextView) getView().findViewById(R.id.eDatumObjavljivanja);
        TextView brojStranica=(TextView) getView().findViewById(R.id.eBrojStranica);
        TextView opis=(TextView) getView().findViewById(R.id.eOpis);
        final Spinner sKontakti = (Spinner)getView().findViewById(R.id.sKontakti);

        imeAutora.setText("Nepoznat autor!");
        nazivKnjige.setText("Nepoznata knjiga!");
        datumObjavljivanja.setText("");
        brojStranica.setText("");
        opis.setText("");

        ispuniSpinner();
        Button dPosalji = (Button) getView().findViewById(R.id.dPosalji);

        final int kljuc = getArguments().getInt("knjiga");
        ArrayList<Knjiga> books = getArguments().getParcelableArrayList("lista");

        int p=0;
        for(int m=0;m<KontejnerskaKlasaKnjiga.vratiVelicinuNiza();m++){
            if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(m).getNaziv().equalsIgnoreCase(books.get(kljuc).getNaziv())) p=m;
        }

        final int i=p;
        final StringBuilder sb = new StringBuilder();
        if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).isKojaJeKnjiga()) imeAutora.setText(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora().toString());
        else {

            for (int k = 0; k < KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size(); k++) {
                if (k != KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().size() - 1)
                    sb.append(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(k).getImeiPrezime() + ", ");
                else sb.append(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getAutori().get(k).getImeiPrezime());
            }
            imeAutora.setText(sb.toString());


            datumObjavljivanja.setText(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getDatumObjavljivanja());
            String s=String.valueOf(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getBrojStranica());
            brojStranica.setText(s);
            opis.setText(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getOpis());

        }


        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* ListeFragment lf=new ListeFragment();

                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mjestoF1,lf).commit();*/

                final int indeks = sKontakti.getSelectedItemPosition();
                Log.i("Send email","");
                String[] TO={sKontakti.getSelectedItem().toString()};
                String[] CC={""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                String zaSlanje="Nepoznat autor!";

                if(!KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).kojaJeKnjiga){
                    zaSlanje = "Zdravo " + imena.get(indeks) + ",\nPročitaj knjigu "+ KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv() +" od " + sb.toString() + "!";
                }
                else zaSlanje = "Zdravo " + imena.get(indeks) + ",\nPročitaj knjigu "+ KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv() +" od " + KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getImeAutora() + "!";
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, zaSlanje);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    getActivity().finish();
                    Log.i("Finished sending email", "");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).setObojena(true);

        nazivKnjige.setText(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaziv().toString());

        if(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).kojaJeKnjiga == false) {
            String url = "https://images.hellogiggles.com/uploads/2017/02/18014708/shutterstock_135114548.jpg";
            URL slika = KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getSlika();
            Picasso.with(getActivity()).load(slika.toString()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(naslovnaStrana, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
        }
        else naslovnaStrana.setImageBitmap(KontejnerskaKlasaKnjiga.vratiKnjiguNaPoziciji(i).getNaslovnaStrana());


    }

    private void ispuniSpinner() {
        Spinner sKontakti = (Spinner)getView().findViewById(R.id.sKontakti);


        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        String name= ContactsContract.Contacts.DISPLAY_NAME;
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String email= ContactsContract.CommonDataKinds.Email.DATA;
        String _ID = ContactsContract.Contacts._ID;
        String emailContactId = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        int brojac=0;
        if(cur.getCount()>0){

            while(cur.moveToNext()){
                String contact_id = cur.getString(cur.getColumnIndex(_ID));
                Cursor emailCur = cr.query(emailUri,null, emailContactId + "=?", new String[]{contact_id},null);
                int br=0;
                while(emailCur.moveToNext()){
                    imejl.add(emailCur.getString(emailCur.getColumnIndex(email)));
                    imena.add(cur.getString(cur.getColumnIndex(name)));
                }

                emailCur.close();
            }

        }

        ArrayAdapter<String> adapterZaSpiner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, imejl);
        adapterZaSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sKontakti.setAdapter(adapterZaSpiner);
    }

}
