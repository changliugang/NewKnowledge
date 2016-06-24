package com.changlg.cn.tapechat.adapter.recyclerview;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * universal RecyclerView adapter
 * Created by chang on 2016/2/29.
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T,BaseAdapterHelper>{


    public QuickAdapter(List<T> data) {
        super(data);
    }

    public QuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public QuickAdapter(View contentView, List<T> data) {
        super(contentView, data);
    }

    public QuickAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public QuickAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    public QuickAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context, multiItemTypeSupport);
    }

    public QuickAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport, List<T> data) {
        super(context, multiItemTypeSupport, data);
    }


}
