package com.example.teedesigner;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.teedesigner.Elements.PictureBitmapElement;

import java.io.Serializable;
import java.util.Map;

public class UserDesignModule implements Serializable {

    private int id;
    private String designName;
    private String preImagePath;
    private String designsGson;
    private String elementsGson;
    private String background;
    private String printSize;

    public UserDesignModule(String designName, String preImagePath, String designsGson, String elementsGson, String background, String printSize) {
        this.designName = designName;
        this.preImagePath = preImagePath;
        this.designsGson = designsGson;
        this.elementsGson = elementsGson;
        this.background = background;
        this.printSize = printSize;
    }

    public UserDesignModule() {


    }

    public String toString() {
        return "UserDesignModule{" +
                "designName='" + designName + '\'' +
                ", preImagePath='" + preImagePath + '\'' +
                ", designsGson='" + designsGson + '\'' +
                ", elementsGson='" + elementsGson + '\'' +
                ", background='" + background + '\'' +
                ", printSize='" + printSize + '\'' +
                '}';
    }

    public ContentValues toCV(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("designName",designName);
        contentValues.put("preImagePath",preImagePath);
        contentValues.put("designsGson",designsGson);
        contentValues.put("elementsGson",elementsGson);
        contentValues.put("background",background);
        contentValues.put("printSize",printSize);
        return contentValues;
    }

    @SuppressLint("Range")
    public UserDesignModule initBCursor(Cursor cursor){
        this.designName=cursor.getString(cursor.getColumnIndex("designName"));
        this.preImagePath=cursor.getString(cursor.getColumnIndex("preImagePath"));
        this.designsGson=cursor.getString(cursor.getColumnIndex("designsGson"));
        this.elementsGson=cursor.getString(cursor.getColumnIndex("elementsGson"));
        this.background=cursor.getString(cursor.getColumnIndex("background"));
        this.printSize=cursor.getString(cursor.getColumnIndex("printSize"));
        return this;
    }


    public Map<String, PictureBitmapElement> decodeDesignGson(){
        return null;
    }

    public int getId() {
        return id;
    }

    public String getDesignName() {
        return designName;
    }

    public String getPreImagePath() {
        return preImagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public void setPreImagePath(String preImagePath) {
        this.preImagePath = preImagePath;
    }

    public void setDesignsGson(String designsGson) {
        this.designsGson = designsGson;
    }

    public void setElementsGson(String elementsGson) {
        this.elementsGson = elementsGson;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setPrintSize(String printSize) {
        this.printSize = printSize;
    }

    public String getDesignsGson() {
        return designsGson;
    }

    public String getElementsGson() {
        return elementsGson;
    }

    public String getBackground() {
        return background;
    }

    public String getPrintSize() {
        return printSize;
    }
}
