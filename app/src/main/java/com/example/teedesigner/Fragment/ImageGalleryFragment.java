package com.example.teedesigner.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.teedesigner.Elements.ElementList;
import com.example.teedesigner.Elements.PictureBitmapElement;
import com.example.teedesigner.GlobalData;
import com.example.teedesigner.R;
import com.example.teedesigner.Service.ImageEditService;
import com.example.teedesigner.SharedViewModel;
import com.example.teedesigner.listener.AddImageListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;


public class ImageGalleryFragment extends Fragment {
    private final String TAG="TAG";
    private final ArrayList<String> images;
    private AddImageListener addImageListener;
    private SharedViewModel viewModel;
    public ImageGalleryFragment(ArrayList<String> images){
        this.images=images;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_image_gallery,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        RecyclerView recyclerView=view.findViewById(R.id.imageGalleryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        MyAdapter adapter=new MyAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        Log.d("TAG", images.toString());
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("imageGallery", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        } );
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addImageListener = (AddImageListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private final Context context;
        public MyAdapter(Context context){
            this.context=context;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v=LayoutInflater.from(context)
                    .inflate(R.layout.image_cardview,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setVisibility(View.GONE);
            ArrayList<ImageView> imageViews=new ArrayList<>();ArrayList<CardView> cardViews=new ArrayList<>();
            imageViews.add(holder.generalImageView1);imageViews.add(holder.generalImageView2);imageViews.add(holder.generalImageView3);imageViews.add(holder.generalImageView4);
            cardViews.add(holder.generalCardView1);cardViews.add(holder.generalCardView2);cardViews.add(holder.generalCardView3);cardViews.add(holder.generalCardView4);
            //smaller than 4, for example 3
            if(getItemSize(position)==0)return;
            if(getItemSize(position)<4){
                //start from the index 3 in arraylist
                for (int j=getItemSize(position);j<4;j++){
                    cardViews.get(j).setVisibility(View.INVISIBLE);
                    //Log.d(TAG, "onBindViewHolder: "+imageViews.get(j));
                }
            }
            for(int j=0;j<getItemSize(position);j++){
                String url=getItem(position).get(j);
                Glide.with(context).load(url).into(imageViews.get(j));
                imageViews.get(j).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Glide.with(context).load(url).into(new CustomTarget<Drawable>(){


                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                Bitmap bitmap=((BitmapDrawable)resource).getBitmap();
                                String name=url.substring(GlobalData.urlPrefix.length());
                                String path=ImageEditService.saveImage(bitmap,name,"/imgs");
                                Log.d(TAG, "uri"+path);
                                ElementList elements= viewModel.getElements();
                                PictureBitmapElement pe;
                                if(elements.hasSelectedElement()){
                                    pe= (PictureBitmapElement) elements.getSelectedElement();
                                    Bitmap pre=pe.getBitmap();
                                    pe.setImgSrc(path);
                                    Bitmap beforeCompressed=pe.getBitmap();
                                    try {
                                        pe.setBitmap(new Compressor(context).compressToBitmap(new File(path)));
                                        pe.setCompressFactor(beforeCompressed.getHeight()/pe.getBitmap().getHeight());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    pe.setScaleFactor(Math.min((pre.getHeight()/pe.getBitmap().getHeight()),pre.getWidth()/pe.getBitmap().getWidth())*pe.getScaleFactor());
                                    pe.setPosition(-pe.getWidth()/2+getResources().getDimension(R.dimen.dp_350)/2,-pe.getHeight()/2+getResources().getDimension(R.dimen.dp_350)/2);
                                }else{
                                    pe=new PictureBitmapElement(path,0,0);
                                    Bitmap beforeCompressed=pe.getBitmap();
                                    try {
                                        pe.setBitmap(new Compressor(context).compressToBitmap(new File(path)));
                                        pe.setCompressFactor(beforeCompressed.getHeight()/pe.getBitmap().getHeight());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    pe.setPosition(-pe.getWidth()/2+getResources().getDimension(R.dimen.dp_350)/2,-pe.getHeight()/2+getResources().getDimension(R.dimen.dp_350)/2);
                                    pe.setScaleFactor(getResources().getDimension(R.dimen.dp_150)/pe.getHeight());
                                    elements.add(pe);
                                }
/*                                try {
                                    pe.setBitmap(new Compressor(context).compressToBitmap(new File(path)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/

                                viewModel.setElements(elements);
                                addImageListener.addImage();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                        Log.d(TAG, "onClick: "+url);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            int count=(int)Math.ceil((double) images.size()/4);
            //Log.d("TAG", "getItemCount: "+count);
            return count;
        }

        public ArrayList<String> getItem(int i) {
            int count=(i)*4;
            ArrayList<String>imagesLine= new ArrayList<>(images.subList(count,Math.min(count+4,images.size())));
            //Log.d(TAG, "getItem: "+imagesLine);
            return imagesLine;
        }
        public int getItemSize(int i){
            //Log.d(TAG, "getItemSize: "+getItem(i).size());
            return getItem(i).size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView generalImageView1;private final ImageView generalImageView2;private final ImageView generalImageView3;private final ImageView generalImageView4;
            private final CardView generalCardView1;private final CardView generalCardView2;private final CardView generalCardView3;private final CardView generalCardView4;
            private final LinearLayout title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                generalImageView1=itemView.findViewById(R.id.generalImageView1);generalImageView2=itemView.findViewById(R.id.generalImageView2);generalImageView3=itemView.findViewById(R.id.generalImageView3);generalImageView4=itemView.findViewById(R.id.generalImageView4);
                generalCardView1=itemView.findViewById(R.id.generalCardView1);generalCardView2=itemView.findViewById(R.id.generalCardView2);generalCardView3=itemView.findViewById(R.id.generalCardView3);generalCardView4=itemView.findViewById(R.id.generalCardView4);
                title=itemView.findViewById(R.id.image_list_title_container);
            }
        }



    }

/*    private class MyListAdapter extends BaseAdapter{
        private Context context;

        public MyListAdapter(Context context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public ArrayList getItem(int i) {
            int count=(i-1)*4;
            ArrayList<String>imagesLine= new ArrayList<>(images.subList(count,count+4));
            return imagesLine;
        }

        public int getItemSize(int i){
            return getItem(i).size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            v=LayoutInflater.from(context).inflate(R.layout.card_view_image,viewGroup,false);
            LinearLayout ll=(LinearLayout) v;
            LinearLayout title=ll.findViewById(R.id.image_list_title_container);
            title.setVisibility(View.GONE);
            ImageView generalImageView1=ll.findViewById(R.id.generalImageView1);
            ImageView generalImageView2=ll.findViewById(R.id.generalImageView2);
            ImageView generalImageView3=ll.findViewById(R.id.generalImageView3);
            ImageView generalImageView4=ll.findViewById(R.id.generalImageView4);
            ArrayList<ImageView> imageViews=new ArrayList<>();
            imageViews.add(generalImageView1);imageViews.add(generalImageView2);imageViews.add(generalImageView3);imageViews.add(generalImageView4);
            //smaller than 4, for example 3
            if(getItemSize(i)<4){
                //start from the index 3 in arraylist
                for (int j=getItemSize(i);i<4;i++){
                    imageViews.get(j).setVisibility(View.INVISIBLE);
                }
            }
            for(int j=0;j<getItemSize(i);j++){
                Glide.with(context).load(getItem(i).get(j)).into(imageViews.get(j));
            }

            return v;
        }
    }*/
}