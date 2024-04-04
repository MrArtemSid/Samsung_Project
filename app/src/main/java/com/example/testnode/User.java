package com.example.testnode;

public class User {
    String name;
    Long points;
    public User(){}
    public User(String name, Long points){
        this.name = name;
        this.points = points;
    }
    @Override
    public String toString(){
        return name +'\n'+points.toString();
    }

    public String getName() {
        return name;
    }

    public Long getPoints() {
        return points;
    }
}
