package com.example.testnode;

public enum Directions {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);
    public int number;
    Directions(int number){
        this.number = number;
    }
}
