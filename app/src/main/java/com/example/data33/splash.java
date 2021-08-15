package com.example.data33;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import java.util.Timer;
import java.util.TimerTask;

public class splash extends AppCompatActivity {
    static int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = (ImageView) findViewById(R.id.logo);

        Glide.with(this).load(R.drawable.logo).into(imageView);

        Timer timer = new Timer();
        TimerTask TT = new TimerTask(){
            @Override
            public void run() {
                counter++;
                if(counter==25) {
                    Intent intent = new Intent(splash.this,LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.schedule(TT,0,100);
    }
}