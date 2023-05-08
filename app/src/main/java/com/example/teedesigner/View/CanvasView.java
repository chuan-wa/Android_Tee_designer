package com.example.teedesigner.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.teedesigner.Controller.CanvasController;
import com.example.teedesigner.Elements.BackgroundBitmapElement;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.SharedViewModel;

public class CanvasView extends CardView {
    private ElementList elements;
    private BackgroundBitmapElement backgroundElement;
    private SharedViewModel viewModel;
    //private Element selectedElement;
    private float downX;
    private float downY;
    private boolean isDesignEmpty;
    private RectF designRect;

    //private String operation;
    private CanvasController canvasController=new CanvasController();
    public CanvasView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public void setCanvasController(CanvasController canvasController){
        this.canvasController=canvasController;
    }
    public void setElements(ElementList elements){
        this.elements = elements;
        canvasController.setElements(elements);
        invalidate();
    }
    public void setBackgroundElement(BackgroundBitmapElement backgroundElement){
        this.backgroundElement=backgroundElement;
        invalidate();
    }

    public void setViewModelStoreOwner(ViewModelStoreOwner viewModelStoreOwner){
        viewModel = new ViewModelProvider(viewModelStoreOwner).get(SharedViewModel.class);
        canvasController.setViewModelStoreOwner(viewModelStoreOwner);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        canvasController.setElements(elements);
        if(this.elements.size()==0) return false;
        try {
            canvasController.handleTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(canvas==null) Log.w("onDraw", ": is null");
        super.onDraw(canvas);
        if(backgroundElement!=null)backgroundElement.draw(canvas);
        if(isDesignEmpty){
            Paint paint=new Paint();
            paint.setColor(Color.GREEN);
            canvas.drawRect(designRect,paint);
        }
        if(elements.size()==0)return;
        elements.draw(canvas);
        //Log.d("TAG", "onDraw: "+canvas.getWidth());
    }


    public ElementList getElements(){
        return this.elements;
    }

    public void setDesignEmpty(boolean designEmpty) {
        isDesignEmpty = designEmpty;
        invalidate();
    }

    public void setDesignRect(RectF designRect){
        this.designRect=designRect;
    }

}
