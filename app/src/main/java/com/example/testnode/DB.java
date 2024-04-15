package com.example.testnode;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DB {
    private static final String URL = "https://yeti-new-physically.ngrok-free.app:8080";
    public static void addUser(User user){
        OkHttpClient client = new OkHttpClient();
        String json = String.format("{\"name\":\"%s\",\"point\":%d}",user.getName(),user.getPoints());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(URL+"/addUser").post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.d("MYRESPONSE",e.toString());
        }
    }
    public static void updatePoint(User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String json = String.format("{\"name\":\"%s\",\"point\":%d}",user.getName(),user.getPoints());
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Request request = new Request.Builder().url(URL+"/updatePoints").post(requestBody).build();
                try {
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    Log.d("MYRESPONSE",e.toString());
                }
            }
        }).start();
    }

    public static boolean check(String name){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(String.format("%s/checkName?name=%s", URL, name)).build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string().equals("false");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> getAll(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL + "/getAll").build();
        try (Response response = client.newCall(request).execute()) {
            List<User> users = stringToArray(response.body().string(), User[].class);
            return users;
        } catch (IOException e) {
            Log.d("MYRESPONSE",e.toString());
        }
        return null;
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(new Gson().fromJson(s, clazz));
    }

}
