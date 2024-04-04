package com.example.testnode.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.testnode.DB;
import com.example.testnode.R;

public class MainMenu extends AppCompatActivity {

    Button btnPlay, btnLeader, btnQuit, btnSend;
    EditText W, H, name, point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }
    private void init(){
        btnPlay = findViewById(R.id.btnPlay);
        btnLeader = findViewById(R.id.btnLeaderBoard);
        btnQuit = findViewById(R.id.btnQuit);
        btnSend = findViewById(R.id.btnSend);
        name = findViewById(R.id.name);
        point = findViewById(R.id.point);

        W = findViewById(R.id.w);
        H = findViewById(R.id.h);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if(W.getText().toString().length() != 0 && H.getText().toString().length() != 0) {
                    intent.putExtra("W", Integer.parseInt(W.getText().toString()));
                    intent.putExtra("H", Integer.parseInt(H.getText().toString()));
                    startActivity(intent);
                }
            }
        });
        btnLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderBoard.class);
                startActivity(intent);
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DB.addUser(name.getText().toString());
                    }
                }).start();
            }
        });

    }
}