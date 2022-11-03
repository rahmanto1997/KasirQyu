package com.otnam.kasirqiu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView splashImage;
    Animation firstAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        splashImage=findViewById(R.id.image_logo);
        firstAnimation= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        splashImage.startAnimation(firstAnimation);

        Thread thread= new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        };
        thread.start();
    }
}