package com.example.teedesigner.Elements;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class BackgroundBitmapElement extends Element {
    private Resources resources;
    private int drawablePath;
    private Bitmap bitmap;
    private Paint paint;
    public BackgroundBitmapElement(Resources resources, int drawablePath) {
        super( 0, 0);
        this.resources=resources;
        this.drawablePath=drawablePath;
        this.bitmap=(BitmapFactory.decodeResource(resources,drawablePath));
        this.paint=new Paint();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public BackgroundBitmapElement(BackgroundPathElement backgroundPathElement) {
        super(backgroundPathElement.getX(),backgroundPathElement.getY());
        setRotation(backgroundPathElement.getRotation());
        setSelected(backgroundPathElement.isSelected());
        setScaleFactor(backgroundPathElement.getScaleFactor());
        setHorizontallyFlip(backgroundPathElement.isHorizontallyFlip());
        setVerticallyFlip(backgroundPathElement.isVerticallyFlip());
        setMargin(backgroundPathElement.getMargin());
        this.resources=(backgroundPathElement.getResources());
        this.drawablePath=(backgroundPathElement.getDrawablePath());
        this.bitmap=(BitmapFactory.decodeResource(resources,drawablePath));
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void setDrawablePath(int drawablePath) {
        this.drawablePath = drawablePath;
    }

    public Resources getResources() {
        return resources;
    }

    public int getDrawablePath() {
        return drawablePath;
    }

    @Override
    public void draw(Canvas canvas){
        canvas.drawBitmap(getBitmap(),null,new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), getPaint());
    }

}
