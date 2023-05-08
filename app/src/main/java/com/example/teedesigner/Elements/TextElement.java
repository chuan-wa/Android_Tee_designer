package com.example.teedesigner.Elements;

import android.graphics.Canvas;
import android.graphics.Paint;

public class TextElement extends Element {
    private String text;
    private float textSize;
    private int textColor;
    private final String kind="TextElement";


    public TextElement(String text, float textSize, int textColor, float x, float y) {
        super(x, y);
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
    }

    private Paint getPaint(){
        Paint paint=new Paint();
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        return paint;
    }

    @Override
    public float getWidth(){
        return getPaint().measureText(text);
    }
    @Override
    //remember why we have minus here : text x,y corrdinate is from bottom-left rather than top-left
    public float getHeight(){
        return -(getPaint().descent()- getPaint().ascent());
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        super.TransformCanvas(canvas,false);
        canvas.drawText(text, getX(), getY(), getPaint());
        canvas.restore();

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(float textSize) {
        this.textSize=textSize;
    }

    public void setTextColor(int color) {
        this.textColor=color;
    }

    public String getText() {
        return text;
    }

}