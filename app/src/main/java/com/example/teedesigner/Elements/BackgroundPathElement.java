package com.example.teedesigner.Elements;

import android.content.res.Resources;
import android.graphics.Paint;

public class BackgroundPathElement extends Element{
    private Resources resources;
    private int drawablePath;
    private Paint paint;
    public BackgroundPathElement(Resources resources,int drawablePath) {
        super(0,0);
        this.resources=resources;
        this.drawablePath=drawablePath;
        this.paint=new Paint();

    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Resources getResources() {
        return resources;
    }

    public int getDrawablePath() {
        return drawablePath;
    }

    public void setDrawablePath(int drawablePath) {
        this.drawablePath = drawablePath;
    }

    public BackgroundPathElement(BackgroundBitmapElement backgroundBitmapElement) {
       super(backgroundBitmapElement.getX(), backgroundBitmapElement.getY());
        setRotation(backgroundBitmapElement.getRotation());
        setSelected(backgroundBitmapElement.isSelected());
        setScaleFactor(backgroundBitmapElement.getScaleFactor());
        this.paint=backgroundBitmapElement.getPaint();
        this.resources=(backgroundBitmapElement.getResources());
        this.drawablePath=(backgroundBitmapElement.getDrawablePath());
        setHorizontallyFlip(backgroundBitmapElement.isHorizontallyFlip());
        setVerticallyFlip(backgroundBitmapElement.isVerticallyFlip());
        setMargin(backgroundBitmapElement.getMargin());
    }

}
