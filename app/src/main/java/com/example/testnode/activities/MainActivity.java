package com.example.testnode.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.testnode.DB;
import com.example.testnode.User;
import com.example.testnode.gameLogic.Directions;
import com.example.testnode.gameLogic.Game;
import com.example.testnode.R;
import com.example.testnode.gameLogic.SaveGame;
import com.example.testnode.nodes.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button back;
    private GridLayout gridLayout;
    private final LinkedList<ImageView> nodesImages = new LinkedList<>();
    private Game game;
    private int W;
    private int H;
    private final Directions UP = Directions.UP;
    private final Directions RIGHT = Directions.RIGHT;
    private final Directions DOWN = Directions.DOWN;
    private final Directions LEFT = Directions.LEFT;
    private boolean[] used;
    private User user;
    private SaveGame saveGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){

        Random random = new Random();
        int newSize = random.nextInt(5) + 5;
        this.W = newSize;
        this.H = newSize;

        this.user = (User) getIntent().getSerializableExtra("user");
        saveGame = new SaveGame(getApplicationContext());
        back = findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        gridLayout = findViewById(R.id.grid_activity);
        gridLayout.setColumnCount(H);
        game = new Game(W,H);
        game.newGame();
        showGraph();
    }

    private void changeColor(int index, int color){
        nodesImages.get(index).setColorFilter(color,PorterDuff.Mode.SRC_IN);
    }
    private void checkConnect(int curr){
        boolean change = false;

        int up = curr - W;
        int right = curr + 1 ;
        int down = curr + W;
        int left = curr - 1;

        if(left >= 0 &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(left), LEFT) && curr % W != 0){
            changeColor(left,Color.BLUE);
            if(!used[left]) {
                used[left] = true;
                checkConnect(left);
            }
            change = true;
        }
        if(right < W*H &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(right), RIGHT) && right % W != 0){
            change = true;
            changeColor(right,Color.BLUE);
            if(!used[right]) {
                used[right] = true;
                checkConnect(right);
            }
        }
        if(down < W*H &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(down), DOWN)){
            change = true;
            changeColor(down,Color.BLUE);
            if(!used[down]) {
                used[down] = true;
                checkConnect(down);
            }
        }
        if(up >= 0 &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(up), UP)) {
            change = true;
            changeColor(up,Color.BLUE);
            if(!used[up]) {
                used[up] = true;
                checkConnect(up);
            }
        }

        if(change){
            changeColor(curr,Color.BLUE);
        }
    }
    private void showGraph(){
        Display display = getWindowManager().getDefaultDisplay();
        nodesImages.clear();

        for(Node node : game.getNodes()){
            nodesImages.add(new ImageView(this));
            nodesImages.getLast().setImageResource(node.getResId());
            nodesImages.getLast().setOnClickListener(this);
            nodesImages.getLast().setId(nodesImages.size() - 1);
            nodesImages.getLast().setLayoutParams(new GridView.LayoutParams(display.getWidth() / W,display.getWidth() / H));
            if (nodesImages.getLast().getId() == game.getStart() || nodesImages.getLast().getId() == game.getFinish())
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.gold_border));
            else
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.normal_border));
            gridLayout.addView(nodesImages.getLast());

        }
        connect();
    }
    private void changeLevel() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

        finish();
    }
    private void connect(){
        used = new boolean[W*H + 1];
        checkConnect(game.getStart());
        for(int i = 0; i < W*H; i++){
            if(!used[i]){
                changeColor(i,Color.BLACK);
            }
        }
        changeColor(game.getStart(),Color.BLUE);
        changeColor(game.getFinish(),Color.GREEN);
        if(used[game.getStart()] && used[game.getFinish()]){

            LayoutInflater inflater = LayoutInflater.from(this);
            View win = inflater.inflate(R.layout.dialog_win,null);

            AlertDialog winDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setView(win)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();

            winDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.corner));
            winDialog.show();

            winDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.addPoint();
                    changeLevel();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DB.updatePoint(user);
                        }
                    }).start();
                    winDialog.dismiss();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnBack)
            finish();
        else {
            v.setRotation((v.getRotation() + 90) % 360);
            game.getNodes().get(v.getId()).changeDirection();
            used = new boolean[W * H + 1];
            connect();
        }
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
