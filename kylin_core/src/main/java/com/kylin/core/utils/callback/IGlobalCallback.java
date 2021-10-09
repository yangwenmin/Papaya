package com.kylin.core.utils.callback;


import androidx.annotation.Nullable;

/**
 * Created by yangwenmin on 2017/12/23.
 */

public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);

}
