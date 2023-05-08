package com.example.teedesigner.Service;

import com.example.teedesigner.Elements.Element;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.FinishPathElement;
import com.example.teedesigner.Elements.PicturePathElement;
import com.example.teedesigner.Elements.TextElement;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;

public class GsonService {

    private static Gson getDecodeGson(){
        GsonFireBuilder builder = new GsonFireBuilder()
                .registerTypeSelector(Element.class, new TypeSelector<Element>() {
                    @Override
                    public Class<? extends Element> getClassForElement(JsonElement readElement) {
                        String kind = readElement.getAsJsonObject().get("kind").getAsString();
                        String innerKind="not have innerKind";
                        if(readElement.getAsJsonObject().get("innerkind")!=null) innerKind=readElement.getAsJsonObject().get("innerkind").getAsString();
                        if(innerKind.equals("FinishElement")){
                            return FinishPathElement.class; //This will cause Gson to deserialize the json mapping to A
                        } else if(kind.equals("PictureElement")) {
                            return PicturePathElement.class; //This will cause Gson to deserialize the json mapping to B
                        } else if(kind.equals("TextElement")){
                            return TextElement.class;
                        } else
                        {
                            return null; //returning null will trigger Gson's default behavior
                        }
                    }
                });
        Gson gson = builder.createGson();
        return gson;
    }

    public static ElementList getBitmapElementList(String data){
        Gson gson= getDecodeGson();
        ElementList pathElements=gson.fromJson(data,ElementList.class);
        ElementList bitmapElements=new ElementList();
        bitmapElements=pathElements.transformPathToBitmap();
        return bitmapElements;
    }

    public static ElementList getPathElementList(String data){
        Gson gson= getDecodeGson();
        return gson.fromJson(data,ElementList.class);
    }

    public static String encodeElementList(ElementList bitmapElements){
        Gson gson=new Gson();
        ElementList pathElements;
        pathElements=bitmapElements.transformBitmapToPath();
        return gson.toJson(pathElements);
    }

    public static Map<String,ElementList> decodeElementMap(String data){
        Gson gson= getDecodeGson();
        Map<String,ArrayList> tempMap=gson.fromJson(data,Map.class);
        Map<String,ElementList>pathElementMap=new HashMap<String, ElementList>();
        for(Map.Entry<String,ArrayList>entry:tempMap.entrySet()){
            //if(entry.getValue().size()==0)continue;
            //String value= gson.toJson(entry.getValue());
            ElementList pathElements=GsonService.getPathElementList(gson.toJson(entry.getValue()));
            pathElementMap.put(entry.getKey(),pathElements);
        }
        return pathElementMap;
    }

    public static String encodeElementsMap(Map<String,ElementList> eMap){
        Gson gson=new Gson();
        for(Map.Entry<String,ElementList>entry:eMap.entrySet()){
            ElementList pathElements=entry.getValue().transformBitmapToPath();
            entry.setValue(pathElements);
        }
        String testValue=gson.toJson(eMap);
        return gson.toJson(eMap);
    }

    public static String encodeStringMap(Map<String,String> sMap){
        Gson gson=new Gson();
        return gson.toJson(sMap);
    }
    public static Map<String,String> decodeStringMap(String data){
        Gson gson=new Gson();
        TreeMap<String,String> stringMap=gson.fromJson(data,TreeMap.class);
        return stringMap;
    }

}
