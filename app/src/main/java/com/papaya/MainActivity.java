package com.papaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kylin.core.app.Kylin;
import com.papaya.gsydemo.GsyDemoActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.main_btn_gsy);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GsyDemoActivity.class));

                Toast.makeText(Kylin.getApplicationContext(),"13",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
