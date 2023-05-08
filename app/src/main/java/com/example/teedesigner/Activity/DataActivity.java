package com.example.teedesigner.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.teedesigner.R;
import com.example.teedesigner.SQLHelper;
import com.example.teedesigner.Service.SaveAsTiffService;
import com.example.teedesigner.adapter.BaseAdapter;
import com.example.teedesigner.adapter.MainAdapter;
import com.example.teedesigner.UserDesignModule;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DataActivity extends AppCompatActivity implements OnItemClickListener {
    private SwipeRecyclerView mRecyclerView;
    private BaseAdapter mAdapter;
    private SQLHelper sqlHelper;
    private Context context;
    private ActivityResultLauncher<Intent> previewActivityResultLauncher;

    protected List<UserDesignModule> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        context=this;
        init();


    }

    private void init() {
        sqlHelper = new SQLHelper(this);
        mDataList = sqlHelper.queryData();
        for (UserDesignModule userDesignModule : mDataList) {
            System.out.println(userDesignModule.toString());
        }
//        File pix = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File screenshots = new File(pix, "Screenshots");

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mRecyclerView.setOnItemClickListener(this);

        mAdapter = new MainAdapter(this);
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);
    }

    private final SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(DataActivity.this).setBackground(R.drawable.selector_red)
                        .setText("Delete")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(DataActivity.this).setBackground(R.drawable.selector_green)
                        .setText("Generate TIFF")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。

                SwipeMenuItem editItem = new SwipeMenuItem(DataActivity.this).setBackground(R.drawable.selector_blue)
                        .setText("Edit")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(editItem); // 添加菜单到右侧。
            }
        }
    };

    private final OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                UserDesignModule module = mDataList.get(position);
                switch (menuPosition) {
                    case 0:
/*                        Toast.makeText(DataActivity.this, "第" + (position+1) + "; 条数据点击删除", Toast.LENGTH_SHORT)
                                .show();*/
                        sqlHelper.delete(module.getId());
                        mDataList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(position, mDataList.size());
                        break;
                    case 1:
                        String tifName=module.getDesignName()+"_";
                        String time= String.valueOf(Calendar.getInstance().getTime());
                        tifName=tifName+"_"+time;
                        tifName = tifName.replaceAll(" ","_");
                        tifName = tifName.replaceAll(":","_");
                        SaveAsTiffService tiff=new SaveAsTiffService(myGetContext(),300,tifName+".tif",module);
                        break;
                    case 2:
                        //System.out.println(mDataList.get(position).);
                        Intent intent = new Intent(DataActivity.this, PreviewActivity.class);
                        intent.putExtra("eCombineMap", module.getDesignsGson());
                        intent.putExtra("eListMap", module.getElementsGson());
                        intent.putExtra("background", module.getBackground());
                        intent.putExtra("id", module.getId());
                        intent.putExtra("printSize", module.getPrintSize());
                        intent.putExtra("preImagePath", module.getPreImagePath());
                        intent.putExtra("name",module.getDesignName());
                        startActivity(intent);
                        finish();

/*                        Toast.makeText(DataActivity.this, "第" + (position+1) + "; 条数据点击编辑", Toast.LENGTH_SHORT)
                                .show();*/
                        break;

                }

            }
        }
    };

    @Override
    public void onItemClick(View view, int adapterPosition) {
        Toast.makeText(this, "第" + adapterPosition + "个", Toast.LENGTH_SHORT).show();
    }

    private Context myGetContext(){
        return context;
    }
}
