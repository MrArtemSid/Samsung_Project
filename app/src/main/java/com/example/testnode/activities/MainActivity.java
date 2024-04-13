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
    private final Directions UP = Directions.UP;
    private final Directions RIGHT = Directions.RIGHT;
    private final Directions DOWN = Directions.DOWN;
    private final Directions LEFT = Directions.LEFT;

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

    void showGraph(){
        Display display = getWindowManager().getDefaultDisplay();
        for(Node node : game.getNodes()){
            nodesImages.add(new ImageView(this));
            nodesImages.getLast().setImageResource(node.getResId());
            nodesImages.getLast().setOnClickListener(this);
            nodesImages.getLast().setId(nodesImages.size() - 1);
            nodesImages.getLast().setLayoutParams(new GridView.LayoutParams(display.getWidth() / W,display.getWidth() / H));
            if (nodesImages.getLast().getId() == game.getStart() || nodesImages.getLast().getId() == game.getFinish())
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.gold_border));
            /*else
                nodesImages.getLast().setBackground(ContextCompat.getDrawable(this, R.drawable.normal_border));
            */
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
