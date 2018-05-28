package com.example.hp.pdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;

public class Main4Activity extends MainActivity implements RecognitionListener{

/*public static SpeechRecognizer recognizer;*//**/

    Hypothesis hypothesis;
    Button btn3;
    TextView textView3;
    MainActivity mainActivity;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btn3=findViewById(R.id.button) ;
        textView3=findViewById(R.id.textView);
        welcom=MediaPlayer.create(this, R.raw.callname);
        welcom.start();
        startLm();
    }
    public void startLm()
    {
        Toast.makeText(this,"Speak Contact Name", Toast.LENGTH_LONG).show();

        btn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        welcom.release();
                        recognizer.startListening("عابد کو کال کرو");
                        break;

                    case MotionEvent.ACTION_UP:
                        recognizer.stop();
                        //showResultThis();
                        break;

                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Main4Activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void showResultThis(String a) {
        textView3.setText(a);
        if(a.contains("عابد"))
        {
            String number=getPhoneNumber("عابد" , this);
            Log.i("nnnnnnnnnnn", "showResultThis: "+number);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        }

    }
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //Log.i("Results", "onResult "+hypothesis.getHypstr());
            Log.i("1111111111", "onResult: 111111111111111111");
            String a= hypothesis.getHypstr();
            showResultThis(a);
        }
        // jb recoginzation start karta hian magr wo koi word recoginzed nhi kar peta tu yeha ajata hain or jo pichla data hota hain sarif wohi show karvta hain.
        else
        {
            welcom=MediaPlayer.create(this,R.raw.again);
            welcom.start();
            Log.i("Eror", "onResult: Errrrrrorrrrrrrrrrrrrrrrr");
            result="";
            showResultThis("");
        }
    }

    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "Unsaved";
        return ret;
    }


}
