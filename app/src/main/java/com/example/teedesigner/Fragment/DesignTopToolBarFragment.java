package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.R;
import com.example.teedesigner.listener.DiscardDesignListener;
import com.example.teedesigner.listener.SaveDesignListener;

public class DesignTopToolBarFragment extends Fragment {
    private ImageButton flipHorizontally;
    private ImageButton flipVertically;
    private SharedViewModel viewModel;
    private ImageButton saveDesign;
    private ImageButton discardDesign;
    private SaveDesignListener saveDesignListener;
    private DiscardDesignListener discardDesignListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_design_top_toolbar, container, false);
        saveDesign=view.findViewById(R.id.button_save_design);
        discardDesign=view.findViewById(R.id.button_discard_design);
        flipHorizontally=view.findViewById(R.id.button_flip_horizontally);
        flipVertically=view.findViewById(R.id.button_flip_vertically);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel.observeElements().observe(getViewLifecycleOwner(),elements -> {
            updateButtonsState(elements.hasSelectedElement());
        });
        flipVertically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElementList elements=viewModel.getElements();
                try {
                    elements.getSelectedElement().verticallyFlip();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewModel.setElements(elements);
            }
        });
        flipHorizontally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElementList elements=viewModel.getElements();
                try {
                    elements.getSelectedElement().horizontallyFlip();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewModel.setElements(elements);
            }
        });

        saveDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDesignListener.saveDesign();
            }
        });

        discardDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discardDesignListener.discardDesign();
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

    }


    private void updateButtonsState(Boolean hasSelectedElement) {
        if (!hasSelectedElement) {
            flipHorizontally.setEnabled(false);
            flipHorizontally.setColorFilter(Color.GRAY);
            flipVertically.setEnabled(false);
            flipVertically.setColorFilter(Color.GRAY);
            Log.d("TAG", "updateButtonsState: ");
        } else {
            flipHorizontally.setEnabled(true);
            flipHorizontally.clearColorFilter();
            flipVertically.setEnabled(true);
            flipVertically.clearColorFilter();
        }

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        saveDesignListener=(SaveDesignListener) context;
        discardDesignListener=(DiscardDesignListener)context;
    }

}
