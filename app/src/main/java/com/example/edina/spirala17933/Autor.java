package com.example.edina.spirala17933;

import java.util.ArrayList;

/**
 * Created by Edina on 16.05.2018..
 */

public class Autor {


    public Autor() {
    }

    String imeiPrezime;
    ArrayList<String> knjige = new ArrayList<String>();



    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        dodajKnjigu(id);

    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    void dodajKnjigu(String id){
        boolean prisutanId=true;
        for(int i=0;i<knjige.size();i++){
            if(knjige.get(i).equals(id)) prisutanId=false;
        }

        if(prisutanId) knjige.add(id);
    }
}
