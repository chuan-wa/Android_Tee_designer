package com.example.teedesigner.Elements;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import id.zelory.compressor.Compressor;

public class ElementList extends ArrayList<Element>{
    private final String kind="ElementList";
    public boolean hasSelectedElement(){
        if(this==null)return false;
        for(Element element:this){
            if(element.isSelected())return true;
        }
        return false;
    }


    public Element getSelectedElement(){
        if(!hasSelectedElement())return null;
        for (Element element:this){
            if(element.isSelected())return element;
        }
        return null;
    }

    public void setSelectedElement(float x,float y){
        if(this==null) Log.w("setSelectedElement: ","elementList is null");
        if(hasSelectedElement()) Log.w("setSelectedElement: ", "there is already a selected element");
        //we want the latter one to become selected, because it is rendered latter, which means it on the topper layer of canvas
        Element clickedElement=null;
        for(Element element:this){
            if(element.containsPoint(x,y))clickedElement=element;
        }
        if(clickedElement==null)return;
        clickedElement.setSelected(true);
    }

    //return a new ElementList
    public ElementList resizeElements(float scaleFactor) {
        ElementList newElements=new ElementList();
        for(Element element:this){
            Element newElement= null;
            try {
                newElement = element.clone();
                if(newElement.getClass()==PictureBitmapElement.class){
                    newElement.setScaleFactor(newElement.getScaleFactor()/((PictureBitmapElement) newElement).getCompressFactor());
                    newElement.setX(newElement.getX()-(1-1/((PictureBitmapElement) newElement).getCompressFactor())*newElement.getWidth()/2);
                    newElement.setY(newElement.getY()-(1-1/((PictureBitmapElement) newElement).getCompressFactor())*newElement.getHeight()/2);
                }
                newElement.resize(scaleFactor);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            newElements.add(newElement);
        }
        return newElements;
    }

    public ElementList clone() {
        ElementList newElements=new ElementList();
        for(Element element:this){
            Element newElement= null;
            try {
                newElement = element.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            newElements.add(newElement);
        }
        return newElements;
    }

    public ElementList transformBitmapToPath(){
        ElementList newElements=new ElementList();
        for(Element element:this){
            if(element.getClass()==PictureBitmapElement.class){
                element= new PicturePathElement((PictureBitmapElement) element);
            }else if(element.getClass()==FinishBitmapElement.class){
                element= new FinishPathElement((FinishBitmapElement)element);
            }
            newElements.add(element);
        }

        return newElements;
    }

    public ElementList transformPathToBitmap(){
        ElementList newElements=new ElementList();
        for(Element element:this){
            if(element.getClass()==PicturePathElement.class){
                element= new PictureBitmapElement((PicturePathElement) element);
            }else if(element.getClass()==FinishPathElement.class){
                element=new FinishBitmapElement((FinishPathElement) element);
            }
            newElements.add(element);
        }
        return newElements;
    }


    public ElementList MUTEtransformBitmapToPath(){
        ElementList newElements=new ElementList();
        for(Element element:this){
            if(element.getClass()==PictureBitmapElement.class){
                element= new PicturePathElement((PictureBitmapElement) element);
            }else if(element.getClass()==FinishBitmapElement.class){
                element= new FinishPathElement((FinishBitmapElement)element);
            }
            newElements.add(element);
        }

        return newElements;
    }

    public void draw(Canvas canvas){
        if(this.size()==0)return;
        for(Element element:this){
            element.draw(canvas);
        }
        if(hasSelectedElement())getSelectedElement().drawSelection(canvas);
    }

    public void drawWithoutSelection(Canvas canvas){
        if(this.size()==0)return;
        for(Element element:this){
            element.draw(canvas);
        }
    }

    public void compressedBitmaps(Context context) throws IOException {
        for(Element element:this){
            if(element.getClass()==PictureBitmapElement.class||element.getClass()==FinishBitmapElement.class){
                ((PictureBitmapElement) element).setBitmap(new Compressor(context).compressToBitmap(new File(((PictureBitmapElement) element).getImgSrc())));
            }
        }
    }

    public void deCompressedBitmap(){
        for(Element element:this){
            if(element.getClass()==PictureBitmapElement.class){
                element.setScaleFactor(element.getScaleFactor()/((PictureBitmapElement) element).getCompressFactor());
            }
        }
    }

    public boolean leveldownElement(){
        if(!this.hasSelectedElement())return false;
        int index=this.indexOf(getSelectedElement());
        if(index==0)return false;
        Collections.swap(this,index-1,index);
        return true;
    }

    public void modifyElements(ArrayList<Float> result,float factor){
        //result: x, y, scale, rotate
        if(result==null)return;
        for(Element element:this){
            element.setScaleFactor(element.getScaleFactor()*result.get(2));
            element.setRotation(element.getRotation()+result.get(3));
            element.setX(element.getX()+result.get(0)/factor);
            element.setY(element.getY()+result.get(1)/factor);
        }

    }


}
