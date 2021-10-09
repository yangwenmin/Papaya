package com.papaya.test.gsydemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.papaya.R;
import com.papaya.test.gsydemo.simple.SimpleActivity;

import androidx.appcompat.app.AppCompatActivity;

public class GsyDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsydemo_main);


        Button button = findViewById(R.id.video_btn_next);
        Button land = findViewById(R.id.video_btn_land);
        Button simple = findViewById(R.id.video_btn_simple);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GsyDemoActivity.this, PlayerActivity.class));
            }
        });
        land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GsyDemoActivity.this, LandActivity.class));
            }
        });
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GsyDemoActivity.this, SimpleActivity.class));
            }
        });


    }
}
