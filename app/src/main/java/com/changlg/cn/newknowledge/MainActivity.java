package com.changlg.cn.newknowledge;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.changlg.cn.newknowledge.crashloghandler.CrashHandler;
import com.changlg.cn.newknowledge.toolbar.BaseToolbarActivity;
import com.changlg.cn.tapechat.adapter.recyclerview.BaseAdapterHelper;
import com.changlg.cn.tapechat.adapter.recyclerview.BaseQuickAdapter;
import com.changlg.cn.tapechat.adapter.recyclerview.QuickAdapter;
import com.changlg.cn.tapechat.adapter.recyclerview.divider.FlexibleDividerDecoration;
import com.changlg.cn.tapechat.adapter.recyclerview.divider.HorizontalDividerItemDecoration;

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
//        toolbar.setNavigationIcon(null);
//        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private void setUpRecyclerView(){
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.mian_list_name));
        mainList.setLayoutManager(new LinearLayoutManager(this));
        mainList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(android.R.color.holo_blue_light)
                 .visibilityProvider(new FlexibleDividerDecoration.VisibilityProvider() {
                    @Override
                    public boolean shouldHideDivider(int position, RecyclerView parent) {
                        if (position==0)
                            return true;
                        return false;
                    }
                }).sizeProvider(new FlexibleDividerDecoration.SizeProvider() {
                    @Override
                    public int dividerSize(int position, RecyclerView parent) {
                        if (position==0)
                            return 0;
                        return MainActivity.this.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
                    }
                })
                .build());
        //.sizeResId(R.dimen.activity_horizontal_margin) 一般情况下的列表写这个就可以了

//        QuickAdapter<String> adapter = new QuickAdapter<String>(this,R.layout.item_main_list,list) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, String item) {
//                helper.setText(R.id.demo_item_name,item);
//                helper.getTextView(R.id.demo_item_name).setClickable(true);
//                helper.setOnClickListener(R.id.demo_item_name, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "demo_item_name", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        };

        QuickAdapter<String> adapter = new QuickAdapter<String>(R.layout.item_main_list,list) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {

                helper.setText(R.id.demo_item_name,item);
                helper.getTextView(R.id.demo_item_name).setClickable(true);
                helper.setOnClickListener(R.id.demo_item_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "demo_item_name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean OnItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "long click position:"+position, Toast.LENGTH_SHORT).show();
                // 默认false，会触发click事件，故设置为true
                return true;
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click position:"+position, Toast.LENGTH_SHORT).show();
            }
        });
        View headView = getLayoutInflater().inflate(R.layout.view_header,(ViewGroup) mainList.getParent(), false);
        View footview = getLayoutInflater().inflate(R.layout.view_footer,(ViewGroup) mainList.getParent(), false);

        adapter.addHeaderView(headView);
        adapter.addFooterView(footview);
        mainList.setAdapter(adapter);

    }

}
