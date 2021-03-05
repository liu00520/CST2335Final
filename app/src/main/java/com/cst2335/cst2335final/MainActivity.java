package com.cst2335.cst2335final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ******Creating OnclickListener that leads to soccer activity***************
        ImageView soccer=  findViewById(R.id.soccer);
        Intent goSoccer = new Intent(this, soccerActivity.class);
        soccer.setOnClickListener( click -> startActivity(goSoccer));
        // ******End of OnclickListener that leads to soccer activity***************


    }
}