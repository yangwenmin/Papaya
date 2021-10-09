package com.papaya.test.demo4;

import com.kylin.core.feiqi_activities.ProxyActivity;
import com.kylin.core.feiqi_delegates.LatteDelegate;

public class Demo4Activity extends ProxyActivity {


    @Override
    public LatteDelegate setRootDelegate() {
        return new Demo4Delegate();
    }

    @Override
    public void post(Runnable runnable) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
