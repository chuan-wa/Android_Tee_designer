package com.example.teedesigner.Service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.R;
import com.example.teedesigner.UserDesignModule;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SaveAsTiffService {
    private final String TAG="Test in SaveAsTiffService";
    private int densityPerInch;
    private Context context;
    private Map<String,String> printSziemap=new TreeMap<>();
    private Map<String,ElementList> elementListMap=new HashMap<>();

    public SaveAsTiffService(Context context, int densityPerInch, String tifName, UserDesignModule module){
        this.context=context;
        this.densityPerInch=densityPerInch;
        this.printSziemap=GsonService.decodeStringMap(module.getPrintSize());
        String background=module.getBackground();
        elementListMap=GsonService.decodeElementMap(module.getElementsGson());
        for(Map.Entry<String,String> entry:printSziemap.entrySet()){
            MyHelper helper=new MyHelper(entry.getValue());
            drawTiff(module,tifName,background+entry.getKey()+entry.getValue(), helper.getCanvas(),helper.getBitmap(),helper.getResizeF(),elementListMap.get(entry.getKey()));
        }

    }

    private void drawTiff(UserDesignModule module,String tifName, String description,Canvas canvas,Bitmap bitmap,float factor,ElementList elements){
        ElementList bitmapElements=elements.transformPathToBitmap();
        ElementList outputElements=bitmapElements.resizeElements(factor);
        outputElements.drawWithoutSelection(canvas);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/result");
        boolean success = true;
        if(!storageDir.exists()){
            success = storageDir.mkdir();
        }
        if(!success){
            Log.w(TAG, "drawTiff: cannot created result folder: "+storageDir);
            return;
        }
        String path=ImageEditService.saveImageAsTiff(module,bitmap,tifName,"/result",description);
        Log.d(TAG, "drawTiff: "+path);

    }




    private class MyHelper {
        private float resizeF=-1f;
        private Canvas canvas=null;
        private Bitmap bitmap=null;
        private MyHelper(String size){
            switch(size){
                case "4 X 4 inch":
                    bitmap=Bitmap.createBitmap(4*densityPerInch,4*densityPerInch,Bitmap.Config.ARGB_8888);
                    canvas=new Canvas(bitmap);
                    resizeF=((float)(4*densityPerInch)/context.getResources().getDimension(R.dimen.dp_300));
                    break;
                case "12 X 14 inch":
                    bitmap=Bitmap.createBitmap(12*densityPerInch,14*densityPerInch,Bitmap.Config.ARGB_8888);
                    canvas=new Canvas(bitmap);
                    resizeF=((float)(12*densityPerInch)/context.getResources().getDimension(R.dimen.dp_300));
                    break;
                case "14 X 16 inch":
                    bitmap=Bitmap.createBitmap(14*densityPerInch,16*densityPerInch,Bitmap.Config.ARGB_8888);
                    canvas=new Canvas(bitmap);
                    resizeF=((float)(14*densityPerInch)/context.getResources().getDimension(R.dimen.dp_350));
                    break;
                case "16 X 18 inch":
                    bitmap=Bitmap.createBitmap(16*densityPerInch,18*densityPerInch,Bitmap.Config.ARGB_8888);
                    canvas=new Canvas(bitmap);
                    resizeF=((float)(16*densityPerInch)/context.getResources().getDimension(R.dimen.dp_400));
                    break;
            }
        }

        public Canvas getCanvas() {
            return canvas;
        }

        public float getResizeF() {
            return resizeF;
        }

        public Bitmap getBitmap(){return bitmap;}
    }

}
