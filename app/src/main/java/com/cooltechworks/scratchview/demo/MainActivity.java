package com.cooltechworks.scratchview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onTextViewDemoClick(View v) {
        startActivity(new Intent(this, DemoClothingActivity.class));
    }

    public void onImageViewDemoClick(View v) {
        startActivity(new Intent(this, CaptchaActivity.class));
    }

}
