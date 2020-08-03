package com.example.edina.spirala17933;

import android.app.AutomaticZenRule;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Edina on 25.03.2018..
 */

public class Knjiga implements Serializable, Parcelable{


    Bitmap naslovnaStrana;
    String imeAutora;

    Kategorije kategorijaKnjige;

    String id;
    String naziv;
    ArrayList<Autor> autori = new ArrayList<Autor>();
    String opis;
    String datumObjavljivanja;
    URL slika;
    int brojStranica;

    protected Knjiga(Parcel in) {
        naslovnaStrana = in.readParcelable(Bitmap.class.getClassLoader());
        imeAutora = in.readString();
        kategorijaKnjige = in.readParcelable(Kategorije.class.getClassLoader());
        id = in.readString();
        naziv = in.readString();
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStranica = in.readInt();
        kojaJeKnjiga = in.readByte() != 0;
        obojena = in.readByte() != 0;
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public boolean isKojaJeKnjiga() {
        return kojaJeKnjiga;
    }

    public void setKojaJeKnjiga(boolean kojaJeKnjiga) {
        this.kojaJeKnjiga = kojaJeKnjiga;
    }

    boolean kojaJeKnjiga = true;


    boolean obojena;


    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.kojaJeKnjiga =  false;
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica,Kategorije kategorija) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.kategorijaKnjige = kategorija;
        this.kojaJeKnjiga =  false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }




    public boolean isObojena() {
        return obojena;
    }

    public void setObojena(boolean obojena) {
        this.obojena = obojena;
    }

    public Bitmap getNaslovnaStrana() {
        return naslovnaStrana;
    }

    public void setNaslovnaStrana(Bitmap naslovnaStrana) {
        this.naslovnaStrana = naslovnaStrana;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.naziv = nazivKnjige;
    }

    public Kategorije getKategorijaKnjige() {
        return kategorijaKnjige;
    }

    public void setKategorijaKnjige(Kategorije kategorijaKnjige) {
        this.kategorijaKnjige = kategorijaKnjige;
    }

    public Knjiga(String imeAutora, String nazivKnjige, Kategorije kategorijaKnjige) {
        this.imeAutora = imeAutora;
        this.naziv = nazivKnjige;
        this.kategorijaKnjige = kategorijaKnjige;
        obojena=false;
        //this.kojaJeKnjiga =  false;
    }

    public Knjiga(Bitmap naslovnaStrana, String imeAutora, String nazivKnjige, Kategorije kategorijaKnjige) {
        this.naslovnaStrana = naslovnaStrana;
        this.imeAutora = imeAutora;
        this.naziv = nazivKnjige;
        this.kategorijaKnjige = kategorijaKnjige;
        this.kojaJeKnjiga =  true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(naslovnaStrana, i);
        parcel.writeString(imeAutora);
        parcel.writeParcelable(kategorijaKnjige, i);
        parcel.writeString(id);
        parcel.writeString(naziv);
        parcel.writeString(opis);
        parcel.writeString(datumObjavljivanja);
        parcel.writeInt(brojStranica);
        parcel.writeByte((byte) (kojaJeKnjiga ? 1 : 0));
        parcel.writeByte((byte) (obojena ? 1 : 0));
    }
}
