package com.example.testnode.nodes;

import com.example.testnode.Directions;
import com.example.testnode.R;

public class Node {
    int[] directions;
    int resId;
    int color;
    public Node(){
        this.directions = new int[]{0,0,0,0};
        this.resId = R.mipmap.blank;
        this.color = 0;
    }
    public Node(int[] directions, int resId, int color){
        this.directions = directions;
        this.resId = resId;
        this.color = color;
    }

    public void changeDirection(){
        int[] changed = new int[4];
        for(int i = 0; i < directions.length; i++){
            changed[(i + 1) % 4] = directions[i];
        }
        directions = changed;
    }

    public boolean compareConnect(Node node, Directions direction){
        switch (direction){
            case UP:
                return this.directions[0] == node.getDirections()[2] && this.directions[0] == 1;
            case RIGHT:
                return this.directions[1] == node.getDirections()[3] && this.directions[1] == 1;
            case DOWN:
                return this.directions[2] == node.getDirections()[0] && this.directions[2] == 1;
            case LEFT:
                return this.directions[3] == node.getDirections()[1] && this.directions[3] == 1;
            default:
                return false;
        }
    }

    public int[] getDirections() {
        return directions;
    }

    public void setDirections(int[] directions) {
        this.directions = directions;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
