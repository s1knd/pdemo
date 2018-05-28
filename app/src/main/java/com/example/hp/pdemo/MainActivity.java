package com.example.hp.pdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class MainActivity extends AppCompatActivity implements RecognitionListener{

    /* We only need the keyphrase to start recognition, one menu with list of choices,
     and one word that is required for method switchSearch - it will bring recognizer
     back to listening for the keyphrase*/
    public static final String MENU_SEARCH = "عابد کو کال کرو";
    /* Keyword we are looking for to activate recognition */
    private static final String KEYPHRASE = "کال کریں";
    public static String result="";

    MediaPlayer welcom;

    Intent in;
    Button btn1;
    TextView textView;

    /* Recognition object */
    public static  SpeechRecognizer recognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
       btn1=findViewById(R.id.button2);
        textView=findViewById(R.id.textView2);
        welcom=MediaPlayer.create(MainActivity.this, R.raw.welcom);
        runRecognizerSetup();
    }



    private void runRecognizerSetup() {
        // Recognizer initialization is a time-consuming and it involves IO,
        // so we execute it in async task
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(getApplicationContext());
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    System.out.println(result.getMessage());
                } else {
                    welcom.start();
                    startL();
                    //finish();
                }
            }
        }.execute();
    }

    private void startL(){


        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        welcom.release();
                        recognizer.startListening(MENU_SEARCH);
                        break;
                    case MotionEvent.ACTION_UP:
                        recognizer.stop();
                        break;
                        default:
                            startL();
                }
                return false;

            }
        });
    }




    public void showResult(String a)
    {
        Log.i("reSult", "showResult: "+a);
        textView.setText(a);

        if (a.contains("کال"))
        {

            Intent intent=new Intent(MainActivity.this, Main4Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else if(a.contains("میسج"))
        {
            Intent intent=new Intent(MainActivity.this, Message.class);
            startActivity(intent);
        }

    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "project.dict.dic"))
                // Disable this line if you don't want recognizer to save raw
                // audio files to app's storage
                //.setRawLogDir(assetsDir)
                .getRecognizer();
        recognizer.addListener(this);
        File languageModel = new File(assetsDir, "project.lm");
        recognizer.addNgramSearch(MENU_SEARCH, languageModel);
    }


    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {

    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //Log.i("Results", "onResult "+hypothesis.getHypstr());
            Log.i("1111111111", "onResult: 111111111111111111");
            String a= hypothesis.getHypstr();
            showResult(a);
        }
        // jb recoginzation start karta hian magr wo koi word recoginzed nhi kar peta tu yeha ajata hain or jo pichla data hota hain sarif wohi show karvta hain.
        else
            {
                welcom=MediaPlayer.create(this,R.raw.again);
                welcom.start();
            Log.i("Eror", "onResult: Errrrrrorrrrrrrrrrrrrrrrr");
            result="";
            showResult(result);
            }
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void onTimeout() {

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}
