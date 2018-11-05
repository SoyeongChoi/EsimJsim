package com.example.chm31.esimjsim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //iv= (ImageView) findViewById(R.id.iv);
        //Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        //iv.startAnimation(myanim);


        //set gifimageview resource

        //wait for 3 seconds and start activity main
        final Intent i = new Intent(this, LoginActivity.class);
        Thread timer = new Thread(){
            public void run () {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}