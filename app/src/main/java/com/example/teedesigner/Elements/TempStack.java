package com.example.teedesigner.Elements;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TempStack extends HashMap<String,Element> {
    private final String PRE="previous",NOW="now";
    public TempStack(){
        this.put(PRE,null);
        this.put(NOW,null);
    }

    public void updatePre(Element fe){
        Element feTemp=null;
        if(fe==null)return;
        try {
            feTemp=fe.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        this.put(PRE,feTemp);
    }
    public void updateNow(Element fe){
        Element feTemp=null;
        if(fe==null)return;
        try {
            feTemp=fe.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        this.put(NOW,feTemp);
    }
    public ArrayList<Float> diff(){
        if(this.get(PRE)==null||this.get(NOW)==null)return null;
        Element pre=this.get(PRE);Element now=this.get(NOW);
        float xDiff=now.getX()-pre.getX();float yDiff=now.getY()-pre.getY();
        float scaleDiff=now.getScaleFactor()/pre.getScaleFactor();
        float rotateDiff=now.getRotation()- pre.getRotation();
        ArrayList<Float>result=new ArrayList<>(Arrays.asList(xDiff,yDiff,scaleDiff,rotateDiff));
        return result;

    }

}
