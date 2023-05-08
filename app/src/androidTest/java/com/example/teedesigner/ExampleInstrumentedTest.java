package com.example.teedesigner;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.PicturePathElement;
import com.example.teedesigner.Service.GsonService;
import com.example.teedesigner.Service.SaveAsTiffService;

import java.util.HashMap;
import java.util.Map;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.teedesigner", appContext.getPackageName());
    }
    @Test
    public void testB() throws CloneNotSupportedException {
/*        SaveAsTiffService saveAsTiffService=new SaveAsTiffService(300,14);
        saveAsTiffService.getCanvas();*/
    }

    @Test
    public void testC(){
        ElementList elements=new ElementList();
        PicturePathElement picturePathElement=new PicturePathElement("/storage/emulated/0/Pictures/imgs/graphical_samples/animals/PngItem_1238594.png",200,200);
        elements.add(picturePathElement);
        elements.transformPathToBitmap();
    }
    @Test
    public void testMapGson(){
        Map<String,String> printSizeMap=new HashMap<>();
        printSizeMap.put("FRONT","12 X 14 inch");
        printSizeMap.put("BACK","12 X 14 inch");
        printSizeMap.put("LEFT","4 X 4 inch");
        printSizeMap.put("RIGHT","4 X 4 inch");
        String data=GsonService.encodeStringMap(printSizeMap);
        Map <String,String> result=GsonService.decodeStringMap(data);


    }

}