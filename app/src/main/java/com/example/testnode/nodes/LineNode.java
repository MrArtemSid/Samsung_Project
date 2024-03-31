package com.example.testnode.nodes;

import android.graphics.Color;

import com.example.testnode.R;
import com.example.testnode.nodes.Node;

public class LineNode extends Node {
    public LineNode(){
        super(new int[]{0,1,0,1}, R.mipmap.line, Color.BLACK);
    }

    @Override
    public int getResId() {
        return super.getResId();
    }

    @Override
    public int[] getDirections() {
        return super.getDirections();
    }
}
