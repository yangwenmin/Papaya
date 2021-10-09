package com.papaya.test.gsydemo.simple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.papaya.R;

import androidx.appcompat.app.AppCompatActivity;


public class SimpleActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);
        Button simpleDetail1 = findViewById(R.id.simple_detail_1);
        Button simpleDetail2 = findViewById(R.id.simple_detail_2);
        Button simpleList1 = findViewById(R.id.simple_list_1);
        Button simpleList2 = findViewById(R.id.simple_list_2);
        Button simplePlayer = findViewById(R.id.simple_player);

        simpleDetail1.setOnClickListener(this);
        simpleDetail2.setOnClickListener(this);
        simpleList1.setOnClickListener(this);
        simpleList2.setOnClickListener(this);
        simplePlayer.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.simple_player:
                startActivity(new Intent(this, SimplePlayer.class));
                break;
            case R.id.simple_list_1:
                startActivity(new Intent(this, SimpleListVideoActivityMode1.class));
                break;
            case R.id.simple_list_2:
                startActivity(new Intent(this, SimpleListVideoActivityMode2.class));
                break;
            case R.id.simple_detail_1:
                startActivity(new Intent(this, SimpleDetailActivityMode1.class));
                break;
            case R.id.simple_detail_2:
                startActivity(new Intent(this, SimpleDetailActivityMode2.class));
                break;
        }
    }
}
