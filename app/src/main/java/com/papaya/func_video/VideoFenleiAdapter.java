package com.papaya.func_video;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.papaya.R;
import com.papaya.application.ConstValues;
import com.papaya.func_video.domain.CategoryStc;

import java.util.List;
import java.util.Random;

/**
 *
 */
public class VideoFenleiAdapter extends BaseAdapter {


    private static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)// 缓存SOURC和RESULT
            .dontAnimate()// 移除所有的动画
            // .fitCenter()// 该api可能 铺不满整个ImageView控件
            // .centerCrop()// 按比例放大/缩小,铺满整个ImageView控件
            .placeholder(R.drawable.shape_solid_gray_5)// 占位图
            .transform(new CenterCrop(), new RoundedCorners(14));

    private Activity context;
    private List<CategoryStc> dataLst;

    private Random rand;


    public VideoFenleiAdapter(Activity context, List<CategoryStc> valueLst) {
        this.context = context;
        this.dataLst = valueLst;

        rand = new Random();

    }

    // item的个数
    @Override
    public int getCount() {
        return dataLst.size();
    }

    // 根据位置获取对象
    @Override
    public CategoryStc getItem(int position) {
        return dataLst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 初始化每一个item的布局(待优化)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_layout, null);
            holder.video_img = (ImageView) convertView.findViewById(R.id.item_video_img);
            holder.operation_text = (TextView) convertView.findViewById(R.id.item_video_tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryStc item = (CategoryStc) dataLst.get(position);

        Glide.with(context)
                .load(item.getImageurl())
                .apply(BANNER_OPTIONS)
                .into(holder.video_img);

        holder.operation_text.setText(item.getCategoryname());


        return convertView;
    }

    private class ViewHolder {
        private ImageView video_img;
        private TextView operation_text;
    }

}
