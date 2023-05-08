package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.teedesigner.Controller.CanvasController;
import com.example.teedesigner.Elements.BackgroundBitmapElement;
import com.example.teedesigner.Elements.BackgroundPathElement;
import com.example.teedesigner.Elements.Element;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.PictureBitmapElement;
import com.example.teedesigner.GlobalData;
import com.example.teedesigner.R;
import com.example.teedesigner.View.CanvasView;
import com.example.teedesigner.listener.ClickBlankPreviewCanvasListener;

import java.util.HashMap;
import java.util.Map;


public class PreviewMidCanvas extends Fragment {
    private CanvasView canvasView;
    private final Map<String, Element> backgroundElements=new HashMap<String, Element>();
    private Map<String, ElementList> designElements =new HashMap<String,ElementList>(){{
        put(FRONT,new ElementList());
        put(BACK,new ElementList());
        put(LEFT,new ElementList());
        put(RIGHT,new ElementList());
    }};
    //standard is 14 inch to 350 dp
    //android:layout_width="350dp"
    //android:layout_height="400dp"
    private PointF center;
    private Map<String, RectF> rectMap=new HashMap<>();
    //standard is 4 inch to 350 dp
    private final Map<String, RectF> printSizeRect =new HashMap<>();
    private Map<String,String> printSizeString=new HashMap<>();

    private String loc;
    private RectF rectL;
    private final String FRONT="FRONT";
    private final String BACK="BACK";
    private final String LEFT="LEFT";
    private final String RIGHT="RIGHT";
    private float sizeFactor=1/3f;
    private String background;
    private ClickBlankPreviewCanvasListener clickBlankPreviewCanvasListener;
    private CanvasController controller;

    private CanvasController.ChangeImageListener changeImageListener;
    private CanvasController.ChangeDesignListener changeDesignListener;
    private CanvasController.ChangeTextListener changeTextListener;
    private CanvasController.LayerListener layerListener;
    private CanvasController.DeleteListener deleteListener;


    public Map<String, RectF> getPrintSizeRect() {
        return printSizeRect;
    }



    public float getSizeFactor() {
        switch (printSizeString.get(loc)){
            case "12 X 14 inch":
                sizeFactor=1/3f;
                break;
            case "14 X 16 inch":
                sizeFactor=1/3f;
                break;
            case "16 X 18 inch":
                sizeFactor=1/3f;
                break;
            case "4 X 4 inch":
                sizeFactor=1/3f;
                break;
        }
        return sizeFactor;
    }

    public PointF getDeviation(){
        PointF pointF=new PointF(0,0);
        switch (printSizeString.get(loc)){
            case "12 X 14 inch":
                pointF.x=(float) (getResources().getDimension(R.dimen.dp_300)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_350)*(1-GlobalData.inch12XFactor)/2f);
                pointF.y=(float) (getResources().getDimension(R.dimen.dp_350)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_400)*(1-GlobalData.inch12YFactor)/2f);
                break;
            case "14 X 16 inch":
                break;
            case "16 X 18 inch":
                pointF.x=(float) (getResources().getDimension(R.dimen.dp_400)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_350)*(1-GlobalData.inch16XFactor)/2f);
                pointF.y=(float) (getResources().getDimension(R.dimen.dp_450)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_400)*(1-GlobalData.inch16YFactor)/2f);
                break;
            case "4 X 4 inch":
                pointF.x=(float) (getResources().getDimension(R.dimen.dp_300)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_350)*(1-GlobalData.inch4XFactor)/2f);
                pointF.y=(float) (getResources().getDimension(R.dimen.dp_300)*getSizeFactor())-(getResources().getDimension(R.dimen.dp_400)*(1-GlobalData.inch4YFactor)/2f);
                break;
        }
        return pointF;
    }


    public void setBackground(String background) {
        this.background = background;
        setBackgroundElements();
    }

    public String getBackground() {
        return background;
    }

    public void setParas(String background,Map<String, String> printSizeString,Map<String, ElementList> designBitmapElements){
        setBackground(background);
        setPrintSizeRect(printSizeString);
        setDesignPathElements(designBitmapElements);
        refresh();
    }

    public void setPrintSizeRect(Map<String, String> printSizeString) {
        this.printSizeString=printSizeString;
        for(Map.Entry<String,String> entry:printSizeString.entrySet()){
            this.printSizeRect.put(entry.getKey(), rectMap.get(entry.getValue()));
        }
        //refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeRectMap();
        initilaizePrintSizeMap();
        View view=inflater.inflate(R.layout.fragment_preview_mid_canvas, container, false);
        //view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        canvasView=view.findViewById(R.id.previewCanvas);
        controller=new CanvasController(changeImageListener,changeTextListener,changeDesignListener,layerListener,deleteListener);
        canvasView.setCanvasController(controller);
        canvasView.setViewModelStoreOwner(requireActivity());
        setBackground("white");
        setLoc("FRONT");
        //TO DO: add background Elements
        view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        Log.d("TAG", "onTouch: "+event.getX());
                        if(printSizeRect.get(loc).contains(event.getX(),event.getY())&& designElements.get(loc).size()==0){
                            //start design activity
                            clickBlankPreviewCanvasListener.clickBlankPreviewCanvas();
                        }
                        break;
                }


                return true;
            }
        });

        return view;
    }

    private void initializeRectMap(){
        center=new PointF(dpToPx(350/2f),dpToPx(400/2f));
        rectMap= new HashMap<String, RectF>() {{
            float inchXF12 = GlobalData.inch12XFactor;
            float inchYF12 = GlobalData.inch12YFactor;
            put("12 X 14 inch", new RectF(center.x*(1- inchXF12),center.y*(1- inchYF12),center.x*(1+ inchXF12),center.y*(1+ inchYF12)));
            float inchXF14 = GlobalData.inch14XFactor;
            float inchYF14 = GlobalData.inch14YFactor;
            put("14 X 16 inch", new RectF(center.x*(1- inchXF14),center.y*(1- inchYF14),center.x*(1+ inchXF14),center.y*(1+ inchYF14)));
            float inchXF16 = GlobalData.inch16XFactor;
            float inchYF16 = GlobalData.inch16YFactor;
            put("16 X 18 inch", new RectF(center.x*(1- inchXF16),center.y*(1- inchYF16),center.x*(1+ inchXF16),center.y*(1+ inchYF16)));
            float inchXF4=GlobalData.inch4XFactor;
            float inchYF4=GlobalData.inch4YFactor;
            put("4 X 4 inch", new RectF(center.x*(1- inchXF4),center.y*(1- inchYF4),center.x*(1+ inchXF4),center.y*(1+ inchYF4)));
        }};

    }
    private int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getActivity().getResources().getDisplayMetrics());
    }

    private void initilaizePrintSizeMap(){
        printSizeRect.put(FRONT,rectMap.get("12 X 14 inch"));
        printSizeRect.put(BACK,rectMap.get("12 X 14 inch"));
        printSizeRect.put(LEFT,rectMap.get("12 X 14 inch"));
        printSizeRect.put(RIGHT,rectMap.get("12 X 14 inch"));
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstance) {
        super.onViewCreated(view,savedInstance);
    }



    public void setBackgroundElements(){
        switch (background){
            case "sample":
                backgroundElements.put("FRONT",new BackgroundPathElement(getResources(),R.drawable.sample1));
                backgroundElements.put("BACK",new BackgroundPathElement(getResources(),R.drawable.sample2));
                backgroundElements.put("LEFT",new BackgroundPathElement(getResources(),R.drawable.sample3));
                backgroundElements.put("RIGHT",new BackgroundPathElement(getResources(),R.drawable.sample4));
                break;
            case "white":
                backgroundElements.put("FRONT",new BackgroundPathElement(getResources(),R.drawable.white1));
                backgroundElements.put("BACK",new BackgroundPathElement(getResources(),R.drawable.white2));
                backgroundElements.put("LEFT",new BackgroundPathElement(getResources(),R.drawable.white3));
                backgroundElements.put("RIGHT",new BackgroundPathElement(getResources(),R.drawable.white4));
                break;

            case "black":
                backgroundElements.put("FRONT",new BackgroundPathElement(getResources(),R.drawable.black1));
                backgroundElements.put("BACK",new BackgroundPathElement(getResources(),R.drawable.black2));
                backgroundElements.put("LEFT",new BackgroundPathElement(getResources(),R.drawable.black3));
                backgroundElements.put("RIGHT",new BackgroundPathElement(getResources(),R.drawable.black4));
                break;

            default:
                Log.w("setBackgroundElements: ", "no such T-shirts");
        }
        //refresh();
    }
    public void setDesignPathElements(Map<String, ElementList> designBitmapElements){
        this.designElements = designBitmapElements;
    }

    public void setLoc(String loc) {
        this.loc=loc;
        setView();
    }

    public void refresh(){
        setView();
    }

    public void setRect(RectF rectL){
        this.rectL=rectL;
    }

    public RectF getRectL() {
        return printSizeRect.get(loc);
    }

    public PointF getCenter(){return new PointF(canvasView.getLayoutParams().width/2,canvasView.getLayoutParams().height/2);}

    private void setView() {
        prepareFinishElement();
        prepareBackgroundElement();
        prepareRect();
        canvasView.setDesignEmpty(getCurrentPathElements().size() == 0);
    }

    private void prepareRect(){
        canvasView.setDesignRect(printSizeRect.get(loc));
    }

    public void setCrossView(){

    }

    public String getLoc(){
        return loc;
    }


    private ElementList getCurrentPathElements(){
        //clear other bitmaps
        if(designElements.get(loc).size()==0){return new ElementList();}
        if(PictureBitmapElement.class != designElements.get(loc).get(0).getClass()){
            for(Map.Entry<String, ElementList> entry: designElements.entrySet()){
                if(entry.getValue().size()==0)continue;
                if(entry.getValue().get(0).getClass()==PictureBitmapElement.class)entry.setValue(entry.getValue().transformBitmapToPath());
            }
            ElementList bitmapTemp= designElements.get(loc).transformPathToBitmap();
            designElements.put(getLoc(),bitmapTemp);

        }
        return designElements.get(loc);
    }

    private void prepareFinishElement(){
        canvasView.setElements(getCurrentPathElements());
    }

    private void prepareBackgroundElement(){
        if(backgroundElements.get(loc)==null) Log.w("addBackgroundElement: ", "background is null");
        //clear last bitmap
        if(backgroundElements.get(loc).getClass()!=BackgroundBitmapElement.class){
            for(Map.Entry<String,Element> entry:backgroundElements.entrySet()){
                if(entry.getValue().getClass()==BackgroundBitmapElement.class)entry.setValue(new BackgroundPathElement((BackgroundBitmapElement) entry.getValue()));
            }
            backgroundElements.put(loc,new BackgroundBitmapElement((BackgroundPathElement) backgroundElements.get(loc)));
        }
        canvasView.setBackgroundElement((BackgroundBitmapElement) backgroundElements.get(loc));
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        clickBlankPreviewCanvasListener=(ClickBlankPreviewCanvasListener) getActivity();
        changeDesignListener=(CanvasController.ChangeDesignListener) context;
        changeImageListener=(CanvasController.ChangeImageListener) context;
        changeTextListener=(CanvasController.ChangeTextListener) context;
        layerListener=(CanvasController.LayerListener) context;
        deleteListener=(CanvasController.DeleteListener) context;
    }

    //this should in low quality, high quality will lower the speed
    public Bitmap getPreviewBitmap(String loc){
        Bitmap bitmap=Bitmap.createBitmap(canvasView.getLayoutParams().width,canvasView.getLayoutParams().height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        //this part can be slower, we transform path to bitmap instead of keeping the bitmap
        if (backgroundElements.get(loc).getClass() == BackgroundBitmapElement.class) {
            backgroundElements.get(loc).draw(canvas);
        }else{
            BackgroundBitmapElement bbElemnt=new BackgroundBitmapElement((BackgroundPathElement) backgroundElements.get(loc));
            bbElemnt.draw(canvas);
        }
        ElementList bitmapElements=designElements.get(loc).transformPathToBitmap();
        bitmapElements.drawWithoutSelection(canvas);
        Bitmap temp= bitmap.copy(bitmap.getConfig(),false);
        Bitmap result=Bitmap.createScaledBitmap(temp,100,110,true);
        bitmap.recycle();
        return result;
    }







}