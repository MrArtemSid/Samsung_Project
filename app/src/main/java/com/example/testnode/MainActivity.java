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
    private LinkedList<Node> nodes = new LinkedList<>();
    private Level[] levels;
    private final int W = 5;
    private final int H = 5;
    private final int NUMBER_OF_LEVELS = 3;
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    void init(){
        gridLayout = findViewById(R.id.grid_activity);
        gridLayout.setColumnCount(H);
        createLevels();
        showGraph();
    }
    boolean[] used = new boolean[200];
    boolean isD = false;

    void dfs(int v, int finish){
        if(v == finish){
            isD = true;
        }
        used[v] = true;
        for(int u : matrix.get(v)){
            if(!used[u]){
                dfs(u,finish);
            }
        }
    }
    void changeColor(int index, int color){
        nodesImages.get(index).setColorFilter(color,PorterDuff.Mode.SRC_IN);
    }

    void checkConnect(int i){
        boolean change = false;
            try{
                if(nodes.get(i).compareConnect(nodes.get(i - 1), Directions.LEFT) && i % W != 0){
                    changeColor(i - 1,Color.BLUE);
                    if(!used[i - 1]) {
                        used[i - 1] = true;
                        checkConnect(i - 1);
                    }
                    change = true;
                }
            }catch (Exception e){}
            try{
                if(nodes.get(i).compareConnect(nodes.get(i + 1), Directions.RIGHT) && (i + 1) % W != 0){
                    change = true;
                    changeColor(i + 1,Color.BLUE);
                    if(!used[i + 1]) {
                        used[i + 1] = true;
                        checkConnect(i + 1);
                    }
                }
            }catch (Exception e){}
            try{
                if(nodes.get(i).compareConnect(nodes.get(i + W), Directions.DOWN)){
                    change = true;
                    changeColor(i + W,Color.BLUE);
                    if(!used[i + W]) {
                        used[i + W] = true;
                        checkConnect(i + W);
                    }
                }
            }catch (Exception e){}
            try{
                if(nodes.get(i).compareConnect(nodes.get(i - W), Directions.UP)) {
                    change = true;
                    changeColor(i - W,Color.BLUE);
                    if(!used[i - W]) {
                        used[i - W] = true;
                        checkConnect(i - W);
                    }
                }
            }catch (Exception e){}
            if(change){
                changeColor(i,Color.BLUE);
            }
    }


    boolean existW(int pos, int border, ArrayList<Integer> number){
        return pos >= 0 && pos < border && number.get(pos) != 0;
    }
    boolean existH(int pos, int border, ArrayList<Integer> number){
        return pos >= 0 && pos < border && number.get(pos%W) != 0;
    }

    void createLevels(){
        levels = new Level[NUMBER_OF_LEVELS];
        for(int i = 0; i < NUMBER_OF_LEVELS; i++){
            levels[i] = new Level(i + 1);
        }
        int start = 3;
        int finish = 23;
            for (int currLvl = 0; currLvl < NUMBER_OF_LEVELS; currLvl++) {
                Integer cnt = 0;
                do {
                    createRandomLevel(levels[currLvl]);
                    matrix.clear();
                    for (int i = 0; i < H * W; i++) {
                        matrix.add(new ArrayList<>());
                        int pos_graph = Math.min(W - 1, i / W);
                        if (levels[currLvl].getGraph(pos_graph).get(i / H) == 0) {
                            continue;
                        }
                        if (existW(i - 1, W, levels[currLvl].getGraph(pos_graph))) {
                            matrix.get(i).add(i - 1);
                        }
                        if (existW(i + 1, W, levels[currLvl].getGraph(pos_graph))) {
                            matrix.get(i).add(i + 1);
                        }
                        if (existH(i + W, W*H, levels[currLvl].getGraph(Math.min(W - 1, i / W + 1)))) {
                            matrix.get(i).add(i + W);
                        }
                        if (existH(i - W, W*H, levels[currLvl].getGraph(Math.max(0, i / W - 1)))) {
                            matrix.get(i).add(i - W);
                        }
                    }
                    Log.d("attempt",cnt.toString());
                    cnt++;
                    used = new boolean[200];
                    dfs(start,finish);
                    if(levels[currLvl].getGraph().get(0).get(start) == 0){
                        isD = false;
                    }
                }while(!isD);
            }

    }
    void createRandomLevel(Level lvl){
        Random random = new Random();
        lvl.clearGraph();
        for (int i = 0; i < W; i++) {
            lvl.setGraph();
            for (int j = 0; j < H; j++) {
                lvl.getGraph(i).add(random.nextInt(4));
            }
        }
    }


    void showGraph(){
        int currLvl = 0;
        for(int i = 0; i < W; i++){
            for(int j = 0; j < H; j++){
                nodesImages.add(new ImageView(this));
                if(!levels[currLvl].isGraphEmpty(i)){
                    switch (levels[currLvl].getNumber(i, j)){
                        case 1:
                            nodes.add(new LineNode());
                            break;
                        case 2:
                            nodes.add(new AngleNode());
                            break;
                        case 3:
                            nodes.add(new TriAngleNode());
                            break;
                        default:
                            nodes.add(new Node());
                    }
                    nodesImages.getLast().setImageResource(nodes.getLast().getResId());
                    nodesImages.getLast().setOnClickListener(this);
                }
                nodesImages.getLast().setId(nodesImages.size() - 1);
                gridLayout.addView(nodesImages.getLast());
            }
        }
        connect();
    }
    void connect(){
        checkConnect(3);
        for(int i = 0; i < W * H; i++){
            if(!used[i]){
                changeColor(i,Color.BLACK);
            }
        }
    }
    @Override
    public void onClick(View v) {
        ImageView obj = (ImageView) v;
        obj.setRotation((obj.getRotation() + 90) % 360);
        nodes.get(v.getId()).changeDirection();
        used = new boolean[200];
        connect();
    }
}
