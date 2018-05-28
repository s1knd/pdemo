package com.example.hp.pdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.Locale;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;

public class Message extends MainActivity implements RecognitionListener, TextToSpeech.OnInitListener {

    TextView mTextView;
    TextView cTextView;

    Button mBotton,tButton;
    int count=0;
    String no;

    /////////////////////////////////////
    private TextToSpeech tts;
    String text;



    private BroadcastReceiver smsReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        tts = new TextToSpeech(this, this);
        mTextView=findViewById(R.id.mTextView);
        mBotton=findViewById(R.id.mButton);
        cTextView=findViewById(R.id.textView4);
        tButton=findViewById(R.id.toggleButton);
        cTextView.setText("Speak Contact Name");
        initializeSMSReceiver();
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                speakOut();
            }
        });
        starML();


    }
    public void starML()
    {
        mBotton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN :{
                        recognizer.startListening("عابد کو کال کرو");
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        recognizer.stop();
                        break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis!=null){
            String a=hypothesis.getHypstr();
            selection(a);
        }
        else {
            Toast.makeText(this,"Not Listen AnyThing", Toast.LENGTH_LONG).show();
        }
    }

    public void selection(String s)
    {
        mTextView.setText(s);
        count++;
        if (count==1)
        {

            no=getPhoneNumber(s, this);
            if (no.equals("Unsaved")){
                Toast.makeText(this,"Contact Not Exist", Toast.LENGTH_LONG).show();
                count--;
                starML();
            }
            else {
                cTextView.setText("Speak Massage Now");
                Toast.makeText(this, "Now Speak Massege", Toast.LENGTH_LONG).show();
                starML();
            }
        }
        if (count==2)
        {
            sendSMS(no, s);
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

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /////////////////////////////////////////////



    private void initializeSMSReceiver(){
        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                if(bundle!=null){
                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);
                        text = message.getDisplayMessageBody();
                        String sender = getContactName(message.getOriginatingAddress());
                        //smsSender.setText("Message from " + sender);
                        //smsText.setText(text);
                        Log.i("Sender", "nnnnnnnnnnnn: "+sender);
                        Log.i("Masseege", "mmmmmmm: "+text);
                    }
                }

            }
        };
    }

    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }


    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                tButton.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

       // String text = "Mein kis jaiga hoon";

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
