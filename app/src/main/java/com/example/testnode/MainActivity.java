package com.example.testnode;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GridLayout gridLayout;
    private LinkedList<ImageView> nodesImages = new LinkedList<>();
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
        int start = 0;
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
        int currLvl = 1;
        for(int i = 0; i < W; i++){
            for(int j = 0; j < H; j++){
                nodesImages.add(new ImageView(this));
                if(!levels[currLvl].isGraphEmpty(i)){
                    switch (levels[currLvl].getNumber(i,j)){
                        case 1:
                            nodesImages.getLast().setImageResource(R.mipmap.line);
                            break;
                        case 2:
                            nodesImages.getLast().setImageResource(R.mipmap.angle);
                            break;
                        case 3:
                            nodesImages.getLast().setImageResource(R.mipmap.angle3);
                            break;
                        default:
                            nodesImages.getLast().setImageResource(R.mipmap.blank);
                    }
                    nodesImages.getLast().setOnClickListener(this);
                }
                nodesImages.getLast().setId(nodesImages.size());
                gridLayout.addView(nodesImages.getLast());
            }
        }
    }
    @Override
    public void onClick(View v) {
        ImageView obj = (ImageView) v;
        obj.setRotation((obj.getRotation() + 90) % 360);

        int id = obj.getId();
        int [] neighbours = {-1, -1, -1, -1}; // up, down, left, right

        int id_i = id / W;
        int id_j = id % W;
        if (id_i > 1) {
            neighbours[0] = id - W;
        }
        if (id_i < W - 1) {
            neighbours[1] = id + W;
        }
        if (id_j > 0) {
            neighbours[2] = id - 1;
        }
        if (id_j < W - 1) {
            neighbours[3] = id + 1;
        }
        obj.setColorFilter(Color.BLACK);
        for (int i = 0; i < 4; ++i) {
            int neighbour = neighbours[i];
            if (neighbours[i] == -1)
                continue;
            if (gridLayout.getChildAt(neighbours[i]).getRotation() != obj.getRotation()) {
                //matrix.get(neighbours[i]).removeIf(x -> (x == id));
                //matrix.get(id).removeIf(x -> (x == neighbour));
            } else {
                //matrix.get(neighbours[i]).add(id);
                //matrix.get(id).add(neighbours[i]);
                obj.setColorFilter(Color.BLUE);
            }
        }
    }
}