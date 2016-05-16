package com.changlg.cn.newknowledge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.changlg.cn.newknowledge.crashloghandler.CrashHandler;
import com.changlg.cn.newknowledge.gson.GsonActivity;
import com.changlg.cn.newknowledge.toolbar.BaseToolbarActivity;
import com.changlg.cn.tapechat.adapter.recyclerview.BaseAdapterHelper;
import com.changlg.cn.tapechat.adapter.recyclerview.QuickAdapter;
import com.changlg.cn.tapechat.adapter.recyclerview.divider.HorizontalDividerItemDecoration;
import com.changlg.cn.tapechat.adapter.recyclerview.listener.OnRecyclerItemClickListener;
import com.changlg.cn.tapechat.adapter.recyclerview.listener.OnRecyclerItemLongClickListener;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseToolbarActivity {

    @InjectView(R.id.main_list)
    RecyclerView mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        CrashHandler.getInstance().init(this);

        setUpRecyclerView();
    }

    @Override
    protected void onCreateCustomToolBar(Toolbar toolbar) {
        super.onCreateCustomToolBar(toolbar);
        toolbar.setNavigationIcon(null);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private void setUpRecyclerView(){
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.mian_list_name));
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(android.R.color.transparent)
                .sizeResId(R.dimen.activity_horizontal_margin)
                .showLastDivider().build());
        QuickAdapter<String> adapter = new QuickAdapter<String>(this,R.layout.item_main_list,list) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.demo_item_name,item);
            }
        };
        mainList.setAdapter(adapter);
        mainList.addOnItemTouchListener(new OnRecyclerItemClickListener(mainList) {
            @Override
            public void onItemClick(RecyclerView parent, View child, int position) {
                startActivity(new Intent(MainActivity.this, GsonActivity.class));
//                Toast.makeText(MainActivity.this, "点击了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });
        mainList.addOnItemTouchListener(new OnRecyclerItemLongClickListener(mainList) {
            @Override
            public void onItemLongClick(RecyclerView parent, View child, int position) {
                Toast.makeText(MainActivity.this, "长按了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
