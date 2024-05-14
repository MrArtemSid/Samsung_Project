package com.example.testnode;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private Long points;
    private boolean check;
    public User(){
        name = "";
        points = 0L;
    }
    public User(String name, Long points, boolean check){
        this.name = name;
        this.points = points;
        this.check = check;
    }
    @Override
    public String toString(){
        return name +"\n"+points.toString();
    }

    public String getName() {
        return name;
    }
    public Long getPoints() {
        return points;
    }
    public void addPoint(int size){
        this.points += size;
    }
    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
