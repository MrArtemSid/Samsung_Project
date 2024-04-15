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
    private final String CHECK = "check";

    public SaveGame(Context context){
        save = context.getSharedPreferences("com.example.testnode", MODE_PRIVATE);
    }
    public boolean isFirst(){
        if(save.getBoolean("first",true) && save.getString(USERNAME, "").equals("")){
            save.edit().putBoolean("first",false).apply();
            return true;
        }else{
            return false;
        }
    }
    public void saveUser(User user){
        save.edit().putLong(POINT,user.getPoints()).apply();
        save.edit().putString(USERNAME,user.getName()).apply();
        save.edit().putBoolean(CHECK,user.isCheck()).apply();
    }
    public User getUser(){
        return new User(save.getString(USERNAME, ""), save.getLong(POINT,0L),save.getBoolean(CHECK,false));
    }
}
