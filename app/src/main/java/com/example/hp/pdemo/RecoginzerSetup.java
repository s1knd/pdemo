package com.example.hp.pdemo;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static com.example.hp.pdemo.MainActivity.MENU_SEARCH;

//import static com.example.hp.pdemo.MainActivity.recognizer;

public class RecoginzerSetup implements RecognitionListener
{
    public static  SpeechRecognizer recognizer;
    public static Context mContext;



    RecoginzerSetup( Context context){

        mContext=context;
            new AsyncTask<Void, Void, Exception>() {
                @Override
                protected Exception doInBackground(Void... params) {
                    try {
                        Assets assets = new Assets(mContext);
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

                    }
                }
            };
        }

    protected void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "new.dict.dic"))
                // Disable this line if you don't want recognizer to save raw
                // audio files to app's storage
                //.setRawLogDir(assetsDir)
                .getRecognizer();
        recognizer.addListener((RecognitionListener) mContext);
        File languageModel = new File(assetsDir, "new.lm");
        recognizer.addNgramSearch(MENU_SEARCH, languageModel);
    }

    public void startL(String searchName)
    {
        recognizer.startListening(searchName);

    }

    public void stopL()
    {
        recognizer.stop();
    }

    public String getResult(String s)
    {
        return s;
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
            String a= hypothesis.getHypstr();
            getResult(a);

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

    }
}

