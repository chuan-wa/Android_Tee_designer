package com.example.teedesigner;

import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.teedesigner.Service.GsonService;
import com.example.teedesigner.Service.SaveAsTiffService;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    String TAG="test";
    @Test
    public void addition_isCorrect() {
        float x1= (float) 55.242917241;
        float y1= (float) 14.127417248;
        Log.d(TAG, ""+cosetCal(x1,y1,x1+0.000001f,y1+0.000001f));
    }
    private double cosetCal(float x1, float y1,float x2,float y2){
        double dotProduct = x1 * x2 + y1 * y2;
        double vector1Magnitude = Math.sqrt(x1 * x1 + y1 * y1);
        double vector2Magnitude = Math.sqrt(x2 * x2 + y2 * y2);
        double cosTheta = dotProduct / (vector1Magnitude * vector2Magnitude);
        return cosTheta;
    }

    @Test
    public void testA() throws IOException {
        // TODO(Developer):
        //  1. Before running this sample,
        //  set up Application Default Credentials as described in
        //  https://cloud.google.com/docs/authentication/external/set-up-adc
        //  2. Replace the project variable below.
        //  3. Make sure you have the necessary permission to list storage buckets
        //  "storage.buckets.list"
        String projectId = "melodic-ranger-379706";
        String bucketName = "chuan_bucket";
        String urlPrefix = "https://storage.googleapis.com/chuan_bucket/";
        //authenticateImplicitWithAdc(projectId);
        listObjects(projectId,bucketName,urlPrefix);
    }

    // When interacting with Google Cloud Client libraries, the library can auto-detect the
    // credentials to use.
    public static void authenticateImplicitWithAdc(String project) throws IOException {

        // *NOTE*: Replace the client created below with the client required for your application.
        // Note that the credentials are not specified when constructing the client.
        // Hence, the client library will look for credentials using ADC.
        //
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests.
        Storage storage = StorageOptions.newBuilder().setProjectId(project).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
        System.out.println("Listed all storage buckets.");
    }

    public static void listObjects(String projectId, String bucketName,String urlPrefix) {
        // The ID of your GCP project
        // String projectId = "your-project-id";

        // The ID of your GCS bucket
        // String bucketName = "your-unique-bucket-name";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Page<Blob> blobs = storage.list(bucketName);
        TreeSet<String> listSet=new TreeSet<>();
        Map<String, ArrayList<String>> classfiedImages=new TreeMap<>();

        for (Blob blob : blobs.iterateAll()) {
            String listName=null;
            String info = blob.getName();
            try {listName=info.split("/")[1];}
            catch (Exception e) {
                e.printStackTrace();
            }
            if(listName==null)continue;
            if(!classfiedImages.containsKey(listName)){
                classfiedImages.put(listName,new ArrayList<>());
                classfiedImages.get(listName).add(urlPrefix+info);
            }else classfiedImages.get(listName).add(urlPrefix+info);
        }

        System.out.println(classfiedImages);
    }
    @Test
    public void testMapGson(){
        Map<String,String> printSizeMap=new HashMap<>();
        printSizeMap.put("FRONT","12 X 14 inch");
        printSizeMap.put("BACK","12 X 14 inch");
        printSizeMap.put("LEFT","4 X 4 inch");
        printSizeMap.put("RIGHT","4 X 4 inch");
        String data= GsonService.encodeStringMap(printSizeMap);
        Map <String,String> result=GsonService.decodeStringMap(data);


    }
}