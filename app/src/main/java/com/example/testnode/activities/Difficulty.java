package com.example.testnode.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testnode.R;
import com.example.testnode.User;

public class Difficulty extends AppCompatActivity implements View.OnClickListener {
    private Button back, easy, normal, hard, random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        init();
    }
    private void init(){
        back = findViewById(R.id.btnBack);
        easy = findViewById(R.id.easyBtn);
        normal = findViewById(R.id.normalBtn);
        hard = findViewById(R.id.hardBtn);
        random = findViewById(R.id.randomBtn);

        back.setOnClickListener(this);
        easy.setOnClickListener(this);
        normal.setOnClickListener(this);
        hard.setOnClickListener(this);
        random.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        if(v.getId() == R.id.btnBack){
            finish();
            return;
        }
        if(v.getId() == R.id.easyBtn){
            intent.putExtra("difficulty",0);
        }
        if(v.getId() == R.id.normalBtn){
            intent.putExtra("difficulty",1);
        }
        if(v.getId() == R.id.hardBtn){
            intent.putExtra("difficulty",2);
        }
        if(v.getId() == R.id.randomBtn){
            intent.putExtra("difficulty",3);
        }
        User user = (User) getIntent().getSerializableExtra("user");
        intent.putExtra("user",user);
        startActivity(intent);

        finish();
    }
}