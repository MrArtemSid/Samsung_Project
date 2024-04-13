package com.example.testnode.gameLogic;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.testnode.User;

import java.io.Serializable;

public class SaveGame {
    private final SharedPreferences save;
    private final String USERNAME = "username";
    private final String POINT = "point";

    public SaveGame(Context context){
        save = context.getSharedPreferences("com.example.testnode", MODE_PRIVATE);
    }
    public boolean isFirst(){
        if(save.getBoolean("first",true)){
            save.edit().putBoolean("first",false).apply();
            return true;
        }else{
            return false;
        }
    }
    public void saveUser(User user){
        save.edit().putLong("point",user.getPoints()).apply();
        save.edit().putString("username",user.getName()).apply();
    }
    public User getUser(){
        return new User(save.getString(USERNAME, "def"), save.getLong(POINT,0L));
    }
}
