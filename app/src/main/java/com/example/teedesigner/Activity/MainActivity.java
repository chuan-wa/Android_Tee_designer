package com.example.teedesigner.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.teedesigner.R;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences imagesURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button goDeisgn=findViewById(R.id.button_go_design);
        Button goData=findViewById(R.id.button_go_data);
        goDeisgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                startActivity(intent);
            }
        });
        goData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });

    }

}