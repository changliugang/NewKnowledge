package com.changlg.cn.tapechat.adapter.recyclerview.listener;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView的Item点击事件监听，相当于ListView的OnItemClickListener
 * Created by chang on 2016/5/12.
 */
public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetector;// 手势核心类
    private RecyclerView recyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
//                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onItemClick(recyclerView, child, recyclerView.getChildAdapterPosition(child));
            }
            return true;
        }

    }

    public abstract void onItemClick(RecyclerView parent, View child, int position);
}
