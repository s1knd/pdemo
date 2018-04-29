package com.example.hp.pdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    public static final String KWS_SEARCH = "کال کریں";
    public static final String MENU_SEARCH = "عابد کو کال کرو";
    /* Keyword we are looking for to activate recognition */
    private static final String KEYPHRASE = "کال کریں";
    public static String result;


    Button btn1;
    TextView textView;

    /* Recognition object */
    public static  SpeechRecognizer recognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.button);
        textView=findViewById(R.id.textView);
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
                    startL();
                }
            }
        }.execute();
    }

    public void startL(){

        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
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
        if(a.contains("کریں")){
            Intent in=new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(in);
        }
        /*if(a.contains("کال")){
            Intent intent = new Intent(Intent.ACTION_DIAL);*//*Uri.parse("tel:" + "03066290363")*//*
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

            startActivity(intent);
        }*/
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "new.dict.dic"))
                // Disable this line if you don't want recognizer to save raw
                // audio files to app's storage
                //.setRawLogDir(assetsDir)
                .getRecognizer();
        recognizer.addListener(this);
        // Create keyword-activation search.
        //recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);
        // Create language model search
        File languageModel = new File(assetsDir, "new.lm");
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
       /* if (hypothesis != null) {
            //System.out.println(hypothesis.getHypstr());
            Log.i("Eror", "onResult: 333333333333333333333333333");
            String a= hypothesis.getHypstr();
            showResult(a);
        }
        else
            Log.i("Eror", "onResult: 222222222222222222222222");*/
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis != null) {
            //Log.i("Results", "onResult "+hypothesis.getHypstr());
            String a= hypothesis.getHypstr();
            result=a;
            showResult(a);
        }
        //else
            //Log.i("Eror", "onResult: Errrrrrorrrrrrrrrrrrrrrrr");
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void onTimeout() {
        /*switchSearch(MENU_SEARCH);*/
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();*/

    }

    public static void startLi(String searchName)
    {
        recognizer.startListening(searchName);

    }

    public static void stopL()
    {
        recognizer.stop();
    }

    public static String getResult()
    {
        return result;
    }
}
