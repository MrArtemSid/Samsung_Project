package com.example.testnode.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.testnode.R;
import com.example.testnode.User;
import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LeaderBoard extends AppCompatActivity {

    private Button back;
    ListView listView;
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        listView = findViewById(R.id.listTest);
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btnBack)
                    finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                getDb();
            }
        }).start();
    }
    private void getDb(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://yeti-new-physically.ngrok-free.app:8080/getAll").build();
        try (Response response = client.newCall(request).execute()) {
            users = stringToArray(response.body().string(), User[].class);
            ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                    R.layout.list_item_1, users);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(adapter);
                }
            });
        } catch (IOException e) {
            Log.d("MYRESPONSE",e.toString());
        }
    }
    public <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(new Gson().fromJson(s, clazz));
    }
}
