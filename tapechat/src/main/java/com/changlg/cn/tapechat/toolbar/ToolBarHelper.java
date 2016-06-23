package com.changlg.cn.tapechat.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.changlg.cn.tapechat.R;


/**
 * ToolBar utils
 * Created by chang on 2016/3/10.
 */
public class ToolBarHelper {

    private Context mContext;

    /*base view*/
    private FrameLayout mBaseView;

    /*content view*/
    private View mContentView;

    /*toolbar*/
    private Toolbar mToolBar;

    private LayoutInflater mInflater;

    /*
        * 1、is toolbar float on window
        * 2、toolbar height
        * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        /*initialization ContentView*/
        initContentView();
        /*initialization UserView*/
        initUserView(layoutId);
        /*initialization toolbar*/
        initToolBar();
    }

    private void initContentView() {
        /*create a FrameLayout as parent container for current view*/
        mBaseView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseView.setLayoutParams(params);
    }

    private void initToolBar() {
        View toolbar = mInflater.inflate(R.layout.toolbar, mBaseView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.id_tool_bar);
    }

    private void initUserView(int id) {
        mContentView = mInflater.inflate(id, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(ATTRS);
        /* get float state from current theme */
        boolean overly = typedArray.getBoolean(0, false);
        /*get float toolbar height from current theme*/
        int toolBarSize = (int) typedArray.getDimension(1,(int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        typedArray.recycle();
        /*if the state is floating ,setup margin 0*/
        params.topMargin = overly ? 0 : toolBarSize;
        mBaseView.addView(mContentView, params);
    }

    public FrameLayout getContentView() {
        return mBaseView;
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

}
