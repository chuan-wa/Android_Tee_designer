package com.example.teedesigner.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.teedesigner.Controller.CanvasController;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.FinishPathElement;
import com.example.teedesigner.Elements.PictureBitmapElement;
import com.example.teedesigner.Elements.TextElement;
import com.example.teedesigner.Fragment.DesignCanvasFragment;
import com.example.teedesigner.Fragment.DesignFragment;
import com.example.teedesigner.Fragment.ImageGalleryFragment;
import com.example.teedesigner.Fragment.ImageListFragment;
import com.example.teedesigner.R;
import com.example.teedesigner.Service.GsonService;
import com.example.teedesigner.Service.ImageEditService;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.View.CanvasView;
import com.example.teedesigner.listener.AddImageListener;
import com.example.teedesigner.listener.ClickAddTextButtonListener;
import com.example.teedesigner.listener.ClickAddImageButtonListener;
import com.example.teedesigner.listener.ClickImageListListener;
import com.example.teedesigner.listener.NotifyColorChangeListener;
import com.example.teedesigner.listener.UploadImageListener;
import com.example.teedesigner.listener.DiscardDesignListener;
import com.example.teedesigner.listener.SaveDesignListener;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class DesignActivity extends AppCompatActivity implements NotifyColorChangeListener, ClickAddTextButtonListener, ClickAddImageButtonListener, ClickImageListListener, AddImageListener, UploadImageListener, SaveDesignListener, DiscardDesignListener, CanvasController.ChangeImageListener, CanvasController.ChangeTextListener, CanvasController.ChangeDesignListener, CanvasController.LayerListener, CanvasController.DeleteListener {
    private SharedViewModel viewModel;
    private ElementList elements = new ElementList();
    private DesignFragment designFragment;
    private ImageListFragment imageListFragment;
    private ImageGalleryFragment imageGalleryFragment;
    private String previousPath;
    private final String TAG = "DesignActivity";
    private String printSizeArea = "default";
    private String m_text="";
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;

        elements = GsonService.getBitmapElementList(getIntent().getStringExtra("pathElements"));
        try {
            elements.compressedBitmaps(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printSizeArea = getIntent().getStringExtra("printAreaSize");
        //if(elements==null)elements=new ElementList();
        setContentView(R.layout.activity_design);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        designFragment = new DesignFragment(printSizeArea);
        imageListFragment = new ImageListFragment();


        fragmentTransaction.add(R.id.fragment_fullscreen_container, designFragment);
        fragmentTransaction.commit();
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        //initialize element list
/*        TextElement textElement = new TextElement("Hello World",100, Color.RED,200,200);
        //textElement.verticallyFlip();
        elements.add(textElement);*/

        //let viewModel hold the elements list
        viewModel.setElements(elements);
        viewModel.observeElements().observe(this, elements -> {
            //Log.d("TAG", "Element List is changed");
        });

/*        bottomToolbarFragment.setAddImageClickLisenter{
            getSupportFragmentManager().tra
        }*/

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        Log.d("TAG", "onActivityResult: " + uri);
        Glide.with(this).load(uri).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                String imagePath = ImageEditService.saveImage(bitmap, null, "/userImgs");
                Log.d("TAG", "onResourceReady: " + imagePath);
                ElementList elements = viewModel.getElements();
                PictureBitmapElement pe;
                if (elements.hasSelectedElement()) {
                    pe=(PictureBitmapElement) elements.getSelectedElement();
                    Bitmap pre=pe.getBitmap();
                    pe.setImgSrc(imagePath);
                    Bitmap beforeCompressed=pe.getBitmap();
                    try {
                        pe.setBitmap(new Compressor(context).compressToBitmap(new File(imagePath)));
                        pe.setCompressFactor(beforeCompressed.getHeight()/pe.getBitmap().getHeight());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pe.setScaleFactor(Math.min((pre.getHeight()/pe.getBitmap().getHeight()),pre.getWidth()/pe.getBitmap().getWidth())*pe.getScaleFactor());
                    pe.setPosition(-pe.getWidth()/2+getResources().getDimension(R.dimen.dp_350)/2,-pe.getHeight()/2+getResources().getDimension(R.dimen.dp_350)/2);
                }else{
                    pe = new PictureBitmapElement(imagePath,0,0);
                    Bitmap beforeCompressed=pe.getBitmap();
                    try {
                        pe.setBitmap(new Compressor(context).compressToBitmap(new File(imagePath)));
                        pe.setCompressFactor(beforeCompressed.getHeight()/pe.getBitmap().getHeight());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pe.setPosition(-pe.getWidth()/2+getResources().getDimension(R.dimen.dp_350)/2,-pe.getHeight()/2+getResources().getDimension(R.dimen.dp_350)/2);
                    pe.setUserPhoto(true);
                    pe.setScaleFactor(getResources().getDimension(R.dimen.dp_150)/pe.getHeight());
                    elements.add(pe);
                }
                viewModel.setElements(elements);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }


    @Override
    public void uploadImage() {
        if (elements.hasSelectedElement()) elements.getSelectedElement().setSelected(false);
        ImagePicker.with(this).crop().start();
    }


    //通过这种方式，选择的元素会被取消,add image只会删除被选择的元素
    @Override
    public void ClickAddImageButton() {
        Log.d("TAG", "addImage: ");
        if (elements.hasSelectedElement()) elements.getSelectedElement().setSelected(false);
        viewModel.setElements(elements);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_fullscreen_container, imageListFragment).addToBackStack("imageList").commit();
    }

    @Override
    public void onClickImageList(ArrayList<String> images) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        imageGalleryFragment = new ImageGalleryFragment(images);
        fragmentTransaction.replace(R.id.fragment_fullscreen_container, imageGalleryFragment).addToBackStack("imageGallery").commit();
    }

    @Override
    public void addImage() {
        //viewModel.setElements(elements);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_fullscreen_container, designFragment).commit();
    }

    //通过这种方式，保留selectedElement
    @Override
    public void changeImage() {
        if (!elements.hasSelectedElement()) Log.w(TAG, "changeImage function is crapped! ");
        PictureBitmapElement pe = (PictureBitmapElement) elements.getSelectedElement();
        if (pe.isUserPhoto()) {
            ImagePicker.with(this).crop().start();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_fullscreen_container, imageListFragment).addToBackStack("imageList").commit();
        }
    }

    @Override
    public void saveDesign() {
        //transform design

        ElementList pathElements;
        if (viewModel.getElements().size() == 0) {
            discardDesign();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("pathElementsDesign", GsonService.encodeElementList(viewModel.getElements()));
        String designPath = saveDesignAsBitmapPath();//save current design
        ElementList designElements = new ElementList();
        //to do: relocate the finishPathElement

        FinishPathElement fe = new FinishPathElement(designPath, 0, 0, null);
        designElements.add(fe);
        //deletePreviousDesign();//delete previous design
        intent.putExtra("designPath", GsonService.encodeElementList(designElements));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void discardDesign() {
        //not transform the design
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    private String saveDesignAsBitmapPath() {
        ElementList savedElements = viewModel.getElements();
        CanvasView canvasView = designFragment.getChildFragmentManager().findFragmentById(R.id.fragment_mid_container).getView().findViewById(R.id.canvas_view);
        Bitmap bitmap = Bitmap.createBitmap(canvasView.getLayoutParams().width, canvasView.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        savedElements.drawWithoutSelection(canvas);
        String path = ImageEditService.saveImage(bitmap, null, "/myDesign");
        bitmap.recycle();
        return path;
    }

    private void deletePreviousDesign() {
        if (previousPath == null) return;
        ImageEditService.deleteImage(previousPath);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗,设计将不会被保存");
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
    public void changeText() {
        makeText();
    }

    @Override
    public void changeDesign() {
        Log.d(TAG, "changeDesign: ");
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
        elements.remove(elements.getSelectedElement());
        Log.d(TAG, "deleteElement: ");
    }

    @Override
    public void addText() {
        if(elements.hasSelectedElement()){
            elements.getSelectedElement().setSelected(false);
            viewModel.setElements(elements);
        }
        makeText();
    }

    @Override
    public void colorChange() {
        DesignCanvasFragment fragment= (DesignCanvasFragment) designFragment.getChildFragmentManager().findFragmentById(R.id.fragment_mid_container);
        fragment.refresh();
    }

    private void makeText(){
        AlertDialog.Builder addText = new AlertDialog.Builder(this);
        addText.setTitle("Please input the text");
        final EditText input=new EditText(this);
        addText.setView(input);

        addText.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_text = input.getText().toString();
                if(elements.hasSelectedElement()){
                    TextElement te= (TextElement) elements.getSelectedElement();
                    te.setText(m_text);
                }else{
                    TextElement te=new TextElement(m_text,100, Color.BLACK,getResources().getDimension(R.dimen.dp_350)/2,getResources().getDimension(R.dimen.dp_400)/2);
                    elements.add(te);
                }
                viewModel.setElements(elements);
            }
        });
        addText.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        addText.show();

    }
}