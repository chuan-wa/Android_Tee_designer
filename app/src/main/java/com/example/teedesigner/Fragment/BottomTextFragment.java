package com.example.teedesigner.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.teedesigner.Elements.TextElement;
import com.example.teedesigner.R;
import com.example.teedesigner.listener.NotifyColorChangeListener;
import com.rtugeek.android.colorseekbar.ColorSeekBar;
import com.rtugeek.android.colorseekbar.OnColorChangeListener;

import org.jetbrains.annotations.Nullable;

public class BottomTextFragment extends Fragment {
    private TextElement te;
/*    private ImageButton exit;
    private ImageButton complete;*/
    private ColorSeekBar colorSeekBar;
    private NotifyColorChangeListener listener;
    public BottomTextFragment (TextElement te){
        this.te=te;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_bottom_text_tool,container,false);
/*        exit=view.findViewById(R.id.exitEditing);
        complete=view.findViewById(R.id.completeEditing);*/
        colorSeekBar=view.findViewById(R.id.colorSeekBar);
        colorSeekBar.setOnColorChangeListener(new OnColorChangeListener() {
            @Override
            public void onColorChangeListener(int progress, int color) {
                te.setTextColor(color);
                listener.colorChange();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener=(NotifyColorChangeListener) context;
    }
}
