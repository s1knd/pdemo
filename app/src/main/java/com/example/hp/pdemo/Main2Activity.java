package com.example.hp.pdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static com.example.hp.pdemo.MainActivity.KWS_SEARCH;

public class Main2Activity extends AppCompatActivity{

    public static SpeechRecognizer recognizer;
    Hypothesis hypothesis;
    Button btn2;
    TextView textView;
    MainActivity mainActivity;
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
                      MainActivity.startLi("عابد کو کال کرو");
                      break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.stopL();
                        showResult();
                        break;

                }
                return false;
            }
        });
    }
    public void showResult(){
        String a=MainActivity.getResult();
        textView.setText(a);
    }

}
