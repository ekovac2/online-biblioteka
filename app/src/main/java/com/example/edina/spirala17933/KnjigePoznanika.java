package com.example.edina.spirala17933;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Edina on 19.05.2018..
 */

public class KnjigePoznanika extends IntentService {

    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();

    public static int STATUS_START = 0;
    public static int STATUS_FINISH = 1;
    public static int STATUS_ERROR = 2;

    public KnjigePoznanika(String name) {
        super(name);
    }
    public KnjigePoznanika(){
        super("");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String idUsera = intent.getStringExtra("id");

        String url1 = "https://www.googleapis.com/books/v1/users/" + idUsera + "/bookshelves";

        try {

            URL urll = new URL(url1);
            HttpURLConnection urlConnection1 = (HttpURLConnection) urll.openConnection();

            InputStream in1 = new BufferedInputStream(urlConnection1.getInputStream());
            String rezultat1 = convertStreamToString(in1);
            JSONObject jo1 = new JSONObject(rezultat1);

                JSONArray items1 = jo1.getJSONArray("items");

                for (int j = 0; j < items1.length(); j++) {
                    JSONObject polica = items1.getJSONObject(j);
                    String idP = "";
                    if (polica.has("id")) idP = polica.getString("id");


                    String url2 = "https://www.googleapis.com/books/v1/users/" + idUsera + "/bookshelves/" + idP + "/volumes";

                    try {

                        URL url = new URL(url2);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String rezultat = convertStreamToString(in);
                        JSONObject jo = new JSONObject(rezultat);

                            JSONArray items = jo.getJSONArray("items");

                            for (int i = 0; i < items.length(); i++) {
                                JSONObject izdvojiKnjigu = items.getJSONObject(i);
                                JSONObject informacije = izdvojiKnjigu.getJSONObject("volumeInfo");

                                String id = "", naziv = "", opis = "", datumObjavljivanja = "";
                                int brojStranica = 0;
                                ArrayList<Autor> autori = new ArrayList<Autor>();
                                URL slika = new URL("https://images.hellogiggles.com/uploads/2017/02/18014708/shutterstock_135114548.jpg");

                                if (izdvojiKnjigu.has("id")) id = izdvojiKnjigu.getString("id");

                                if (informacije.has("title")) naziv = informacije.getString("title");
                                if (informacije.has("authors")) {
                                    JSONArray autor = informacije.getJSONArray("authors");
                                    for (int m = 0; m < autor.length(); m++) {
                                        autori.add(new Autor(autor.getString(m), ""));
                                    }
                                }
                                if (informacije.has("publishedDate"))
                                    datumObjavljivanja = informacije.getString("publishedDate");
                                if (informacije.has("description"))
                                    opis = informacije.getString("description");
                                if (informacije.has("pageCount"))
                                    brojStranica = Integer.parseInt(informacije.getString("pageCount"));
                                if (informacije.has("imageLinks")) {
                                    JSONObject urlovi = informacije.getJSONObject("imageLinks");
                                    if (urlovi.has("tumbnail"))
                                        slika = new URL(urlovi.getString("tumbnail"));

                                }
                                knjige.add(new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica));


                        }
                    } catch (MalformedURLException e) {
                        receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
                    } catch (IOException e) {
                        receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
                    } catch (JSONException e) {
                        receiver.send(KnjigePoznanika.STATUS_ERROR, Bundle.EMPTY);
                    }
                }


        } catch (MalformedURLException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR,Bundle.EMPTY);
        } catch (IOException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR,Bundle.EMPTY);
        } catch (JSONException e) {
            receiver.send(KnjigePoznanika.STATUS_ERROR,Bundle.EMPTY);
        }


        Bundle b = new Bundle();
        b.putParcelableArrayList("listaKnjiga", knjige);
        receiver.send(KnjigePoznanika.STATUS_FINISH,b);


    }
    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

}
