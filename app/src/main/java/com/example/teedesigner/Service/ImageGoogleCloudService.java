package com.example.teedesigner.Service;

import android.util.Log;

import com.example.teedesigner.GlobalData;
import com.example.teedesigner.listener.DownloadCompleteListener;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public  class ImageGoogleCloudService {
    private static boolean downloadingComplete=false;
    private DownloadCompleteListener downloadCompleteListener;
    //private static String urlPrefix;

    public static Map<String,ArrayList<String>> getImages(){
        // TODO(Developer):
        //  1. Before running this sample,
        //  set up Application Default Credentials as described in
        //  https://cloud.google.com/docs/authentication/external/set-up-adc
        //  2. Replace the project variable below.
        //  3. Make sure you have the necessary permission to list storage buckets
        //  "storage.buckets.list"
        String projectId = "melodic-ranger-379706";
        String bucketName = "chuan_bucket";
        String urlPrefix = GlobalData.urlPrefix;
        //authenticateImplicitWithAdc(projectId);
        return listObjects(projectId,bucketName,urlPrefix);
    }



    // When interacting with Google Cloud Client libraries, the library can auto-detect the
    // credentials to use.
    public  void authenticateImplicitWithAdc(String project) throws IOException {

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

    public static Map<String,ArrayList<String>> listObjects(String projectId, String bucketName, String urlPrefix) {
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
        DownloadingComplete();
        Log.d("TAG", "listObjects: "+isDownloadingComplete());
        return classfiedImages;
    }

    public static void DownloadingComplete() {
        downloadingComplete = true;
    }
    public static boolean isDownloadingComplete(){
        return downloadingComplete;
    }
}
