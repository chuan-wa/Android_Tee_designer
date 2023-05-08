package com.example.teedesigner.listener;

import java.util.ArrayList;
import java.util.Map;

public interface DownloadCompleteListener {
    void downloadComplete(Map<String, ArrayList<String>> allImages);
}
