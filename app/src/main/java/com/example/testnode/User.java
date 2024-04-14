package com.example.testnode;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private Long points;
    public User(){
        name = "defName";
        points = 0L;
    }
    public User(String name, Long points){
        this.name = name;
        this.points = points;
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
    public void addPoint(){
        this.points++;
    }
}
