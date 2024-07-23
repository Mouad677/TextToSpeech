package com.example.texttospeach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText ed1;
    LinearLayout audio_container;
    LinearLayout line_progressor;

    ImageView img_bt;
    Button bt_switch, bt_save;
    TextToSpeech txt_speech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = findViewById(R.id.ed_txt);
        audio_container = findViewById(R.id.audio_container);
        img_bt = findViewById(R.id.img_play);
        bt_switch = findViewById(R.id.bt_switch);
        bt_save = findViewById(R.id.bt_save);

        txt_speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    txt_speech.setLanguage(Locale.US);
                }
            }
        });

        bt_switch.setOnClickListener((View view) ->{
            String txt = ed1.getText().toString();
            if(!txt.isEmpty()){
                audio_container.setVisibility(View.VISIBLE);
                Toast.makeText(this, "The text was converted to speech!", Toast.LENGTH_SHORT).show();
            }

        });



        img_bt.setOnClickListener((View view) ->{
            txt_speech.speak(ed1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        });


    }
}