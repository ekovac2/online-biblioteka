package com.example.edina.spirala17933;

import android.os.AsyncTask;

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
 * Created by Edina on 17.05.2018..
 */

public class DohvatiKnjige extends AsyncTask<String,Integer,Void> {

    ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();


    public DohvatiKnjige(IDohvatiKnjigeDone p) {pozivatelj = p;}

    @Override
    protected Void doInBackground(String... strings) {

        String query = null;
        try {
            query = URLEncoder.encode(strings[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=5";


        try {

            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);
            JSONObject jo = new JSONObject(rezultat);

            JSONArray items = jo.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject izdvojiKnjigu = items.getJSONObject(i);
                JSONObject informacije = izdvojiKnjigu.getJSONObject("volumeInfo");

                String id="", naziv="",opis="", datumObjavljivanja="";
                int brojStranica=0;
                ArrayList<Autor> autori = new ArrayList<Autor>();
                URL slika = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy_M7GYkU2drtb1VPTaKGXhTlcwKL45XZ21x5fLsDdwEMi0d8M");

                if(izdvojiKnjigu.has("id")) id=izdvojiKnjigu.getString("id");
                if(informacije.has("title")) naziv=informacije.getString("title");
                if(informacije.has("authors")){
                    JSONArray autor = informacije.getJSONArray("authors");
                    for(int j=0;j<autor.length();j++){
                        autori.add(new Autor(autor.getString(j), ""));
                    }
                }
                if(informacije.has("publishedDate")) datumObjavljivanja = informacije.getString("publishedDate");
                if(informacije.has("description")) opis=informacije.getString("description");
                if(informacije.has("pageCount")) brojStranica = Integer.parseInt(informacije.getString("pageCount"));
                if(informacije.has("imageLinks")){
                    JSONObject urlovi = informacije.getJSONObject("imageLinks");
                    if(urlovi.has("thumbnail")) slika = new URL(urlovi.getString("thumbnail"));
                }
                knjige.add(new Knjiga(id,naziv,autori,opis,datumObjavljivanja,slika,brojStranica));


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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


    public interface IDohvatiKnjigeDone{
        public void onDohvatiDone(ArrayList<Knjiga> k);  }

    private IDohvatiKnjigeDone pozivatelj;



    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(knjige);
    }



}
