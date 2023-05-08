package com.example.teedesigner.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;

import id.zelory.compressor.Compressor;

public class PictureBitmapElement extends Element{
    private Bitmap bitmap;
    private Paint paint;
    private String imgSrc;
    private boolean isUserPhoto=false;
    private float compressFactor=1f;

    public float getCompressFactor() {
        return compressFactor;
    }

    public void setCompressFactor(float compressFactor) {
        this.compressFactor = compressFactor;
    }

    public boolean isUserPhoto() {
        return isUserPhoto;
    }

    public void setUserPhoto(boolean userPhoto) {
        isUserPhoto = userPhoto;
    }

    public PictureBitmapElement(PicturePathElement picturePathElement) {
        super(picturePathElement.getX(),picturePathElement.getY());
        setRotation(picturePathElement.getRotation());
        setSelected(picturePathElement.isSelected());
        setScaleFactor(picturePathElement.getScaleFactor());
        this.bitmap= BitmapFactory.decodeFile(picturePathElement.getImgSrc());
        this.paint=picturePathElement.getPaint();
        this.imgSrc=picturePathElement.getImgSrc();
        this.isUserPhoto=picturePathElement.isUserPhoto();
        this.compressFactor=picturePathElement.getCompressFactor();
        setHorizontallyFlip(picturePathElement.isHorizontallyFlip());
        setVerticallyFlip(picturePathElement.isVerticallyFlip());
        setMargin(picturePathElement.getMargin());

    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        this.bitmap=BitmapFactory.decodeFile(imgSrc);
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public PictureBitmapElement(String imgSrc , float x, float y){
        super(x,y);
        this.imgSrc=imgSrc;
        BitmapFactory.Options options = new BitmapFactory.Options();
/*        options.inJustDecodeBounds = true;
        options.inSampleSize=4;
        options.inJustDecodeBounds=false*/
        this.bitmap=BitmapFactory.decodeFile(imgSrc,options);
        paint=new Paint();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public Paint getPaint() {
        return paint;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public float getWidth() {

        return bitmap.getWidth();
    }

    @Override
    public float getHeight() {

        return bitmap.getHeight();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        super.TransformCanvas(canvas,false);
        canvas.drawBitmap(bitmap,getX(),getY(),paint);
        canvas.restore();


    }

}
