package com.example.teedesigner.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;

//this class is used to transform bitmap path data
public class PicturePathElement extends Element {
    //private Bitmap picture;
    //private Uri imgSrc;
    private String imgSrc;
    private Paint paint;
    private boolean isUserPhoto=false;
    private final String kind="PictureElement";
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

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public PicturePathElement(String imgSrc, float x, float y) {
        super(x, y);
        this.imgSrc = imgSrc;
        this.paint=new Paint();
    }

    public PicturePathElement(PictureBitmapElement pictureBitmapElement){
        super(pictureBitmapElement.getX(),pictureBitmapElement.getY());
        setRotation(pictureBitmapElement.getRotation());
        setSelected(pictureBitmapElement.isSelected());
        setScaleFactor(pictureBitmapElement.getScaleFactor());
        this.paint=pictureBitmapElement.getPaint();
        this.imgSrc=pictureBitmapElement.getImgSrc();
        this.isUserPhoto=pictureBitmapElement.isUserPhoto();
        this.compressFactor=pictureBitmapElement.getCompressFactor();
        setHorizontallyFlip(pictureBitmapElement.isHorizontallyFlip());
        setVerticallyFlip(pictureBitmapElement.isVerticallyFlip());
        setMargin(pictureBitmapElement.getMargin());
    }

    public String getImgSrc() {
        return imgSrc;
    }

    //public void setPicture(Bitmap picture) {this.picture = picture;}



    public Paint getPaint(){return paint;}

    @Override
    public float getWidth() {

        return 0;
    }

    @Override
    public float getHeight() {

        return 0;
    }


}
