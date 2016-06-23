package com.changlg.cn.newknowledge;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chang on 2016/5/25.
 */
public class ClickAdapter extends RecyclerView.Adapter<ClickAdapter.BaseViewHolder> {
    private List<String> mList;  //用户列表
    private Context mContext;

    public ClickAdapter(Context context, List<String> list) {
        mContext = context;
        this.mList = list;
    }

    @Override
    public ClickAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_main_list, parent, false);

        return new BaseViewHolder(itemView, new IMyViewHolderClicks() {
            @Override
            public void onItemClick(String uid) {
                // 跳转到个人信息页，根据uid获取个人信息
            }

            //如果需要刷新某个特定界面，则参数中包含position
            @Override
            public void onFollowStatusChange(final String user,final int position) {
                //与服务器交互，如果成功，刷新当前按钮文字的 "关注"为"已关注"
                notifyItemChanged(position);

            }
        });
    }

    @Override
    public void onBindViewHolder(ClickAdapter.BaseViewHolder holder, int position) {
        String user = mList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvFollowStatus;        //关注按钮
        CardView linearContainer;   //整个item的根布局
        IMyViewHolderClicks mListener;

        public BaseViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            tvFollowStatus = (TextView) v.findViewById(R.id.demo_item_name);
            linearContainer = (CardView) v.findViewById(R.id.demo_item_cardview);
            mListener = listener;
            tvFollowStatus.setOnClickListener(this);
            linearContainer.setOnClickListener(this);
        }

        public void bind(String user) {

            tvFollowStatus.setText("加关注");
            //将实体绑定到view上面
            tvFollowStatus.setTag(user);
            tvFollowStatus.setClickable(true);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.demo_item_name:
                    tvFollowStatus.setClickable(false);
                    Log.d("chang","Text");
                    mListener.onFollowStatusChange((String) tvFollowStatus.getTag(), getLayoutPosition());
                    break;
                case R.id.demo_item_cardview:
                    Log.d("chang","Card");
                    mListener.onItemClick(((String) tvFollowStatus.getTag()));
                    break;
            }
        }
    }

    private interface IMyViewHolderClicks {
        //单击整个item跳转到用户界面，需要传递uid
        public void onItemClick(String uid);

        //关注按钮,需要更新按钮的状态
        public void onFollowStatusChange(String user, int position);
    }
}
