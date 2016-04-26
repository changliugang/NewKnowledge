package com.changlg.cn.tapechat.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * QuickAdapter的基类
 * Created by chang on 2016/2/26.
 */
public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper>
        extends RecyclerView.Adapter<BaseAdapterHelper> implements OnClickListener {

    protected Context context;

    protected int layoutResId;

    protected List<T> data;

    private OnItemClickListener mOnItemClickListener = null;

    private boolean isFooterRefresh;



    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    protected MultiItemTypeSupport<T> multiItemTypeSupport;

    public BaseQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.data = data == null ? new ArrayList<T>() : data;
    }

    public BaseQuickAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        this(context, multiItemTypeSupport, null);
    }

    public BaseQuickAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport, List<T> data) {
        this.context = context;
        this.multiItemTypeSupport = multiItemTypeSupport;
        this.data = data == null ? new ArrayList<T>() : data;
    }

    public BaseQuickAdapter(Context context, int layoutResId, List<T> data,int minHeight,int heightOffset) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.data = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public int getItemCount() {
        if (multiItemTypeSupport != null) {
            return multiItemTypeSupport.getCount();
        }
        return data.size();
    }

    public T getItem(int position) {
        if (position >= data.size()) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (multiItemTypeSupport != null) {
            return multiItemTypeSupport.getItemViewType(position, getItem(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseAdapterHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (multiItemTypeSupport != null) {
            int layoutId = multiItemTypeSupport.getLayoutId(viewType);
            view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        }
        view.setOnClickListener(this);
        BaseAdapterHelper bah = new BaseAdapterHelper(view);
        return bah;
    }

    @Override
    public void onBindViewHolder(BaseAdapterHelper holder, int position) {
        holder.itemView.setTag(position);
        T item = getItem(position);
        convert((H) holder, item);
    }

    /**
     * 适配逻辑实现函数
     *
     * @param helper 适配器帮助类
     * @param item   适配对象
     */
    protected abstract void convert(H helper, T item);

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.OnItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void add(T item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> lists) {
        int startIndex = data.size();
        data.addAll(startIndex, lists);
        notifyItemRangeInserted(startIndex, lists.size());
    }

    public void replaceAll(List<T> lists) {
        data.clear();
        addAll(lists);
    }

    public void set(int index, T item) {
        data.set(index, item);
        notifyDataSetChanged();
    }

    public void set(T oldItem, T newItem) {
        set(data.indexOf(oldItem), newItem);
    }

    public void remove(int index) {
        data.remove(index);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        data.remove(item);
        notifyDataSetChanged();
    }

    public boolean contains(T item) {
        return data.contains(item);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

}
