package com.example.teedesigner.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

public class FinishPathElement extends PicturePathElement {
    private RectF rectCliped;
    private final String innerkind="FinishElement";

    public FinishPathElement(String path, float x, float y, RectF rectCliped) {
        super(path, x, y);
        this.rectCliped=rectCliped;
    }



    public FinishPathElement(FinishBitmapElement finishBitmapElement) {
        super(finishBitmapElement.getImgSrc(),finishBitmapElement.getX(),finishBitmapElement.getY());
        setRotation(finishBitmapElement.getRotation());
        setSelected(finishBitmapElement.isSelected());
        setScaleFactor(finishBitmapElement.getScaleFactor());
        setPaint(finishBitmapElement.getPaint());
        setHorizontallyFlip(finishBitmapElement.isHorizontallyFlip());
        setVerticallyFlip(finishBitmapElement.isVerticallyFlip());
        setMargin(finishBitmapElement.getMargin());
        this.rectCliped=finishBitmapElement.getRectCliped();
    }

    public RectF getRectCliped() {
        return rectCliped;
    }


    public void setRectCliped(RectF rectL) {
        this.rectCliped=rectL;
    }
}
