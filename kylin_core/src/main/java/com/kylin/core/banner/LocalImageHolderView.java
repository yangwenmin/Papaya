package com.kylin.core.banner;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kylin.core.R;
import com.kylin.core.app.Latte;

/**
 * Created by Sai on 15/8/4.
 * 本地图片Holder例子
 */
public class LocalImageHolderView extends Holder<String> {
    private ImageView imageView;
    private static final RequestOptions BANNER_OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .dontAnimate()
            .fitCenter()
            // .centerCrop()
            ;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView = itemView.findViewById(R.id.ivPost);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(Latte.getApplicationContext())
                .load(data)
                .apply(BANNER_OPTIONS)
                .into(imageView);
    }
}
