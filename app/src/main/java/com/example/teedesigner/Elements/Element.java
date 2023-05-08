package com.example.teedesigner.Elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;


import com.example.teedesigner.Elements.ElementButtons.ElementButton;

public class Element implements Cloneable {
    private float x;
    private float y;
    private float rotation;
    private float scaleFactor;
    private boolean isSelected;
    private boolean isVerticallyFlip;
    private boolean isHorizontallyFlip;
    private float margin=0f;
    private final ElementButton deleteButton=new ElementButton("deleteButton");
    private final ElementButton scaleAndRotateButton=new ElementButton("scaleAndRotateButton");
    private final ElementButton layerLevelButton=new ElementButton("layerLevelButton");
    private final ElementButton changeButton=new ElementButton("changeButton");

    public Element(float x, float y) {
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.scaleFactor = 1;
        this.isSelected=false;
    }
    @Override
    protected Element clone() throws CloneNotSupportedException{
        return (Element) super.clone();
    }

    public boolean isVerticallyFlip() {
        return isVerticallyFlip;
    }

    public boolean isHorizontallyFlip() {
        return isHorizontallyFlip;
    }

    public float getMargin() {
        return margin;
    }

    public void draw(Canvas canvas){

    }


    public RectF getRect(){
        float centerX=this.x+getWidth()/2f,centerY=this.y+getHeight()/2f;
        //Log.d("TAG", "getHeight: "+getHeight());
        //abs is for the textElement, use minus getHeight
        float left=centerX-scaleFactor*(centerX-this.x+margin),right=centerX+scaleFactor*(centerX-this.x+margin),top=centerY-scaleFactor*Math.abs(centerY-this.y-margin),bottom=centerY+scaleFactor*Math.abs(centerY-this.y-margin);
        //dont let margin affect the scaling accuracy
        //left-=margin;right+=margin;top-=margin;bottom+=margin;

        if(left>=right&&bottom>=top) Log.d("TAG", "The rect is broken.");

        //Log.d("TAG", "left: "+left);
        return new RectF(left,top,right,bottom);
    }

    //also set button positions
    public void setButtonPositions(){
        //PointF[]positions=new PointF[4];
        RectF rect=getRect();
        //positions[0] = new PointF(rect.left , rect.top ); // top left
        this.deleteButton.setButton(rect.left,rect.top);
       // positions[1] = new PointF(rect.right , rect.top ); // top right
        this.changeButton.setButton(rect.right,rect.top);
        //positions[2] = new PointF(rect.left , rect.bottom ); // bottom left
        this.scaleAndRotateButton.setButton(rect.right,rect.bottom);
        //positions[3] = new PointF(rect.right , rect.bottom ); // bottom right
        this.layerLevelButton.setButton(rect.left,rect.bottom);
       // return positions;
    }




    //call setButtonPositions in drawSelection

    protected void drawSelection(Canvas canvas){
        Paint paint= new Paint();paint.setColor(Color.GRAY);paint.setStrokeWidth(4);paint.setStyle(Paint.Style.STROKE);
        RectF rect=getRect();
        TransformCanvas(canvas,true);
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, paint);
        //notice the list, it determines which button will on the top layer
        ElementButton [] buttons=getButtons();
        for (ElementButton button : buttons) {
            button.draw(canvas);
            //canvas.drawCircle(button.getX(), button.getY(), button.getRadius(), paint);
        }
        canvas.restore();


    }

    public float getWidth(){
        return 0;
    }

    public float getHeight(){
        return 0;
    }

    public boolean containsPoint(float touchX, float touchY){
        //rotate change the hit box
        float[] point = transformPoint(touchX,touchY);
        return getRect().contains(point[0], point[1]);
    }

    public boolean isButtonPressed(float touchX,float touchY){
        //same as containsPoint Method
        float[] point = transformPoint(touchX,touchY);
        for(ElementButton button:getButtons()){
            if(button.isClicked(point[0],point[1]))return true;
        }
        return false;
    }

    public ElementButton getPressedButton(float touchX,float touchY) throws Exception {
        if(!isButtonPressed(touchX,touchY))throw new Exception("No button was pressed");
        float[] point = transformPoint(touchX,touchY);

        ElementButton pressedButton = null;
        for(ElementButton button:getButtons()){
            if(button.isClicked(point[0],point[1]))pressedButton=button;
        }
        return pressedButton;
    }

    public float[] transformPoint(float x,float y){
        float[] point = {x,y};
        Matrix inverseMatrix = new Matrix();
        getRotateMatrix().invert(inverseMatrix);
        inverseMatrix.mapPoints(point);
        return point;
    }


    private Matrix getRotateMatrix(){
        float centerX = this.x + getWidth() /2f;
        float centerY = this.y + getHeight() /2f;
        Matrix matrix = new Matrix();
        matrix.postTranslate(-centerX,-centerY);
        matrix.postRotate(getRotation());
        matrix.postTranslate(centerX,centerY);
        return matrix;
    }

    protected void executeInTransformedCanvas(Canvas canvas, boolean extraDraw, Runnable runnable) {
        // Save current canvas state
        canvas.save();
        // Get element's center point
        PointF centerPoint = new PointF(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
        // Apply transformation matrix based on element's position and rotation
        canvas.concat(getRotateMatrix());
        // Translate canvas to element's center point
        canvas.translate(centerPoint.x, centerPoint.y);
        // Scale canvas around element's center point
        if(!extraDraw){
            canvas.scale(getScaleFactor(), getScaleFactor());
            if(isVerticallyFlip)canvas.scale(1f,-1f);
            if(isHorizontallyFlip)canvas.scale(-1f,1f);
        }
        // Translate canvas back to element's original position
        canvas.translate(-centerPoint.x, -centerPoint.y);
        // Execute the provided code
        runnable.run();
        // Restore canvas state
        canvas.restore();
    }

    protected void TransformCanvas(Canvas canvas, boolean extraDraw){
        canvas.save();
        PointF centerPoint = new PointF(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
        // Apply transformation matrix based on element's position and rotation
        canvas.concat(getRotateMatrix());
        // Translate canvas to element's center point
        canvas.translate(centerPoint.x, centerPoint.y);
        // Scale canvas around element's center point
        if(!extraDraw){
            canvas.scale(getScaleFactor(), getScaleFactor());
            if(isVerticallyFlip)canvas.scale(1f,-1f);
            if(isHorizontallyFlip)canvas.scale(-1f,1f);
        }
        // Translate canvas back to element's original position
        canvas.translate(-centerPoint.x, -centerPoint.y);

    }



    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setScaleFactor(float scaleFactor) {
        //avoid the hit box become untouchable
        if(scaleFactor<0){
            this.scaleFactor= -scaleFactor;
            return;
        }
        //very rare situation
        if(scaleFactor==0){
            return;
        }
        this.scaleFactor = scaleFactor;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x){
        this.x=x;
    }

    public void setY(float y){
        this.y=y;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setSelected(boolean isSelected){
        this.isSelected=isSelected;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public ElementButton[] getButtons(){
        setButtonPositions();
        ElementButton [] buttons={deleteButton,changeButton,layerLevelButton,scaleAndRotateButton};
        return buttons;
    }

    public void setVerticallyFlip(boolean verticallyFlip) {
        isVerticallyFlip = verticallyFlip;
    }

    public void setHorizontallyFlip(boolean horizontallyFlip) {
        isHorizontallyFlip = horizontallyFlip;
    }

    public void setMargin(float margin) {
        this.margin = margin;
    }

    public void verticallyFlip(){
        this.isVerticallyFlip=!this.isVerticallyFlip;
    }
    public void horizontallyFlip(){
        this.isHorizontallyFlip=!this.isHorizontallyFlip;
    }


    public void resize(float resizeFactor){
        this.scaleFactor=resizeFactor*scaleFactor;
        this.x=resizeFactor*(x+getWidth()/2f)-getWidth()/2;
        this.y=resizeFactor*(y+getHeight()/2f)-getHeight()/2;
    }



}