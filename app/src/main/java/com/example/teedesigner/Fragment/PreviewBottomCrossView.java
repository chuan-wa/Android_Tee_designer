package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.teedesigner.R;
import com.example.teedesigner.listener.ClickCrossView;

public class PreviewBottomCrossView extends Fragment {
    private ImageView imageViewFront;
    private ImageView imageViewBack;
    private ImageView imageViewLeft;
    private ImageView imageViewRight;
    private ClickCrossView clickCrossView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_preview_bottom_cross_view, container, false);
        imageViewFront=view.findViewById(R.id.TeesFront);imageViewBack=view.findViewById(R.id.TeesBack);imageViewLeft=view.findViewById(R.id.TeesLeft);imageViewRight=view.findViewById(R.id.TeesRight);
        //TO DO: set listener
        imageViewFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCrossView.clickFront();
            }
        });
        imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCrossView.clickRight();
            }
        });
        imageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCrossView.clickLeft();
            }
        });
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCrossView.clickBack();
            }
        });




        return view;
    }
    private void setImageViewFront(Bitmap bitmap){
        imageViewFront.setImageBitmap(bitmap);
    }
    private void setImageViewBack(Bitmap bitmap){
        imageViewBack.setImageBitmap(bitmap);
    }
    private void setImageViewLeft(Bitmap bitmap){
        imageViewLeft.setImageBitmap(bitmap);
    }
    private void setImageViewRight(Bitmap bitmap){
        imageViewRight.setImageBitmap(bitmap);
    }

    public void setImageView(Bitmap bitmap, String loc){
        switch (loc){
            case "FRONT":
                setImageViewFront(bitmap);
                break;
            case "BACK":
                setImageViewBack(bitmap);
                break;
            case "LEFT":
                setImageViewLeft(bitmap);
                break;
            case "RIGHT":
                setImageViewRight(bitmap);
                break;
            default:
                Log.w("setImageView", " no such location "+loc);
                break;
        }

    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        clickCrossView=(ClickCrossView) context;

    }

}