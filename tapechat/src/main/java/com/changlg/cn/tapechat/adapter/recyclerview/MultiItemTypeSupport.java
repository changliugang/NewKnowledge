package com.changlg.cn.tapechat.adapter.recyclerview;

/**
 * Multi-Type item Support interface
 * Created by chang on 2016/2/29.
 */
public interface MultiItemTypeSupport<T> {

    int getLayoutId(int viewType);

    int getItemViewType(int position, T t);

    int getCount();

}
