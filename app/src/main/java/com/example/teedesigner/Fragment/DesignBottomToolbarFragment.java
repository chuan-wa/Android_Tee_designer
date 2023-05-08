package com.example.teedesigner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.teedesigner.R;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.listener.ClickAddImageButtonListener;
import com.example.teedesigner.listener.ClickAddTextButtonListener;
import com.example.teedesigner.listener.UploadImageListener;

public class DesignBottomToolbarFragment extends Fragment {
    private ImageButton addTextButton;
    private ImageButton addImageButton;
    private ImageButton uploadImageButton;
    private SharedViewModel viewModel;
    private ClickAddImageButtonListener clickAddImageButtonListener;
    private UploadImageListener uploadImageListener;
    private ClickAddTextButtonListener clickAddTextButtonListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_design_bottom_toolbar, container, false);
        addTextButton = view.findViewById(R.id.button_add_text);
        addImageButton = view.findViewById(R.id.button_add_image);
        uploadImageButton = view.findViewById(R.id.button_upload_image);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddTextButtonListener.addText();
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddImageButtonListener.ClickAddImageButton();
            }
        });
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {uploadImageListener.uploadImage();}
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickAddImageButtonListener = (ClickAddImageButtonListener) context;
            uploadImageListener=(UploadImageListener) context;
            clickAddTextButtonListener=(ClickAddTextButtonListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }


}