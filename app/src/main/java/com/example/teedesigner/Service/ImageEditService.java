package com.example.teedesigner.Service;

import android.graphics.Bitmap;
import android.media.ImageWriter;
import android.os.Environment;
import android.util.Log;


import com.example.teedesigner.UserDesignModule;

import org.beyka.tiffbitmapfactory.CompressionScheme;
import org.beyka.tiffbitmapfactory.Orientation;
import org.beyka.tiffbitmapfactory.TiffSaver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

public class ImageEditService {





    public static String saveImage(Bitmap bitmap,String name,String folder){
        if(name==null){
            name= String.valueOf(Calendar.getInstance().getTime());
            name = name.replaceAll(" ","_");
            name = name.replaceAll(":","_");
            name = name+".png";
        }
        //String savedImagePath=null;
        String savedImagePath=null;
        String imageFileName=name;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+folder);
        boolean success = true;
        if(!storageDir.exists()){
            success = storageDir.mkdir();
        }
        if(success){
            File imageFile = new File(storageDir, imageFileName);
            try{
                if(imageFile.exists())return imageFile.getPath();
                Files.createDirectories(Paths.get(imageFile.getParent()));
                imageFile.createNewFile();
                //savedImagePath = imageFile.getAbsolutePath();
                savedImagePath = imageFile.getPath();
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fOut);
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savedImagePath;
    }

    public static String saveImageAsTiff(UserDesignModule module,Bitmap bitmap, String name, String folder, String description){
        String path=null;
        //String savedImagePath=null;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+folder);
        boolean success = true;
        if(!storageDir.exists()){
            success = storageDir.mkdir();
        }
        if(success){
            TiffSaver.SaveOptions options = new TiffSaver.SaveOptions();
            options.compressionScheme = CompressionScheme.LZW;
            options.orientation = Orientation.LEFT_TOP;
            options.author = "User";
            options.copyright = "No copyRight";
            options.imageDescription= "["+module.getDesignName()+"] ["+module.getBackground()+"] ["+module.getPrintSize()+"]";
            path=storageDir.getPath()+"/"+name;
            boolean saved = TiffSaver.appendBitmap(path, bitmap, options);
        }
        return path;
    }

    public static void deleteImage(String path){
        if(path==null)return;
        File imageFile=new File(path);
        if(imageFile.exists()){
            imageFile.delete();
        }
    }

}
