package com.example.edina.spirala17933;

import android.database.CursorJoiner;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Edina on 19.05.2018..
 */

public class customResultResiver extends ResultReceiver {

    public void setReceiver(Receiver mReceiver) {
        this.mReceiver = mReceiver;
    }

    private Receiver mReceiver;

    public customResultResiver(Handler handler) {
        super(handler);
    }


    public interface Receiver {
        public void onReceiveResult(int resulCode, Bundle resultData);
    }

    @Override
    public void  onReceiveResult(int resultCode, Bundle resultData){
        if(mReceiver!=null)
            mReceiver.onReceiveResult(resultCode,resultData);
    }



}
