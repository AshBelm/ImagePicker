package com.mcmo.z.imagepicker.library.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.activity.ImagePickActivity;
import com.mcmo.z.imagepicker.library.util.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ZhangWei on 2016/11/24.
 */

public class AlbumListFragment extends Fragment{
    public static final String TAG = "AlbumListFragment";
    private ImagePickActivity mActivity;
    private ListView lv;
    private LinkedList<String> keys= new LinkedList<>();
    private AlbumListAdapter mAdapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImagePickActivity)
            mActivity = (ImagePickActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.mcmo_album_list,container,false);
        lv= (ListView) view.findViewById(R.id.lv_album);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.changeToDetail(keys.get(position));
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mActivity.data!=null){
            Iterator<String> iterator=mActivity.data.keySet().iterator();
            while (iterator.hasNext()){
                keys.add(iterator.next());
            }
            if(keys.contains("Screenshots")){
                keys.remove("Screenshots");
                keys.addFirst("Screenshots");
            }
            if(keys.contains("Camera")){
                keys.remove("Camera");
                keys.addFirst("Camera");
            }
            mAdapter=new AlbumListAdapter();
            lv.setAdapter(mAdapter);
        }
    }

    private class AlbumListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return keys.size();
        }

        @Override
        public Object getItem(int position) {
            return keys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView=LayoutInflater.from(mActivity).inflate(R.layout.item_album_list,null);
                holder.tvName= (TextView) convertView.findViewById(R.id.tv_albumName);
                holder.tvNum= (TextView) convertView.findViewById(R.id.tv_nums);
                holder.ivCover= (ImageView) convertView.findViewById(R.id.iv_cover);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            String path=mActivity.data.get(keys.get(position)).get(0).getData();
            String pages=mActivity.data.get(keys.get(position)).size()+"张";
            ImageLoader.getInstance().displayImage("file:///"+path,holder.ivCover);
            holder.tvNum.setText(pages);
            holder.tvName.setText(keys.get(position));
            return convertView;
        }
    }
    private class ViewHolder{
        TextView tvName,tvNum;
        ImageView ivCover;
    }
}
