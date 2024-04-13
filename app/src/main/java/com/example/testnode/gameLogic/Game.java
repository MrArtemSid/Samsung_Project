package com.example.testnode.gameLogic;

import com.example.testnode.nodes.AngleNode;
import com.example.testnode.nodes.LineNode;
import com.example.testnode.nodes.Node;
import com.example.testnode.nodes.TriAngleNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Game {
    boolean isD = false;
    boolean[] used;
    private Level level;
    private ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
    private LinkedList<Node> nodes = new LinkedList<>();
    int start = 0;
    int finish = 1;

    public Game(int W, int H){
        level = new Level(W,H);
    }
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
        return pos >= 0 && pos < border && number.get(pos % level.getW()) != 0;
    }
    public void createLevels(){
        int H = level.getH();
        int W = level.getW();
        do {
            createRandomLevel();
            matrix.clear();
            for (int i = 0; i < H * W; i++) {
                matrix.add(new ArrayList<>());
                int pos_graph = Math.min(W - 1, i / W);
                if (level.getGraph(pos_graph).get(i % H) == 0) {
                    continue;
                }
                if (existW(i - 1, W, level.getGraph(pos_graph))) {
                    matrix.get(i).add(i - 1);
                }
                if (existW(i + 1, W, level.getGraph(pos_graph))) {
                    matrix.get(i).add(i + 1);
                }
                if (existH(i + W, W*H, level.getGraph(Math.min(W - 1, i / W + 1)))) {
                    matrix.get(i).add(i + W);
                }
                if (existH(i - W, W*H, level.getGraph(Math.max(0, i / W - 1)))) {
                    matrix.get(i).add(i - W);
                }
            }
            used = new boolean[W * H + 1];
            dfs(start,finish);
            if(level.getGraph().get(0).get(start) == 0){
                isD = false;
            }
        }while(!isD);

    }
    void createRandomLevel(){
        Random random = new Random();
        level.clearGraph();
        for (int i = 0; i < level.getW(); i++) {
            level.setGraph();
            for (int j = 0; j < level.getH(); j++) {
                level.getGraph(i).add(random.nextInt(3) % 3 + 1);
            }
        }
    }
    public void createNodesMatrix(){
        for(int i = 0; i < level.getW(); i++){
            for(int j = 0; j < level.getH(); j++){
                if(!level.isGraphEmpty(i)){
                    switch (level.getNumber(i, j)){
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
                            //nodes.add(new Node());
                    }
                }
            }
        }
    }
    public Level getLevel() {
        return level;
    }
    public ArrayList<ArrayList<Integer>> getMatrix() {
        return matrix;
    }
    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public int getStart() {
        return start;
    }

    public int getFinish() {
        return finish;
    }
}
