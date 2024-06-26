package com.example.testnode.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testnode.DB;
import com.example.testnode.R;
import com.example.testnode.User;
import com.example.testnode.gameLogic.SaveGame;
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

    private ListView listView;
    private SaveGame saveGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        init();
    }
    private void init(){
        listView = findViewById(R.id.listTest);
        Button btnBack = findViewById(R.id.btnBack);
        saveGame = new SaveGame(getApplicationContext());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                showAll();
            }
        }).start();
    }
    private void showAll(){
        List<User> users = DB.getAll();

        if(users == null)
            return;
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                R.layout.list_item_1, users);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.list_item_1, users) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView textView = view.findViewById(android.R.id.text1);
                        User user = users.get(position);

                        if (user.getName().equals(saveGame.getUser().getName())) {
                            textView.setTextColor(Color.rgb(204,255,255));
                            textView.setText(String.format("Ваш результат\n%d", user.getPoints()));
                        } else {
                            textView.setTextColor(Color.WHITE);
                        }
                        return view;
                    }
                };
                listView.setAdapter(adapter);
            }
        });
    }
    public <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(new Gson().fromJson(s, clazz));
    }
}