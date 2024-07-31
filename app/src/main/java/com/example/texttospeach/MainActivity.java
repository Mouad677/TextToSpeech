package com.example.texttospeach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText ed1;
    LinearLayout audio_container;
    LinearLayout line_progressor;
    ImageView img_bt;
    Button bt_switch, bt_save;
    TextToSpeech txt_speech;
    TextView txt_audioLocation;
    TextView txt_github;
    private static final int REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = findViewById(R.id.ed_txt);
        audio_container = findViewById(R.id.audio_container);
        img_bt = findViewById(R.id.img_play);
        bt_switch = findViewById(R.id.bt_switch);
        bt_save = findViewById(R.id.bt_save);
        txt_audioLocation = findViewById(R.id.txt_audiolocation);
        txt_github = findViewById(R.id.txt_github);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} ,REQUEST_CODE);
        }

        txt_speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    txt_speech.setLanguage(Locale.ENGLISH);
                }
            }
        });



        bt_switch.setOnClickListener((View view) ->{
            String txt = ed1.getText().toString();
            if(!txt.isEmpty()){
                audio_container.setVisibility(View.VISIBLE);

                Toast.makeText(this, "The text was converted to speech!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Nothing to switched!", Toast.LENGTH_SHORT).show();
            }
        });

        img_bt.setOnClickListener(view -> {
            txt_speech.speak(ed1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        });

        bt_save.setOnClickListener(view -> {
            saveAudio(ed1.getText().toString());
        });

        txt_github.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://github.com/Mouad677");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

    }

    private void saveAudio(String txt){
        if(txt.isEmpty()){
            Toast.makeText(this, "Nothing to save!", Toast.LENGTH_SHORT).show();
            return;
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String fileName = "audio_output_" +timeStamp + ".wav";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/TextToSpeech/";;
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
            if(!file.mkdirs()){
                Toast.makeText(this, "Directory didn't created !", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File f = new File(file, fileName);
        HashMap<String, String> hashRende = new HashMap<>();
        hashRende.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, fileName);
        int speechStatus = txt_speech.synthesizeToFile(txt, null, f, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        if(speechStatus == TextToSpeech.SUCCESS){
            Toast.makeText(this, "Audio saved", Toast.LENGTH_SHORT).show();
            txt_audioLocation.setText("Audio Location : " + f.getAbsolutePath());
        }else{
            Toast.makeText(this, "Audio didn't saved !", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error synthesized to file: " +speechStatus);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted !", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission denied !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(txt_speech != null){
            txt_speech.stop();
            txt_speech.shutdown();
        }
        super.onDestroy();
    }






}