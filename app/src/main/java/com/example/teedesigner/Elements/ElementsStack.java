package com.example.teedesigner.Elements;

import android.util.Log;

import com.example.teedesigner.Service.GsonService;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ElementsStack extends ArrayList<ElementList> {
    private int count=-1;
    private final String TAG="ElementsStack";
    private final Gson gson=new Gson();

    @Override
    public boolean add(ElementList e){
        //very first, detect the element is same as the last one
        //we use gson to compare
        if(count>=0){
            if((GsonService.encodeElementList(e)).equals((GsonService.encodeElementList(this.get(count)))))return false;
            //if(e.equals(this.get(count)))return false;
        }
        //first remove the elements that topper than index element
        for(int x = this.size() - 1; x > count; x--) {super.remove(x);}
        //second detect if size too big
        if(this.size()>=20){
            super.remove(0);
            super.add(e);
            return true;
        }
        //then add elementList
        super.add(e) ;
        count++;
        return true;
    }

    public ElementList goBack(){
        if(!canBack()) Log.w(TAG,"can not go back, count is "+count+" size is "+this.size());
        count--;
        return getElementList();
    }

    public ElementList goForward(){
        if(!canForward())Log.w(TAG,"can not go forward, count is "+count+" size is "+this.size());
        count++;
        return getElementList();
    }

    private ElementList getElementList(){
        if(this.get(count).size()==0)return new ElementList();
        return this.get(count).clone();
    }

    public boolean canBack(){
        return count>0;
    }
    public boolean canForward(){
        return count<this.size()-1;
    }

}
