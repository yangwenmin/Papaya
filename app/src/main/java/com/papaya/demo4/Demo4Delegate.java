package com.papaya.demo4;

import android.os.Bundle;
import android.view.View;

import com.kylin.core.activities.ProxyActivity;
import com.kylin.core.delegates.LatteDelegate;
import com.papaya.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Demo4Delegate extends LatteDelegate {


    @Override
    public Object setLayout() {
        return R.layout.delegate_demo4;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }
}
