package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.teedesigner.R;
import com.example.teedesigner.listener.ChangeColorListener;
import com.example.teedesigner.listener.DiscardPreviewListener;
import com.example.teedesigner.listener.SaveDesignListener;
import com.example.teedesigner.listener.SavePreviewListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class PreviewTopToolBar extends Fragment {
    ArrayList<String> sizeOptions = new ArrayList<>(Arrays.asList("12 X 14 inch", "14 X 16 inch", "16 X 18 inch"));
    ArrayList<String> colorOptions=new ArrayList<>(Arrays.asList("white","black"));
    ImageButton exit;
    ImageButton save;
    SavePreviewListener savePreviewListener;
    DiscardPreviewListener discardPreviewListener;
    ArrayAdapter<String> sizeAdapter;
    ArrayAdapter<String> colorAdapter;
    Spinner sizeSpinner,colorSpinner;
    Map<String,Integer>selectedItem=new HashMap<>();
    String loc="FRONT";
    String background="white";
    private ChangeColorListener changeColorListener;
    private Map<String,String> printSizeMap=new HashMap<>();
    private final Map<String,Integer> sWithItemMap=new HashMap<String,Integer>(){{
        put("12 X 14 inch",0);
        put("14 X 16 inch",1);
        put("16 X 18 inch",2);
        put("4 X 4 inch",0);
        put("white",0);
        put("black",1);
    }};

    public void setSelectedItem() {
        selectedItem.put("FRONT",sWithItemMap.get(printSizeMap.get("FRONT")));
        selectedItem.put("BACK",sWithItemMap.get(printSizeMap.get("BACK")));
        selectedItem.put("LEFT",sWithItemMap.get(printSizeMap.get("LEFT")));
        selectedItem.put("RIGHT",sWithItemMap.get(printSizeMap.get("RIGHT")));
        selectedItem.put("COLOR",sWithItemMap.get(background));
        //changeSizeOptions();
    }

    //toolbar接受初始size时，也要调整选择框
    public PreviewTopToolBar(Map<String,String>printSizeMap,String background){
        this.printSizeMap=printSizeMap;
        this.background=background;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_preview_top_tool_bar, container, false);
        exit=view.findViewById(R.id.button_discard_preview);
        save=view.findViewById(R.id.button_save_preview);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardPreviewListener.discardPreview();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreviewListener.savePreview();
            }
        });
        setSelectedItem();

        sizeSpinner = view.findViewById(R.id.size_spinner);
        sizeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizeOptions){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Get the current dropdown item view
                View view = super.getDropDownView(position, convertView, parent);

                // If the current item is selected, set its background color
                if (position == sizeSpinner.getSelectedItemPosition()) {
                    view.setBackgroundColor(Color.GRAY);
                } else {
                    view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                return view;
            }
        };
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected value
                selectedItem.put(loc,position);
                String selectedOption = parent.getItemAtPosition(position).toString();
                changeColorListener.changeSize(selectedOption);
                // Display the selected value in the spinner view
                //((TextView) parent.getChildAt(0)).setText(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });



        colorSpinner = view.findViewById(R.id.color_spinner);
        colorAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, colorOptions){
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Get the current dropdown item view
                View view = super.getDropDownView(position, convertView, parent);

                // If the current item is selected, set its background color
                if (position == colorSpinner.getSelectedItemPosition()) {
                    view.setBackgroundColor(Color.GRAY);
                } else {
                    view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                return view;
            }
        };



        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected value

                String selectedOption = parent.getItemAtPosition(position).toString();
                // Display the selected value in the spinner view
                changeColorListener.changeColor(selectedOption);
                ((TextView) parent.getChildAt(0)).setText(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing if nothing is selected
            }
        });
        changeSizeOptions();
        colorSpinner.setSelection(selectedItem.get("COLOR"));


        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        savePreviewListener=(SavePreviewListener) context;
        discardPreviewListener=(DiscardPreviewListener) context;
        changeColorListener=(ChangeColorListener) context;
    }

    public void setLoc(String loc){
        this.loc=loc;
        changeSizeOptions();
    }



    public void changeSizeOptions(){
            switch(loc){
                case"FRONT":
                    sizeAdapter.clear();
                    sizeAdapter.add("12 X 14 inch");
                    sizeAdapter.add("14 X 16 inch");
                    sizeAdapter.add("16 X 18 inch");
                    sizeSpinner.setSelection(selectedItem.get(loc));
                    break;
                case"BACK":
                    sizeAdapter.clear();
                    sizeAdapter.add("12 X 14 inch");
                    sizeAdapter.add("14 X 16 inch");
                    sizeAdapter.add("16 X 18 inch");
                    sizeSpinner.setSelection(selectedItem.get(loc));
                    break;
                case"LEFT":
                    sizeAdapter.clear();
                    sizeAdapter.add("4 X 4 inch");
                    sizeSpinner.setSelection(selectedItem.get(loc));
                    break;
                case"RIGHT":
                    sizeAdapter.clear();
                    sizeAdapter.add("4 X 4 inch");
                    sizeSpinner.setSelection(selectedItem.get(loc));
                    break;
            }
    }
}