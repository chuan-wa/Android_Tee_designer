package com.example.teedesigner.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

public class FinishBitmapElement extends PictureBitmapElement{
    private RectF rectCliped;
    public FinishBitmapElement(String imgSrc, float x, float y,RectF rectCliped) {
        super(imgSrc, x, y);
        this.rectCliped=rectCliped;
    }


    public FinishBitmapElement(FinishPathElement finishPathElement) {
        super(finishPathElement.getImgSrc(),finishPathElement.getX(),finishPathElement.getY());
        setRotation(finishPathElement.getRotation());
        setSelected(finishPathElement.isSelected());
        setScaleFactor(finishPathElement.getScaleFactor());
        setBitmap(BitmapFactory.decodeFile(finishPathElement.getImgSrc()));
        setPaint(finishPathElement.getPaint());
        setHorizontallyFlip(finishPathElement.isHorizontallyFlip());
        setVerticallyFlip(finishPathElement.isVerticallyFlip());
        setMargin(finishPathElement.getMargin());
        this.rectCliped=finishPathElement.getRectCliped();
    }

    public void setRectCliped(RectF rectCliped) {
        this.rectCliped = rectCliped;
    }

    public RectF getRectCliped() {
        return rectCliped;
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("TAG", "FinishBitmapElement is drawn");
        canvas.save();
        canvas.clipRect(rectCliped);
        super.TransformCanvas(canvas,false);
        canvas.drawBitmap(getBitmap(),getX(),getY(),getPaint());
        canvas.restore();
        canvas.restore();
    }
}
