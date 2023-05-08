package com.example.teedesigner.Elements.ElementButtons;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

public class ElementButton {
    private float x;
    private float y;
    private final float radius=30;
    private final int color= Color.BLUE;
    private final String name;
    //private boolean isClicked=false;

    public ElementButton(String name){
        this.x=0;
        this.y=0;
        this.name=name;
    }

    public boolean isClicked(float touchX,float touchY){
        float distance = (float) Math.sqrt(Math.pow(this.x - touchX, 2) + Math.pow(this.y - touchY, 2));
        return distance <= radius;
    }

    public void setButton(float x,float y){
        this.x=x;
        this.y=y;
    }

    //will seperate into several draw method to distinguish buttons
    public void draw(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        canvas.drawCircle(this.x,this.y,radius,paint);
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getRadius(){
        return radius;
    }
    public String getName(){return name;}


}
