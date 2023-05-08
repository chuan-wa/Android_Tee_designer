package com.example.teedesigner.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.teedesigner.Controller.CanvasController;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.FinishBitmapElement;
import com.example.teedesigner.Elements.FinishPathElement;
import com.example.teedesigner.Elements.TempStack;
import com.example.teedesigner.Elements.TextElement;
import com.example.teedesigner.Fragment.PreviewBottomCrossView;
import com.example.teedesigner.Fragment.PreviewFragment;
import com.example.teedesigner.Fragment.PreviewMidCanvas;
import com.example.teedesigner.Fragment.PreviewTopToolBar;
import com.example.teedesigner.R;
import com.example.teedesigner.SQLHelper;
import com.example.teedesigner.Service.GsonService;
import com.example.teedesigner.Service.ImageEditService;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.listener.ChangeColorListener;
import com.example.teedesigner.listener.ClickBlankPreviewCanvasListener;
import com.example.teedesigner.listener.ClickCrossView;
import com.example.teedesigner.listener.DiscardPreviewListener;
import com.example.teedesigner.listener.SavePreviewListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PreviewActivity extends AppCompatActivity implements ClickBlankPreviewCanvasListener, ClickCrossView, SavePreviewListener, DiscardPreviewListener, ChangeColorListener, CanvasController.ChangeImageListener, CanvasController.ChangeTextListener, CanvasController.ChangeDesignListener, CanvasController.LayerListener, CanvasController.DeleteListener {
    //用savedInstanceState儲存重要數據
    private final String TAG = "PreviewActivity";
    private TempStack tempStack=new TempStack();
    private Map<String, ElementList> eCombineMap = new HashMap<String, ElementList>();
    private Map<String, ElementList> eListMap = new HashMap<String, ElementList>();
    private Map<String, String> printSizeMap = new TreeMap<>();
    private SharedViewModel viewModel;
    private final String FRONT = "FRONT";
    private final String BACK = "BACK";
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    private String background = "black";
    private PreviewFragment previewFragment;
    private PreviewMidCanvas previewMidCanvas;
    private PreviewBottomCrossView previewBottomCrossView;
    private PreviewTopToolBar previewTopToolBar;
    private ActivityResultLauncher<Intent> designActivityResultLauncher;
    private SQLHelper sqlHelper;
    private int id = -1;
    private String preImagePath = null;
    private Context context;
    private String m_text_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eCombineMap.put(FRONT, new ElementList());
        eCombineMap.put(RIGHT, new ElementList());
        eCombineMap.put(BACK, new ElementList());
        eCombineMap.put(LEFT, new ElementList());
        eListMap.put(FRONT, new ElementList());
        eListMap.put(RIGHT, new ElementList());
        eListMap.put(BACK, new ElementList());
        eListMap.put(LEFT, new ElementList());
        printSizeMap.put(FRONT, "16 X 18 inch");
        printSizeMap.put(BACK, "14 X 16 inch");
        printSizeMap.put(LEFT, "4 X 4 inch");
        printSizeMap.put(RIGHT, "4 X 4 inch");
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        //if there is a intent from DataActivity
        if (id != -1) {
            eCombineMap = GsonService.decodeElementMap(intent.getStringExtra("eCombineMap"));
            eListMap = GsonService.decodeElementMap(intent.getStringExtra("eListMap"));
            background = intent.getStringExtra("background");
            printSizeMap = GsonService.decodeStringMap(intent.getStringExtra("printSize"));
            preImagePath = intent.getStringExtra("preImagePath");
            m_text_name = intent.getStringExtra("name");
        }

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        //initialize element list
/*        TextElement textElement = new TextElement("Hello World",100, Color.BLACK,200,200);
        //textElement.verticallyFlip();
        elements.add(textElement);*/

        //let viewModel hold the elements list
        viewModel.observeElements().observe(this, elements -> {
            //refresh cross view
            String loc = getLoc();
            eCombineMap.put(loc, viewModel.getElements());
            previewMidCanvas.setParas(background, printSizeMap, eCombineMap);
            Bitmap bitmap = previewMidCanvas.getPreviewBitmap(loc).copy(previewMidCanvas.getPreviewBitmap(loc).getConfig(), true);
            previewBottomCrossView.setImageView(bitmap, loc);
            //bitmap.recycle();
            Log.d("TAG", "Element List is changed");
        });
        //initial designPathElements


        //consider starting this activity from the MYDesign Activity
        context = this;
        setContentView(R.layout.activity_preview);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        previewFragment = new PreviewFragment(printSizeMap, background);
        fragmentTransaction.add(R.id.fragment_fullscreen_preview, previewFragment).commit();
        designActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    //doSomeOperations();
                    Intent intent = result.getData();
                    ElementList pathElements = GsonService.getPathElementList(intent.getStringExtra("pathElementsDesign"));
                    eListMap.put(previewMidCanvas.getLoc(), pathElements);
                    ElementList pathMyDesignElements = GsonService.getPathElementList(intent.getStringExtra("designPath"));
                    FinishPathElement fe = (FinishPathElement) pathMyDesignElements.get(0);
                    fe.setRectCliped(previewMidCanvas.getRectL());
                    fe.setScaleFactor(previewMidCanvas.getSizeFactor());
                    fe.setPosition(-previewMidCanvas.getDeviation().x, -previewMidCanvas.getDeviation().y);
                    String previousPath = null;
                    if (eCombineMap.get(previewMidCanvas.getLoc()).size() != 0) {
                        FinishBitmapElement fePrevious = (FinishBitmapElement) eCombineMap.get(previewMidCanvas.getLoc()).get(0);
                        previousPath = fePrevious.getImgSrc();
                    }

                    eCombineMap.put(previewMidCanvas.getLoc(), pathMyDesignElements);
                    ImageEditService.deleteImage(previousPath);
                    //we can declare finishElement in designActivity

/*                            FinishBitmapElement finishElement=new FinishBitmapElement(designPath,0,0,previewMidCanvas.getRectL());
                            finishElement.setScaleFactor(previewMidCanvas.getSizeFactor());
                            finishDesigns.put(previewMidCanvas.getLoc(),finishElement);*/
                    previewMidCanvas.setParas(background, printSizeMap, eCombineMap);

                }

                if (result.getResultCode() == Activity.RESULT_CANCELED) {

                }
                tempStack.updatePre(getCurrentFe());
                refreshCrossView();

                //previewMidCanvas.refresh();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("eCombineMapGson", GsonService.encodeElementsMap(eCombineMap));
        outState.putString("eListMapGson", GsonService.encodeElementsMap(eListMap));
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveState) {
        super.onRestoreInstanceState(saveState);
        eListMap = GsonService.decodeElementMap(saveState.getString("eListMapGson"));
        eCombineMap = GsonService.decodeElementMap(saveState.getString("eCombineMapGson"));
        previewMidCanvas.setDesignPathElements(eCombineMap);
        previewMidCanvas.refresh();
        onStart();

    }

    @Override
    public void onStart() {
        super.onStart();

        previewMidCanvas = (PreviewMidCanvas) previewFragment.getChildFragmentManager().findFragmentById(R.id.fragment_mid_preview);
        previewMidCanvas.setParas(background, printSizeMap, eCombineMap);
        previewBottomCrossView = (PreviewBottomCrossView) previewFragment.getChildFragmentManager().findFragmentById(R.id.fragment_bottom_preview);
        previewTopToolBar = (PreviewTopToolBar) previewFragment.getChildFragmentManager().findFragmentById(R.id.fragment_top_preview);
        //previewTopToolBar.changeSizeOptions();
        tempStack.updatePre(getCurrentFe());
        refreshCrossView();
    }

    private FinishBitmapElement getCurrentFe(){
        if(eCombineMap.get(getLoc()).size()==0)return null;
        return (FinishBitmapElement) eCombineMap.get(getLoc()).get(0);
    }

    private void refreshCrossView() {
        previewBottomCrossView.setImageView(previewMidCanvas.getPreviewBitmap(FRONT), FRONT);
        previewBottomCrossView.setImageView(previewMidCanvas.getPreviewBitmap(BACK), BACK);
        previewBottomCrossView.setImageView(previewMidCanvas.getPreviewBitmap(LEFT), LEFT);
        previewBottomCrossView.setImageView(previewMidCanvas.getPreviewBitmap(RIGHT), RIGHT);
    }

    private String getLoc() {
        return previewMidCanvas.getLoc();
    }


    @Override
    public void clickBlankPreviewCanvas() {
/*        TextElement textElement = new TextElement("pikapika",100, Color.BLACK,200,200);
        ElementList pathElements=new ElementList();pathElements.add(textElement);
        PicturePathElement picturePathElement=new PicturePathElement("///storage/emulated/0/Pictures/imgs/graphical_samples/animals/PngItem_1238594.png",200,200);
        pathElements.add(picturePathElement);
        //ElementList pathElements=elements.transformBitmapToPath();
        designPathElements.put(previewMidCanvas.getLoc(),pathElements);*/
        modifyEList();
        Gson gson = new Gson();
        //這裏，實裝刪除和變換全體元素的功能
        //to do
        String data = gson.toJson(eListMap.get(previewMidCanvas.getLoc()));
        Intent intent = new Intent(PreviewActivity.this, DesignActivity.class);
        intent.putExtra("pathElements", data);
        intent.putExtra("printAreaSize", printSizeMap.get(getLoc()));
        designActivityResultLauncher.launch(intent);
    }

    private void modifyEList(){
        tempStack.updateNow(getCurrentFe());
        eListMap.get(getLoc()).modifyElements(tempStack.diff(),previewMidCanvas.getSizeFactor());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void clickFront() {
        modifyEList();
        previewTopToolBar.setLoc(FRONT);
        previewMidCanvas.setLoc(FRONT);
        tempStack.updatePre(getCurrentFe());
    }

    @Override
    public void clickBack() {
        modifyEList();
        previewTopToolBar.setLoc(BACK);
        previewMidCanvas.setLoc(BACK);
        tempStack.updatePre(getCurrentFe());
    }

    @Override
    public void clickLeft() {
        modifyEList();
        previewTopToolBar.setLoc(LEFT);
        previewMidCanvas.setLoc(LEFT);
        tempStack.updatePre(getCurrentFe());
        //previewMidCanvas.setRect();
    }

    @Override
    public void clickRight() {
        modifyEList();
        previewTopToolBar.setLoc(RIGHT);
        previewMidCanvas.setLoc(RIGHT);
        tempStack.updatePre(getCurrentFe());
    }


    @Override
    public void discardPreview() {
        AlertDialog isExit = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isExit.setTitle("系统提示");
        // 设置对话框消息
        isExit.setMessage("确定要退出吗");
        // 添加选择按钮并注册监听
        isExit.setButton("确定", listener);
        isExit.setButton2("取消", listener);
        // 显示对话框
        isExit.show();
    }

    @Override
    public void savePreview() {
        modifyEList();
        sqlHelper = new SQLHelper(this);
        AlertDialog.Builder nameDesign = new AlertDialog.Builder(this);
        nameDesign.setTitle("Please name your design");
        final EditText input = new EditText(this);
        nameDesign.setView(input);

        nameDesign.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_text_name = input.getText().toString();
                if (id == -1) {
                    preImagePath = ImageEditService.saveImage(previewMidCanvas.getPreviewBitmap("FRONT"), null, "/preview");
                    sqlHelper.addData(m_text_name, preImagePath, GsonService.encodeElementsMap(eCombineMap), GsonService.encodeElementsMap(eListMap), background, GsonService.encodeStringMap(printSizeMap));
                } else {
                    ImageEditService.deleteImage(preImagePath);
                    preImagePath = ImageEditService.saveImage(previewMidCanvas.getPreviewBitmap("FRONT"), null, "/preview");
                    sqlHelper.updateData(id, m_text_name, preImagePath, GsonService.encodeElementsMap(eCombineMap), GsonService.encodeElementsMap(eListMap), background, GsonService.encodeStringMap(printSizeMap));
                }
                finish();
            }
        });
        nameDesign.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if (id == -1) {
            input.setText("Mydesign");
        } else {
            input.setText(m_text_name);
        }
        nameDesign.show();


    }

    @Override
    public void changeColor(String color) {
        Log.d("TAG", "changeColor: " + color);
        this.background = color;
        onStart();

    }

    @Override
    public void changeSize(String size) {
        Log.d("TAG", "changeSize: " + size);
        String preSize=printSizeMap.get(getLoc());
        printSizeMap.put(getLoc(), size);
        if(eCombineMap.get(getLoc()).size()==0||preSize.equals(size)){
            onStart();
            return;
        }
        clickBlankPreviewCanvas();
    }


    @Override
    public void changeImage() {
        return;
    }

    @Override
    public void changeText() {
        return;
    }

    @Override
    public void changeDesign() {
        clickBlankPreviewCanvas();
    }

    @Override
    public void canLevelDown(boolean is) {
        if (!is) Toast.makeText(this, "already at the bottom", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Level Down Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteElement(ElementList elements) {
        if (!elements.hasSelectedElement()) {
            Log.w(TAG, "deleteElement is crashed ");
            return;
        }

        Log.d(TAG, "deleteElement: ");
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                        elements.remove(elements.getSelectedElement());
                        viewModel.setElements(elements);
                        eListMap.put(getLoc(), new ElementList());
                        previewMidCanvas.setParas(background, printSizeMap, eListMap);
                        break;
                    case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                        break;
                    default:
                        break;
                }
            }
        };
        AlertDialog isDel = new AlertDialog.Builder(this).create();
        // 设置对话框标题
        isDel.setTitle("系统提示");
        // 设置对话框消息
        isDel.setMessage("确定要删除设计吗");
        // 添加选择按钮并注册监听
        isDel.setButton("确定", deleteListener);
        isDel.setButton2("取消", deleteListener);
        // 显示对话框
        isDel.show();
    }

}
