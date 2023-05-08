package com.example.teedesigner.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teedesigner.R;

import java.util.Map;


public class PreviewFragment extends Fragment {
    private final Map<String,String>printSizeMap;
    private final String background;
    public PreviewFragment(Map<String,String>printSizeMap,String background){
        this.printSizeMap=printSizeMap;
        this.background=background;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_preview_main, container, false);
        //view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        PreviewTopToolBar previewTopToolBar=new PreviewTopToolBar(printSizeMap,background);
        PreviewMidCanvas previewMidCanvas=new PreviewMidCanvas();
        PreviewBottomCrossView previewBottomCrossView=new PreviewBottomCrossView();
        transaction.add(R.id.fragment_top_preview,previewTopToolBar)
                .add(R.id.fragment_mid_preview,previewMidCanvas)
                .add(R.id.fragment_bottom_preview,previewBottomCrossView)
                .commit();


        return view;
    }
}