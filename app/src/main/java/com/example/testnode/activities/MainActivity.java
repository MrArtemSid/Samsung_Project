package com.example.testnode.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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
import android.widget.SeekBar;
import android.widget.TextView;
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

    private Handler mHandler;
    private Runnable mRunnable;
    private static final long TIMEOUT_MILLISECONDS = 25000;
    private MediaPlayer mMediaPlayer;
    private int currWaitSong = 0;
    private int diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        timer_create();
    }

    void timer_create() {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wait1);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                changeMediaPlayerSound();
                mMediaPlayer.start();
                mHandler.postDelayed(mRunnable, TIMEOUT_MILLISECONDS);
            }
        };
        mHandler.postDelayed(mRunnable, TIMEOUT_MILLISECONDS);
    }
    void timer_kill() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private void changeMediaPlayerSound() {
        timer_kill();
        int currWait;

        if (currWaitSong == 0) {
            currWait = R.raw.wait1;
        } else
        {
            currWait = R.raw.wait2;
        }

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), currWait);
        currWaitSong = (currWaitSong + 1) % 2;
    }

    private void init(){
        Random random = new Random();
        int newSize = random.nextInt(7) + 6;
        diff = getIntent().getIntExtra("difficulty",1);
        switch (diff){
            case 0:
                newSize = (random.nextInt(3) + 4);
                break;
            case 1:
                newSize = (random.nextInt(3) + 7);
                break;
            case 2:
                newSize = (random.nextInt(3) + 10);
                break;
            case 3:
                newSize = random.nextInt(9) + 4;
                break;

        }

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
        Random random = new Random();
        for(Node node : game.getNodes()){
            int rr = random.nextInt(4);
            nodesImages.add(new ImageView(this));
            nodesImages.getLast().setSoundEffectsEnabled(false);
            nodesImages.getLast().setImageResource(node.getResId());
            nodesImages.getLast().setOnClickListener(this);
            nodesImages.getLast().setId(nodesImages.size() - 1);
            nodesImages.getLast().setLayoutParams(new GridView.LayoutParams(display.getWidth() / W,display.getWidth() / H));
            if (nodesImages.getLast().getId() == game.getStart() || nodesImages.getLast().getId() == game.getFinish())
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.gold_border));
            else
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.normal_border));
            for(int i = 0; i < rr; i++){
                node.changeDirection();
            }
            nodesImages.getLast().setRotation(nodesImages.getLast().getRotation() + 90 * rr);
            gridLayout.addView(nodesImages.getLast());

        }
        connect();
    }
    private void changeLevel() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("difficulty",diff);
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
            ((TextView)win.findViewById(R.id.winText)).setText(String.format("Победа\n+%d",game.getLevel().getH()));

            AlertDialog winDialog = new AlertDialog.Builder(this)
                    //.setCancelable(false)
                    .setView(win)
                    //.setPositiveButton(android.R.string.ok, null)
                    .create();

            winDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.corner));

            winDialog.show();

            int id_music = 0;
            if ((user.getPoints() / 10) % 20 == 0 && user.getPoints() >= 10 * 20) {
                id_music = R.raw.many_wins;
                TextView winText = win.findViewById(R.id.winText);
                winText.setText(String.format("А ты хорош!\n+%d",game.getLevel().getH()));
            }
            else {
                id_music = R.raw.anime_win;
            }
            timer_kill();
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), id_music);
            mMediaPlayer.start();

//            winDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    user.addPoint();
//                    changeLevel();
//                    if(user.isCheck())
//                        DB.updatePoint(user);
//                    winDialog.dismiss();
//                }
//            });
            winDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    user.addPoint(game.getLevel().getH());
                    changeLevel();
                    mMediaPlayer.stop();
                    if(user.isCheck())
                        DB.updatePoint(user);
                    timer_kill();
                    winDialog.dismiss();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        timer_kill();
        if(v.getId() == R.id.btnBack) {
            finish();
            saveGame.saveUser(user);

        }
        else {
            v.setRotation((v.getRotation() + 90) % 360);
            game.getNodes().get(v.getId()).changeDirection();
            used = new boolean[W * H + 1];

            timer_kill();
            mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.click1);
            mMediaPlayer.start();

            timer_create();
            connect();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        saveGame.saveUser(user);
        timer_kill();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        saveGame.saveUser(user);
        timer_kill();
    }
}
