package com.example.testnode;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testnode.nodes.AngleNode;
import com.example.testnode.nodes.LineNode;
import com.example.testnode.nodes.Node;
import com.example.testnode.nodes.TriAngleNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    private LinkedList<ImageView> nodesImages = new LinkedList<>();
    private Game game;

    //INTENT///////////////////////
    private final int W = 5;
    private final int H = 5;
    ///////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
        gridLayout = findViewById(R.id.grid_activity);
        gridLayout.setColumnCount(H);
        game = new Game(W, H);
        game.createLevels();
        game.createNodesMatrix();
        showGraph();
    }
    boolean[] used = new boolean[W * H + 1];

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
        if(RIGHT < W * H &&
                game.getNodes().get(curr).compareConnect(game.getNodes().get(RIGHT), Directions.RIGHT) && RIGHT % W != 0){
            change = true;
            changeColor(RIGHT,Color.BLUE);
            if(!used[RIGHT]) {
                used[RIGHT] = true;
                checkConnect(RIGHT);
            }
        }
        if(DOWN < W * H &&
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
        for(Node node : game.getNodes()){
            nodesImages.add(new ImageView(this));
            nodesImages.getLast().setImageResource(node.getResId());
            nodesImages.getLast().setOnClickListener(this);
            nodesImages.getLast().setId(nodesImages.size() - 1);
            gridLayout.addView(nodesImages.getLast());
        }
        connect();
    }
    void connect(){
        checkConnect(3);
        for(int i = 0; i < W * H; i++){
            if(!used[i]){
                changeColor(i, Color.BLACK);
            }
        }
    }
    @Override
    public void onClick(View v) {
        v.setRotation((v.getRotation() + 90) % 360);
        game.getNodes().get(v.getId()).changeDirection();
        used = new boolean[W * H + 1];
        connect();
    }
}
