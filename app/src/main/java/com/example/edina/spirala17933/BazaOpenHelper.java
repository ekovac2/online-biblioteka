package com.example.edina.spirala17933;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Edina on 25.05.2018..
 */

public class BazaOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="mojaBaza.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_TABLE_KATEGORIJA="Kategorija";
    public static final String KATEGORIJA_ID = "_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    public static final String DATABASE_TABLE_KNJIGA="Knjiga";
    public static final String KNJIGA_ID = "_id";
    public static final String KNJIGA_NAZIV = "naziv";
    public static final String KNJIGA_OPIS = "opis";
    public static final String KNJIGA_DATUM_OBJAVLJIVANJA = "datumObjavljivanja";
    public static final String KNJIGA_BROJ_STRANICA = "brojStranica";
    public static final String KNJIGA_ID_WEBSERVIS = "idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE="idkategorije";
    public static final String KNJIGA_SLIKA="slika";
    public static final String KNJIGA_PREGLEDANA="pregledana";

    public static final String DATABASE_TABLE_AUTOR="Autor";
    public static final String AUTOR_ID = "_id";
    public static final String AUTOR_IME = "ime";


    public static final String DATABASE_TABLE_AUTORSTVO="Autorstvo";
    public static final String AUTORSTVO_ID = "_id";
    public static final String AUTORSTVO_ID_AUTORA = "idautora";
    public static final String AUTORSTVO_ID_KNJIGE = "idknjige";


    private  static final String DATABASE_CREATE_KATEGORIJA = "create table " + DATABASE_TABLE_KATEGORIJA + " (" + KATEGORIJA_ID + " integer primary key autoincrement, " +
            KATEGORIJA_NAZIV + " text not null);";

    private  static final String DATABASE_CREATE_KNJIGA = "create table " + DATABASE_TABLE_KNJIGA + " (" + KNJIGA_ID + " integer primary key autoincrement, " +
            KNJIGA_NAZIV + " text not null, " + KNJIGA_OPIS + " text not null, " + KNJIGA_DATUM_OBJAVLJIVANJA + " text not null, " + KNJIGA_BROJ_STRANICA + " integer not null, "
            + KNJIGA_ID_WEBSERVIS + " text not null, " + KNJIGA_ID_KATEGORIJE+ " integer not null, " + KNJIGA_SLIKA + " text not null, " + KNJIGA_PREGLEDANA + " integer not null);";

    private  static final String DATABASE_CREATE_AUTOR = "create table " + DATABASE_TABLE_AUTOR + " (" + AUTOR_ID + " integer primary key autoincrement, " +
            AUTOR_IME + " text not null);";

    private  static final String DATABASE_CREATE_AUTORSTVO = "create table " + DATABASE_TABLE_AUTORSTVO + " (" + AUTORSTVO_ID + " integer primary key autoincrement, " +
            AUTORSTVO_ID_AUTORA + " integer not null, " + AUTORSTVO_ID_KNJIGE +" integer not null);";


    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase sb = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_KATEGORIJA);
        sqLiteDatabase.execSQL(DATABASE_CREATE_KNJIGA);
        sqLiteDatabase.execSQL(DATABASE_CREATE_AUTOR);
        sqLiteDatabase.execSQL(DATABASE_CREATE_AUTORSTVO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KNJIGA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_KATEGORIJA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_AUTORSTVO);

        onCreate(sqLiteDatabase);
    }

    long dodajKategoriju(String naziv){
        SQLiteDatabase sb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KATEGORIJA_NAZIV, naziv);

       return sb.insert(DATABASE_TABLE_KATEGORIJA, null, cv);
    }

    long dodajKnjigu(Knjiga knjiga){
        SQLiteDatabase sb = getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(knjiga.kojaJeKnjiga) {
            boolean tu = false;
            for (int i = 0; i < ListeFragment.mojaListaAutora.size(); i++) {
                if (ListeFragment.mojaListaAutora.get(i).equalsIgnoreCase(knjiga.getImeAutora()))
                    tu = true;
            }
            if (!tu) {
                ListeFragment.mojaListaAutora.add(knjiga.getImeAutora());
                KategorijeAkt.boh.dodajAutora(knjiga.getImeAutora());
            }
        }

        if(!knjiga.kojaJeKnjiga){

            cv.put(KNJIGA_BROJ_STRANICA, knjiga.getBrojStranica());
            cv.put(KNJIGA_NAZIV, knjiga.getNaziv());
            cv.put(KNJIGA_DATUM_OBJAVLJIVANJA, knjiga.getDatumObjavljivanja());
            cv.put(KNJIGA_OPIS, knjiga.getOpis());
            cv.put(KNJIGA_SLIKA, String.valueOf(knjiga.getSlika()));
            int ima=0;
            if(knjiga.isObojena()) ima=1;
            cv.put(KNJIGA_PREGLEDANA,ima);

            int indeks=0;
            Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
            while(cr.moveToNext()){
                if(knjiga.getKategorijaKnjige().getNaziv().equalsIgnoreCase(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)))) indeks=cr.getInt(cr.getColumnIndex(KATEGORIJA_ID));
            }
            cv.put(KNJIGA_ID_KATEGORIJE, indeks);
            cv.put(KNJIGA_ID_WEBSERVIS, knjiga.getId());

        }
        else{
            cv.put(KNJIGA_BROJ_STRANICA, 0);
            cv.put(KNJIGA_NAZIV, knjiga.getNaziv());
            cv.put(KNJIGA_DATUM_OBJAVLJIVANJA, "");
            cv.put(KNJIGA_OPIS, "");
            cv.put(KNJIGA_SLIKA, BitmapUString(knjiga.getNaslovnaStrana()));
            int ima=0;
            if(knjiga.isObojena()) ima=1;
            cv.put(KNJIGA_PREGLEDANA,ima);
            int indeks=0;
            Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
            while(cr.moveToNext()){
                if(knjiga.getKategorijaKnjige().getNaziv().equalsIgnoreCase(cr.getString(cr.getColumnIndex(KATEGORIJA_NAZIV)))) indeks=cr.getInt(cr.getColumnIndex(KATEGORIJA_ID));
            }
            cv.put(KNJIGA_ID_KATEGORIJE, indeks);
            cv.put(KNJIGA_ID_WEBSERVIS, "");
        }
        return sb.insert(DATABASE_TABLE_KNJIGA, null, cv);
    }

    long dodajAutora(String naziv){
        SQLiteDatabase sb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AUTOR_IME, naziv);

        return sb.insert(DATABASE_TABLE_AUTOR, null, cv);
    }

    public Cursor getAllData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+name,null);
        return res;
    }

    long dodajAutorstvo(int iAutor,int iKnjiga){
        SQLiteDatabase sb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AUTORSTVO_ID_AUTORA, iAutor);
        cv.put(AUTORSTVO_ID_KNJIGE, iKnjiga);

        return sb.insert(DATABASE_TABLE_AUTORSTVO, null, cv);

    }

    public void ucitajKnjige() throws MalformedURLException {
        KontejnerskaKlasaKnjiga.resetuj();

        Cursor cr = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KNJIGA);
        while(cr.moveToNext()) {

            String idWeb = cr.getString(cr.getColumnIndex(KNJIGA_ID_WEBSERVIS));
            if(!idWeb.equalsIgnoreCase("")){

                ArrayList<Autor> autor = new ArrayList<Autor>();
                int idKnjige = cr.getInt(cr.getColumnIndex(KNJIGA_ID));
                int brStranica = cr.getInt(cr.getColumnIndex(KNJIGA_BROJ_STRANICA));
                String naziv = cr.getString(cr.getColumnIndex(KNJIGA_NAZIV));
                String datum = cr.getString(cr.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA));
                String opis = cr.getString(cr.getColumnIndex(KNJIGA_OPIS));
                URL slika = new URL(cr.getString(cr.getColumnIndex(KNJIGA_SLIKA)));
                int obojena = cr.getInt(cr.getColumnIndex(KNJIGA_PREGLEDANA));
                int idKat = cr.getInt(cr.getColumnIndex(KNJIGA_ID_KATEGORIJE));

                Kategorije k = new Kategorije("");
                Cursor kat = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                while(kat.moveToNext()){
                    if(kat.getInt(kat.getColumnIndex(KATEGORIJA_ID)) == idKat) k = new Kategorije(kat.getString(kat.getColumnIndex(KATEGORIJA_NAZIV)));
                }
                kat.close();

                Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
                while(aut.moveToNext()){
                    if(aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige){
                        Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                        while(autori.moveToNext()){
                            if(autori.getInt(autori.getColumnIndex(AUTOR_ID))==aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA))) autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                        }
                    }
                }



                Knjiga kk = new Knjiga(String.valueOf(idWeb), naziv, autor, opis, datum, slika, brStranica, k);
                if(obojena==1) kk.setObojena(true);
                else kk.setObojena(false);
                KontejnerskaKlasaKnjiga.ubaciNovuKnjigu(kk);
            }
        else{
            ArrayList<Autor> autor = new ArrayList<Autor>();
            int idKnjige = cr.getInt(cr.getColumnIndex(KNJIGA_ID));
            String naziv = cr.getString(cr.getColumnIndex(KNJIGA_NAZIV));
            Bitmap slika = StringUBitmap(cr.getString(cr.getColumnIndex(KNJIGA_SLIKA)));
            int obojena = cr.getInt(cr.getColumnIndex(KNJIGA_PREGLEDANA));
            int idKat = cr.getInt(cr.getColumnIndex(KNJIGA_ID_KATEGORIJE));

            Kategorije k = new Kategorije("");
            Cursor kat = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
            while(kat.moveToNext()){
                if(kat.getInt(kat.getColumnIndex(KATEGORIJA_ID)) == idKat) k = new Kategorije(kat.getString(kat.getColumnIndex(KATEGORIJA_NAZIV)));
            }
            kat.close();

            Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
            while(aut.moveToNext()){
                if(aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige){
                    Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                    while(autori.moveToNext()){
                        if(autori.getInt(autori.getColumnIndex(AUTOR_ID))==aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA))) autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                    }
                }
            }

            Knjiga kk = new Knjiga(slika,autor.get(0).getImeiPrezime(), naziv,k);
                if(obojena==1) kk.setObojena(true);
                else kk.setObojena(false);
            KontejnerskaKlasaKnjiga.ubaciNovuKnjigu(kk);

            }

        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e){
            Log.d("greska", e.toString());
            return null;
        }
    }

    ArrayList<Knjiga> knjigeKategorije(long idKategorije) throws MalformedURLException {
        ArrayList<Knjiga> lista= new ArrayList<Knjiga>();

        Kategorije k = new Kategorije("");
        Cursor kat = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
        while(kat.moveToNext()) {
            int id = kat.getInt(kat.getColumnIndex(KATEGORIJA_ID)); //long
            if (id == Integer.valueOf(String.valueOf(idKategorije))) k = new Kategorije(kat.getString(kat.getColumnIndex(KATEGORIJA_NAZIV)));
        }
        Cursor knj = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KNJIGA);
        while(knj.moveToNext()) {
            if (knj.getInt(knj.getColumnIndex(KNJIGA_ID_KATEGORIJE)) ==  Integer.valueOf(String.valueOf(idKategorije))) {
                String idWeb = knj.getString(knj.getColumnIndex(KNJIGA_ID_WEBSERVIS));
                if (!idWeb.equalsIgnoreCase("")) {
                    ArrayList<Autor> autor = new ArrayList<Autor>();
                    int idKnjige = knj.getInt(knj.getColumnIndex(KNJIGA_ID));
                    int brStranica = knj.getInt(knj.getColumnIndex(KNJIGA_BROJ_STRANICA));
                    String naziv = knj.getString(knj.getColumnIndex(KNJIGA_NAZIV));
                    String datum = knj.getString(knj.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA));
                    String opis = knj.getString(knj.getColumnIndex(KNJIGA_OPIS));
                    URL slika = new URL(knj.getString(knj.getColumnIndex(KNJIGA_SLIKA)));
                    int obojena = knj.getInt(knj.getColumnIndex(KNJIGA_PREGLEDANA));
                    int idKat = knj.getInt(knj.getColumnIndex(KNJIGA_ID_KATEGORIJE));


                    Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
                    while (aut.moveToNext()) {
                        if (aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige) {
                            Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                            while (autori.moveToNext()) {
                                if (autori.getInt(autori.getColumnIndex(AUTOR_ID)) == aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA)))
                                    autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                            }
                        }
                    }


                    Knjiga kk = new Knjiga(String.valueOf(idWeb), naziv, autor, opis, datum, slika, brStranica, k);
                    if(obojena==1) kk.setObojena(true);
                    else kk.setObojena(false);
                    lista.add(kk);

                } else {
                    ArrayList<Autor> autor = new ArrayList<Autor>();
                    int idKnjige = knj.getInt(knj.getColumnIndex(KNJIGA_ID));
                    String naziv = knj.getString(knj.getColumnIndex(KNJIGA_NAZIV));
                    Bitmap slika = StringUBitmap(knj.getString(knj.getColumnIndex(KNJIGA_SLIKA)));
                    int obojena = knj.getInt(knj.getColumnIndex(KNJIGA_PREGLEDANA));
                    int idKat = knj.getInt(knj.getColumnIndex(KNJIGA_ID_KATEGORIJE));
                    Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
                    while (aut.moveToNext()) {
                        if (aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige) {
                            Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                            while (autori.moveToNext()) {
                                if (autori.getInt(autori.getColumnIndex(AUTOR_ID)) == aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA)))
                                    autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                            }
                        }
                    }

                    Knjiga kk = new Knjiga(slika, autor.get(0).getImeiPrezime(), naziv, k);
                    if(obojena==1) kk.setObojena(true);
                    else kk.setObojena(false);
                    lista.add(kk);

                }
            }
        }

        return lista;
    }

    ArrayList<Knjiga> knjigeAutora(long idAutora) throws MalformedURLException {
        ArrayList<Knjiga> lista = new ArrayList<Knjiga>();

        ArrayList<Integer> listaknjiga = new ArrayList<Integer>();
        Cursor autorstvo = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
        while(autorstvo.moveToNext()){
            int autorId = autorstvo.getInt(autorstvo.getColumnIndex(AUTORSTVO_ID_AUTORA));
            if(autorId == Integer.valueOf(String.valueOf(idAutora))) listaknjiga.add(autorstvo.getInt(autorstvo.getColumnIndex(AUTORSTVO_ID_KNJIGE)));
        }

        Cursor knj = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KNJIGA);
        while(knj.moveToNext()) {
            int knjigeId = knj.getInt(knj.getColumnIndex(KNJIGA_ID));
            for (int i = 0; i < listaknjiga.size(); i++) {
                if (knjigeId == listaknjiga.get(i)) {
                    String idWeb = knj.getString(knj.getColumnIndex(KNJIGA_ID_WEBSERVIS));
                    if (!idWeb.equalsIgnoreCase("")) {
                        ArrayList<Autor> autor = new ArrayList<Autor>();
                        int idKnjige = knj.getInt(knj.getColumnIndex(KNJIGA_ID));
                        int brStranica = knj.getInt(knj.getColumnIndex(KNJIGA_BROJ_STRANICA));
                        String naziv = knj.getString(knj.getColumnIndex(KNJIGA_NAZIV));
                        String datum = knj.getString(knj.getColumnIndex(KNJIGA_DATUM_OBJAVLJIVANJA));
                        String opis = knj.getString(knj.getColumnIndex(KNJIGA_OPIS));
                        URL slika = new URL(knj.getString(knj.getColumnIndex(KNJIGA_SLIKA)));
                        int obojena = knj.getInt(knj.getColumnIndex(KNJIGA_PREGLEDANA));
                        int idKat = knj.getInt(knj.getColumnIndex(KNJIGA_ID_KATEGORIJE));

                        Kategorije k = new Kategorije("");
                        Cursor kat = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                        while(kat.moveToNext()){
                            if(kat.getInt(kat.getColumnIndex(KATEGORIJA_ID)) == idKat) k = new Kategorije(kat.getString(kat.getColumnIndex(KATEGORIJA_NAZIV)));
                        }
                        kat.close();

                        Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
                        while (aut.moveToNext()) {
                            if (aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige) {
                                Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                                while (autori.moveToNext()) {
                                    if (autori.getInt(autori.getColumnIndex(AUTOR_ID)) == aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA)))
                                        autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                                }
                            }
                        }


                        Knjiga kk = new Knjiga(String.valueOf(idWeb), naziv, autor, opis, datum, slika, brStranica, k);
                        if(obojena==1) kk.setObojena(true);
                        else kk.setObojena(false);
                        lista.add(kk);

                    } else {
                        ArrayList<Autor> autor = new ArrayList<Autor>();
                        int idKnjige = knj.getInt(knj.getColumnIndex(KNJIGA_ID));
                        String naziv = knj.getString(knj.getColumnIndex(KNJIGA_NAZIV));
                        Bitmap slika = StringUBitmap(knj.getString(knj.getColumnIndex(KNJIGA_SLIKA)));
                        int obojena = knj.getInt(knj.getColumnIndex(KNJIGA_PREGLEDANA));
                        int idKat = knj.getInt(knj.getColumnIndex(KNJIGA_ID_KATEGORIJE));
                        Cursor aut = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTORSTVO);
                        while (aut.moveToNext()) {
                            if (aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_KNJIGE)) == idKnjige) {
                                Cursor autori = KategorijeAkt.boh.getAllData(DATABASE_TABLE_AUTOR);
                                while (autori.moveToNext()) {
                                    if (autori.getInt(autori.getColumnIndex(AUTOR_ID)) == aut.getInt(aut.getColumnIndex(AUTORSTVO_ID_AUTORA)))
                                        autor.add(new Autor(autori.getString(autori.getColumnIndex(AUTOR_IME)), String.valueOf(autori.getInt(autori.getColumnIndex(AUTOR_ID)))));
                                }
                            }
                        }
                        Kategorije k = new Kategorije("");
                        Cursor kat = KategorijeAkt.boh.getAllData(DATABASE_TABLE_KATEGORIJA);
                        while(kat.moveToNext()){
                            if(kat.getInt(kat.getColumnIndex(KATEGORIJA_ID)) == idKat) k = new Kategorije(kat.getString(kat.getColumnIndex(KATEGORIJA_NAZIV)));
                        }
                        kat.close();

                        Knjiga kk = new Knjiga(slika, autor.get(0).getImeiPrezime(), naziv, k);
                        if(obojena==1) kk.setObojena(true);
                        else kk.setObojena(false);
                        lista.add(kk);


                    }
                }


            }

        }



        return lista;
    }


    private String BitmapUString(Bitmap b){
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] bA=baos.toByteArray();
        return Base64.encodeToString(bA, Base64.DEFAULT);
    }
    private Bitmap StringUBitmap(String s){
        byte[] bA=Base64.decode(s.getBytes(),Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bA,0,bA.length);
    }



}
