package com.example.teedesigner.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.teedesigner.Elements.TextElement;
import com.example.teedesigner.R;
import com.example.teedesigner.SharedViewModel;


public class DesignFragment extends Fragment {
    private SharedViewModel viewModel;
    private final String printSizeArea;
    public DesignFragment(String printSizeArea) {
        this.printSizeArea=printSizeArea;
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_main, container, false);
        FragmentTransaction transaction=getChildFragmentManager().beginTransaction();
        DesignBottomToolbarFragment bottomToolbarFragment=new DesignBottomToolbarFragment();
        DesignCanvasFragment canvasFragment=new DesignCanvasFragment(printSizeArea);
        DesignTopToolBarFragment topToolBarFragment=new DesignTopToolBarFragment();
        transaction.add(R.id.fragment_top_container,topToolBarFragment)
                .add(R.id.fragment_mid_container,canvasFragment)
                .add(R.id.fragment_bottom_container,bottomToolbarFragment)
                .commit();

        viewModel=new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.observeElements().observe(getViewLifecycleOwner(),elements -> {
            if(elements.hasSelectedElement()){
                if(elements.getSelectedElement().getClass()== TextElement.class){
                    BottomTextFragment bottomTextFragment=new BottomTextFragment((TextElement) elements.getSelectedElement());
                    FragmentTransaction transaction1=getChildFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragment_bottom_container,bottomTextFragment);
                    transaction1.commit();
                    return;
                }
            }
            FragmentTransaction transaction2=getChildFragmentManager().beginTransaction();
            transaction2.replace(R.id.fragment_bottom_container,bottomToolbarFragment);
            transaction2.commit();


        });
        return view;
    }


}