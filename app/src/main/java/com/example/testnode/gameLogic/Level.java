package com.example.testnode.gameLogic;

import java.util.ArrayList;

public class Level {
    private int W;
    private int H;
    private ArrayList<ArrayList<Integer>> graph;

    public Level(int W, int H) {
        graph = new ArrayList<>();
        this.W = W;
        this.H = H;
    }

    public void clearGraph(){
        graph.clear();
    }

    public void setGraph(){
        graph.add(new ArrayList<>());
    }

    public ArrayList<Integer> getGraph(int pos){
        return graph.get(pos);
    }

    public int getNumber(int pos1, int pos2){
        return graph.get(pos1).get(pos2);
    }
    public boolean isGraphEmpty(){
        return graph.isEmpty();
    }

    public boolean isGraphEmpty(int pos){
        return graph.get(pos).isEmpty();
    }
    public ArrayList<ArrayList<Integer>> getGraph() {
        return graph;
    }

    public int getW() {
        return W;
    }

    public int getH() {
        return H;
    }

}
