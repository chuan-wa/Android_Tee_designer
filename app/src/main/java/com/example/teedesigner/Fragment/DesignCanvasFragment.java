package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.teedesigner.Controller.CanvasController;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.View.CanvasView;
import com.example.teedesigner.R;

public class DesignCanvasFragment extends Fragment{
    private CanvasView canvasView;
    private SharedViewModel viewModel;
    private ImageButton back;
    private ImageButton forward;
    private CanvasController controller;
    private CanvasController.ChangeImageListener changeImageListener;
    private CanvasController.ChangeDesignListener changeDesignListener;
    private CanvasController.ChangeTextListener changeTextListener;
    private CanvasController.LayerListener layerListener;
    private CanvasController.DeleteListener deleteListener;
    private String printSize=null;


    public  DesignCanvasFragment(){}

    public  DesignCanvasFragment(String printSize){this.printSize=printSize;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_mid_canvas, container, false);
        //view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        canvasView = view.findViewById(R.id.canvas_view);
        controller=new CanvasController(changeImageListener,changeTextListener,changeDesignListener,layerListener,deleteListener);
        canvasView.setCanvasController(controller);
        //这里判断rect的大小
        LinearLayout.LayoutParams layoutParamsnew= setCanvasSize();
        canvasView.setLayoutParams(layoutParamsnew);
        canvasView.setViewModelStoreOwner(requireActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //canvasView.setElements(elements);
        back=view.findViewById(R.id.designBack);forward=view.findViewById(R.id.designForward);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.goBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.goForward();
            }
        });
        viewModel.observeElements().observe(getViewLifecycleOwner(),elements -> {
            canvasView.setElements(elements);
            setButtonState();
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }


    public void setButtonState(){
       back.setEnabled(viewModel.canGoBack());
       forward.setEnabled(viewModel.canGoForward());
       if(viewModel.canGoBack())back.clearColorFilter();
       else back.setColorFilter(Color.GRAY);
        if(viewModel.canGoForward())forward.clearColorFilter();
        else forward.setColorFilter(Color.GRAY);

    }


    public void addText(String text){

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        changeDesignListener=(CanvasController.ChangeDesignListener) context;
        changeImageListener=(CanvasController.ChangeImageListener) context;
        changeTextListener=(CanvasController.ChangeTextListener) context;
        layerListener=(CanvasController.LayerListener) context;
        deleteListener=(CanvasController.DeleteListener) context;
    }

    private LinearLayout.LayoutParams setCanvasSize(){
        LinearLayout.LayoutParams layoutParamsnew=null;

        if(printSize==null||printSize.equals("default")){
            layoutParamsnew= new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_400),(int) getResources().getDimension(R.dimen.dp_450));
            layoutParamsnew.gravity= Gravity.CENTER;
        }
        switch(printSize){
            case "4 X 4 inch":
                layoutParamsnew= new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_300),(int) getResources().getDimension(R.dimen.dp_300));
                layoutParamsnew.gravity= Gravity.CENTER;
                break;
            case "12 X 14 inch":
                layoutParamsnew= new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_300),(int) getResources().getDimension(R.dimen.dp_350));
                layoutParamsnew.gravity= Gravity.CENTER;
                break;
            case "14 X 16 inch":
                layoutParamsnew= new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_350),(int) getResources().getDimension(R.dimen.dp_400));
                layoutParamsnew.gravity= Gravity.CENTER;
                break;
            case "16 X 18 inch":
                layoutParamsnew= new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.dp_400),(int) getResources().getDimension(R.dimen.dp_450));
                layoutParamsnew.gravity= Gravity.CENTER;
                break;
            default:
                Log.w("TAG", "setCanvasSize: no such size");
        }
        return layoutParamsnew;
    }

    public void refresh(){
        canvasView.invalidate();
    }
}
