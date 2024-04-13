package com.example.testnode.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testnode.gameLogic.Directions;
import com.example.testnode.gameLogic.Game;
import com.example.testnode.R;
import com.example.testnode.nodes.Node;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    private LinkedList<ImageView> nodesImages = new LinkedList<>();
    private Game game;
    private int W;
    private int H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
        this.W = getIntent().getIntExtra("W",5);
        this.H = getIntent().getIntExtra("H",5);
        gridLayout = findViewById(R.id.grid_activity);
        gridLayout.setColumnCount(H);
        game = new Game(W,H);
        game.createLevels();
        game.createNodesMatrix();
        showGraph();
    }
    boolean[] used;

    void changeColor(int index, int color){
        nodesImages.get(index).setColorFilter(color,PorterDuff.Mode.SRC_IN);
    }
    void checkConnect(int curr){
        boolean change = false;

        int UP = curr - W;
        int RIGHT = curr + 1 ;
        int DOWN = curr + W;
        int LEFT = curr - 1;

        if(LEFT >= 0 &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(LEFT), Directions.LEFT) && curr % W != 0){
            changeColor(LEFT,Color.BLUE);
            if(!used[LEFT]) {
                used[LEFT] = true;
                checkConnect(LEFT);
            }
            change = true;
        }
        if(RIGHT < W*H &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(RIGHT), Directions.RIGHT) && RIGHT % W != 0){
            change = true;
            changeColor(RIGHT,Color.BLUE);
            if(!used[RIGHT]) {
                used[RIGHT] = true;
                checkConnect(RIGHT);
            }
        }
        if(DOWN < W*H &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(DOWN), Directions.DOWN)){
            change = true;
            changeColor(DOWN,Color.BLUE);
            if(!used[DOWN]) {
                used[DOWN] = true;
                checkConnect(DOWN);
            }
        }
        if(UP >= 0 &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(UP), Directions.UP)) {
            change = true;
            changeColor(UP,Color.BLUE);
            if(!used[UP]) {
                used[UP] = true;
                checkConnect(UP);
            }
        }

        if(change){
            changeColor(curr,Color.BLUE);
        }
    }

    void showGraph(){
        Display display = getWindowManager().getDefaultDisplay();
        for(Node node : game.getNodes()){
            nodesImages.add(new ImageView(this));
            nodesImages.getLast().setImageResource(node.getResId());
            nodesImages.getLast().setOnClickListener(this);
            nodesImages.getLast().setId(nodesImages.size() - 1);
            nodesImages.getLast().setLayoutParams(new GridView.LayoutParams(display.getWidth() / W,display.getWidth() / H));
            gridLayout.addView(nodesImages.getLast());
        }
        connect();
    }
    void connect(){
        used = new boolean[W*H + 1];
        checkConnect(game.getStart());
        for(int i = 0; i < W*H; i++){
            if(!used[i]){
                changeColor(i,Color.BLACK);
            }
        }
    }
    @Override
    public void onClick(View v) {
        v.setRotation((v.getRotation()+90) % 360);
        game.getNodes().get(v.getId()).changeDirection();
        used = new boolean[W*H+1];
        connect();
    }
}
