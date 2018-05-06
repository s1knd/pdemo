/*
package com.example.hp.pdemo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;


public class Main3Activity extends MainActivity{

    public static SpeechRecognizer recognizer;
    Hypothesis hypothesis;
    Button btn2;
    TextView textView;
    MainActivity mainActivity;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn2 = findViewById(R.id.button2);
        textView=findViewById(R.id.textView2);
        startL();
    }
    public void startL()
    {

        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                      recognizer.startListening("عابد کو کال کرو");
                      break;

                    case MotionEvent.ACTION_UP:
                        recognizer.stop();
                        showResult();
                        break;

                }
                return false;
            }
        });
    }
    public void showResult(){
        String a=mainActivity.getResult();
        textView.setText(a);
if (a.contains("کال"))
        {
            Calling call=new Calling();
            call.sendData(a);
        }
        else {

        //mediaPlayer= MediaPlayer.create(Main2Activity.this, R.raw.abc);
        //mediaPlayer.start();


}
}
*/
