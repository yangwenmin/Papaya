package com.kylin.core.banner;

import android.view.View;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.kylin.core.R;

/**
 * Created by ywm
 */

public class CBHolderCreator implements CBViewHolderCreator {

    @Override
    public Holder createHolder(View itemView) {
        return new LocalImageHolderView(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_localimage;
    }
}
