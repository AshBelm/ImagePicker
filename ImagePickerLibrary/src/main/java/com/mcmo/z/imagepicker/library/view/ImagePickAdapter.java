package com.mcmo.z.imagepicker.library.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.mcmo.z.imagepicker.library.R;
import com.mcmo.z.imagepicker.library.info.BaseImagesInfo;
import com.mcmo.z.imagepicker.library.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by weizhang210142 on 2016/5/6.
 */
public class ImagePickAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BaseImagesInfo> data;
    private GetThumbnailCallBack mCallBack;
    private int itemWidth;

    public ImagePickAdapter(Context context) {
        this.context = context;
    }

    public ImagePickAdapter(Context context, ArrayList<BaseImagesInfo> data) {
        this(context);
        this.data = data;
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
        notifyDataSetChanged();
    }

    public void setGetThumbnailCallBack(GetThumbnailCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setData(ArrayList<BaseImagesInfo> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mcmo_item_imagep, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.mcmo_iv_ip_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (itemWidth != 0) {
            ViewGroup.LayoutParams lp = convertView.getLayoutParams();
            if (lp == null) {
                lp = new AbsListView.LayoutParams(itemWidth, itemWidth);
                convertView.setLayoutParams(lp);
            } else {
                if (lp.height != itemWidth) {
                    lp.height = itemWidth;
                }
            }
        }
        if (holder != null) {
            String filePath = data.get(position).getData();
            if (data.get(position).isQueryOver()) {
                if (!StringUtil.empty(data.get(position).getThumbnail())) {
                    filePath = data.get(position).getThumbnail();
                }
            } else {
                if (mCallBack != null) {
                    filePath = mCallBack.getThumbnail(position);
                    if (StringUtil.empty(filePath)) filePath = data.get(position).getData();
                }
            }
            ImageLoader.getInstance().displayImage("file:///" + filePath, holder.iv);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv;
    }

    public interface GetThumbnailCallBack {
        public String getThumbnail(int position);
    }
}
