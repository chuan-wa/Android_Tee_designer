package com.example.teedesigner.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.teedesigner.R;
import com.example.teedesigner.Service.ImageGoogleCloudService;
import com.example.teedesigner.listener.ClickImageListListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




public class ImageListFragment extends Fragment {
    //private ArrayList<String> imageList=new ArrayList<>();
    private Map<String,ArrayList<String>> allImages=new TreeMap<>();
    private View view;
    private static ClickImageListListener clickImageListListener;
    private AlertDialog dialog;

    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        view = inflater.inflate(R.layout.fragment_image_list, container, false);
        ListView listView=view.findViewById(R.id.imageListView);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("loading images...");
        dialog.show();


        //dialog.show();


        executor.execute(new Runnable() {
            @Override
            public void run() {

                allImages= ImageGoogleCloudService.getImages();
                //imageList.addAll(allImages.keySet());
                dialog.dismiss();

                //Background work here

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        MyMapAdapter adapter=new MyMapAdapter(getActivity(),allImages);
                        listView.setAdapter(adapter);
/*                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String item=(String)adapterView.getItemAtPosition(i);
                                Log.d("TAG", "onItemClick: "+item);
                            }
                        });*/
                    }
                });
            }

        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getFragmentManager().popBackStack("imageList", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        } );

        return view;

    }

    private static class MyMapAdapter extends BaseAdapter{
        private final Map<String,ArrayList<String>> allImages;
        private final ArrayList<String> imageList=new ArrayList<>();
        private final Context context;
        public MyMapAdapter(Context context,Map<String,ArrayList<String>> allImages){
            this.allImages=allImages;
            this.context=context;
            imageList.addAll(allImages.keySet());
        }
        //how many lines
        @Override
        public int getCount() {
            return allImages.size();
        }

        @Override
        public ArrayList<String> getItem(int i) {
            return allImages.get(imageList.get(i));
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //view是缓存过的视图
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;
            //v指linearLayout
            /*if(view==null)*/v=LayoutInflater.from(context).inflate(R.layout.image_cardview,viewGroup,false);
            //v=view;
            LinearLayout ll=(LinearLayout) v;
            TextView textView=ll.findViewById(R.id.image_list_name);
            textView.setText(imageList.get(i));
            ImageView generalImageView1=ll.findViewById(R.id.generalImageView1);
            ImageView generalImageView2=ll.findViewById(R.id.generalImageView2);
            ImageView generalImageView3=ll.findViewById(R.id.generalImageView3);
            ImageView generalImageView4=ll.findViewById(R.id.generalImageView4);
            ArrayList<String> firstFour=new ArrayList<>(getItem(i).subList(0, Math.min(4, getItem(i).size())));
            Glide.with(context).load(firstFour.get(0)).into(generalImageView1);
            Glide.with(context).load(firstFour.get(1)).into(generalImageView2);
            Glide.with(context).load(firstFour.get(2)).into(generalImageView3);
            Glide.with(context).load(firstFour.get(3)).into(generalImageView4);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick: "+imageList.get(i));
                    clickImageListListener.onClickImageList(getItem(i));
                }
            });
            return v;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickImageListListener = (ClickImageListListener) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }
}