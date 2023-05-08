/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.teedesigner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teedesigner.R;
import com.example.teedesigner.UserDesignModule;

import java.util.List;


public class MainAdapter extends BaseAdapter<MainAdapter.ViewHolder> {

    private List<UserDesignModule> mDataList;

    public MainAdapter(Context context) {
        super(context);
    }



    public void notifyDataSetChanged(List<UserDesignModule> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.item_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(mDataList.get(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_img;
        TextView tv_name;
        TextView tv_designGson;
        TextView tvTitle;
        TextView tv_elementsGson;
        TextView tv_printSize;

        public ViewHolder(View itemView) {
            super(itemView);
            //Log.d("TAG", "ViewHolder is ready ");
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_designGson = itemView.findViewById(R.id.tv_designGson);
            tv_elementsGson = itemView.findViewById(R.id.tv_elementsGson);
            tv_printSize = itemView.findViewById(R.id.tv_printSize);
        }

        public void setData(UserDesignModule module) {
            //Log.d("TAG", "SetData is performed ");
            String preImagePath = module.getPreImagePath();
            if(preImagePath==null){
                iv_img.setImageResource(R.drawable.sample1);
            }else {
                Log.d("TAG", "setData: "+preImagePath);
                iv_img.setImageBitmap(getLoacalBitmap(preImagePath));
            }
            this.tv_name.setText(module.getDesignName());
            //this.tv_designGson.setText(module.getDesignsGson());
            this.tv_designGson.setText(module.getDesignsGson()==null?module.getDesignsGson():"DesignsGson");
            this.tv_elementsGson.setText(module.getElementsGson()==null?module.getElementsGson():"ElementsGson");
            this.tv_printSize.setText(module.getPrintSize()==null?module.getPrintSize():"PrintSize");
        }
    }
    public static Bitmap getLoacalBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }
}