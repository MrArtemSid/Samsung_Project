package com.example.testnode;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DB {
    public static void addUser(String name){
        OkHttpClient client = new OkHttpClient();
        String json = String.format("{\"name\":\"%s\",\"point\":%d}",name,0);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url("http://192.168.0.12:8080/addUser").post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.d("MYRESPONSE",e.toString());
        }
    }
    public static void updatePoint(String name, Long point){
        OkHttpClient client = new OkHttpClient();
        String json = String.format("{\"name\":\"%s\",\"point\":%d}",name,point);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url("http://192.168.0.12:8080/updatePoint").post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.d("MYRESPONSE",e.toString());
        }
    }
}
