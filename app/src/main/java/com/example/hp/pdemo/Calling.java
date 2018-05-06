package com.example.hp.pdemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;






public class Calling extends MainActivity {

    MainActivity mainActivity;
    MediaPlayer mediaPlayer;
    String cResult=null;

    Uri uri;

    Intent intent=new Intent(Intent.ACTION_VIEW);
    public void sendData(String cString)
    {

        {
            uri=Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, cResult);
            startActivity(intent);
        }
    }
}
