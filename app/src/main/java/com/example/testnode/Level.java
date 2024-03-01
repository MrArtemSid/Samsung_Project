package com.example.testnode;

import java.util.ArrayList;

public class Level {
    private int numberLvl;
    private ArrayList<ArrayList<Integer>> graph;

    public Level(int numberLvl) {
        this.numberLvl = numberLvl;
        graph = new ArrayList<>();
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

    public int getNumberLvl() {
        return numberLvl;
    }

    public void setNumberLvl(int numberLvl) {
        this.numberLvl = numberLvl;
    }

    public ArrayList<ArrayList<Integer>> getGraph() {
        return graph;
    }

}
