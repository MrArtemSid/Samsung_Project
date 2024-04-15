package com.example.testnode.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testnode.DB;
import com.example.testnode.R;
import com.example.testnode.User;
import com.example.testnode.gameLogic.SaveGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainMenu extends AppCompatActivity {

    private Button btnPlay, btnLeader, btnQuit;
    private User user;
    private SaveGame saveGame;
    private TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }
    private void init(){
        saveGame = new SaveGame(getApplicationContext());
        getSavedGame();
        btnPlay = findViewById(R.id.btnPlay);
        btnLeader = findViewById(R.id.btnLeaderBoard);
        btnQuit = findViewById(R.id.btnQuit);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setText(saveGame.getUser().getName());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        btnLeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!user.isCheck()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (DB.check(user.getName())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Имя уже занято", Toast.LENGTH_SHORT).show();
                                                createNewUser();
                                            }
                                        });
                                        return;
                                    }
                                    user.setCheck(true);
                                    saveGame.saveUser(user);
                                    DB.addUser(user);
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Отсутствует соединение с сервером", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LeaderBoard.class);
                        startActivity(intent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createNewUser(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View addUser = inflater.inflate(R.layout.dialog_create_user,null);

        AlertDialog createUserDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(addUser)
                .setPositiveButton(android.R.string.ok, null)
                .create();
        createUserDialog.show();
        createUserDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userName = createUserDialog.findViewById(R.id.userName);
                assert userName != null;
                if(userName.getText().toString().length() > 3){
                    user = new User(userName.getText().toString(),saveGame.getUser().getPoints(), saveGame.getUser().isCheck());
                    saveGame.saveUser(user);
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (DB.check(user.getName())) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Имя уже занято", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        return;
                                    }
                                    user.setCheck(true);
                                    saveGame.saveUser(user);
                                    DB.addUser(user);
                                    createUserDialog.dismiss();
                                }catch (Exception e){
                                    createUserDialog.dismiss();
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }catch (Exception e){
                        createUserDialog.dismiss();
                        e.printStackTrace();
                    }
                    tvUserName.setText(saveGame.getUser().getName());
                }else {
                    Toast.makeText(getApplicationContext(), "Имя не может быть короче 3 символов", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getSavedGame(){
        if(saveGame.isFirst()) {
            createNewUser();
            return;
        }
        try {
            user = saveGame.getUser();
            if(user.getName().length() <= 3)
                throw new Exception("Username is not Available");
        } catch (Exception e) {
            e.printStackTrace();
            createNewUser();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        user = saveGame.getUser();
    }
    @Override
    public void onPause() {
        super.onPause();
        saveGame.saveUser(user);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        saveGame.saveUser(user);
    }
}