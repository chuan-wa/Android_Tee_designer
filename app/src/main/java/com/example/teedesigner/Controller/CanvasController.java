package com.example.teedesigner.Controller;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.teedesigner.Elements.Element;
import com.example.teedesigner.Elements.ElementButtons.ElementButton;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.FinishBitmapElement;
import com.example.teedesigner.Elements.PictureBitmapElement;
import com.example.teedesigner.Elements.TextElement;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.listener.ClickBlankPreviewCanvasListener;

import org.w3c.dom.Text;

public class CanvasController {
    private ElementList elements;
    private String operation;
    private boolean isRunning;
    private SharedViewModel viewModel;
    private float downX;
    private float downY;
    ChangeImageListener changeImageListener;
    ChangeTextListener changeTextListener;
    ChangeDesignListener changeDesignListener;
    LayerListener layerListener;
    DeleteListener deleteListener;
    public interface ChangeImageListener{
        void changeImage();
    }

    public interface ChangeTextListener{
        void changeText();
    }
    public interface ChangeDesignListener{
        void changeDesign();
    }
    public interface LayerListener{
        void canLevelDown(boolean is);
    }
    public interface DeleteListener{
        void deleteElement(ElementList elements);
    }

    public void setChangeImageListener(ChangeImageListener listener){
        changeImageListener=listener;
    }
    public void setChangeTextListener(ChangeTextListener listener){
        changeTextListener=listener;
    }
    public void setChangeDesignListener(ChangeDesignListener listener){
        changeDesignListener=listener;
    }

    public CanvasController(){}
    public CanvasController(ChangeImageListener changeImageListener,ChangeTextListener changeTextListener,ChangeDesignListener changeDesignListener,LayerListener layerListener,DeleteListener deleteListener){
        this.changeDesignListener=changeDesignListener;
        this.changeImageListener=changeImageListener;
        this.changeTextListener=changeTextListener;
        this.layerListener=layerListener;
        this.deleteListener=deleteListener;
    }


    public void setElements(ElementList elements){
        this.elements=elements;
    }
    public void setViewModelStoreOwner(ViewModelStoreOwner viewModelStoreOwner){
        viewModel=new ViewModelProvider(viewModelStoreOwner).get(SharedViewModel.class);
    }

    public String getOperation() {
        return this.operation;
    }
    public void notOperated(){
        this.operation="notOperated";
    }
    public void setRunning(){
        isRunning=!isRunning;
    }
    public boolean Running(){
        return isRunning;
    }


    public void handleTouchEvent(MotionEvent event) throws Exception {

        if (isRunning==true)return;
        if(elements.size()==0)return;
        //setRunning();
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mouseDown(event.getX(),event.getY());
                downX=event.getX();downY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mouseMove(event.getX()-downX,event.getY()-downY);
                downX=event.getX();downY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mouseUp();
                break;
        }
        //setRunning();
    }



    private void mouseDown(float x, float y) throws Exception {
        setRunning();
        notOperated();
        //preview process
        if(elements.hasSelectedElement()){
            Element selectedElement=elements.getSelectedElement();
            //click button
            if(selectedElement.isButtonPressed(x,y)){
                ElementButton pressedButton=selectedElement.getPressedButton(x,y);
                operation=pressedButton.getName();
            }
            //not press button but click the element itself
            else if(selectedElement.containsPoint(x,y)){
                operation ="drag";
            }
            //not interact with the selected element
            else{
                selectedElement.setSelected(false);
                elements.setSelectedElement(x,y);
                operation="drag";
            }

        }else{
            elements.setSelectedElement(x,y);
            operation ="drag";
        }
        setRunning();
        viewModel.setElements(elements);
        //Log.d("TAG", "mouseDown: ");


    }

    private void mouseMove(float x,float y) throws Exception {
        setRunning();
        if(!elements.hasSelectedElement()){
            //dont forget to declare the end
            setRunning();
            return;
        }
        Element selectedElement=elements.getSelectedElement();
        switch (operation){
            case "drag":
                move(x,y,selectedElement);
                //Log.d("TAG", "mouseMove");

                break;

            case "scaleAndRotateButton":
                scaleAndRotate(x,y,selectedElement);
                //Log.d("TAG", "scale ");

                break;

            case "notOperated":

                break;


            default:
                Log.w("TAG", "mouseMove: Invalid operation:"  + operation );
                break;

        }
        setRunning();
    }

    private void mouseUp() throws Exception {
        setRunning();
        if(!elements.hasSelectedElement()){
            //dont forget to declare the end
            setRunning();
            return;
        }
        Element selectedElement=elements.getSelectedElement();
        switch (operation){
            case "scaleAndRotateButton":
                //for(int i=0;i<100;i++)scaleAndRotate(10,10,selectedElement);
                //Log.d("TAG", "Smaller value "+selectedElement.getScaleFactor()+"  "+selectedElement.getRotation());

                //Log.d("TAG", "scale ");
                //scaleAndRotate(10,10,selectedElement);
                break;

            case "changeButton":
                if(selectedElement.getClass()==PictureBitmapElement.class){
                    changeImageListener.changeImage();
                }else if(selectedElement.getClass()== TextElement.class){
                    changeTextListener.changeText();
                }else if(selectedElement.getClass()== FinishBitmapElement.class){
                    changeDesignListener.changeDesign();
                }
                break;
                //scaleAndRotate(1000,1000,selectedElement);
                //Log.d("TAG", "larger value "+selectedElement.getScaleFactor()+"  "+selectedElement.getRotation());
            case "deleteButton":
                deleteListener.deleteElement(elements);
                break;
            case "layerLevelButton":
                //Log.d("TAG", "layerLevelButton");
                layerListener.canLevelDown(elements.leveldownElement());
                break;
            default:

                Log.w("TAG", "mouseMove: Invalid operation:"  + operation );
                break;
        }
        viewModel.setElements(elements);
        notOperated();
        setRunning();
    }

    private void triggerDesignActivity(){
        //if click the right area, designActivity will be activated
        
    }

    private void move(float deltaX,float deltaY,Element selectedElement){
        //Log.d("TAG", String.valueOf(selectedElement.getX()));
        selectedElement.setX(selectedElement.getX()+deltaX);
        //Log.d("TAG", String.valueOf(selectedElement.getX()));
        selectedElement.setY(selectedElement.getY()+deltaY);
    }

    private void scaleAndRotate(float deltaX,float deltaY,Element selectedElement){
        RectF rect=selectedElement.getRect();
        Log.d("Right and Left", rect.right+" "+ rect.left);
        Log.d("Bottom and Top", rect.bottom+" "+rect.top);
        PointF movement = new PointF(deltaX,deltaY);
        float vectorX=rect.right-rect.left;
        float vectorY=rect.bottom-rect.top;


        // Calculate the projection of the movement on the distance of opposite corners
        // cannot use getRotateMatrix, it is rotating point by the center not the vector
        // Math.sin or cos use radian number not degree number!!!
        float radians= (float) Math.toRadians(selectedElement.getRotation());
        PointF diagonal = new PointF((float) (vectorX*Math.cos(radians)-vectorY*Math.sin(radians)), (float) (vectorX*Math.sin(radians)+vectorY*Math.cos(radians)));
        //Log.d("TAG", "afterX: "+(vectorX*Math.cos(getRotation())-vectorY*Math.sin(getRotation()))+"afterY: "+diagonal.y);
        PointF originalVector=new PointF(diagonal.x/2f,diagonal.y/2f);
        PointF changedVector=new PointF(originalVector.x+movement.x,originalVector.y+movement.y);
        Log.d("originalVector", originalVector.x+" "+originalVector.y);
        Log.d("changedVector", changedVector.x+" "+changedVector.y);
        float degree=calculateVectoreDegree(originalVector.x,originalVector.y,changedVector.x,changedVector.y);
        Log.d("degree", ""+degree);
        selectedElement.setRotation(selectedElement.getRotation()+degree);

        //first rotate, then scale
        //used to apply projection, but now use sin/cos because rotate will affect scale
        //look the triangle on the ipad
        //just take the advantage of vector
        double orgDis=  Math.sqrt(Math.pow(originalVector.x,2)+Math.pow(originalVector.y,2));
        double newDis=  Math.sqrt(Math.pow(changedVector.x,2)+Math.pow(changedVector.y,2));
        Log.d("orgDis", ""+orgDis);
        Log.d("newDis", ""+newDis);
        selectedElement.setScaleFactor((float) (selectedElement.getScaleFactor()*(newDis/orgDis)));



    }

    private float calculateVectoreDegree(float x1, float y1,float x2,float y2){
        double dotProduct = x1 * x2 + y1 * y2;
        double vector1Magnitude = Math.sqrt(x1 * x1 + y1 * y1);
        double vector2Magnitude = Math.sqrt(x2 * x2 + y2 * y2);
        double cosTheta = dotProduct / (vector1Magnitude * vector2Magnitude);
        //small rounding errors when performing floating point calculations
        cosTheta=Math.min(cosTheta,1);
        cosTheta=Math.max(-1,cosTheta);

        //double value =  ((double)(x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2)));
        Log.d("value", ""+cosTheta);
        float angle = (float) Math.toDegrees(Math.acos(cosTheta));
        //用外积判断正负
        if((x1*y2-y1*x2)<0)return -angle;
        return angle;
    }

    public void setElements(){
        viewModel.setElements(elements);
    }


}
