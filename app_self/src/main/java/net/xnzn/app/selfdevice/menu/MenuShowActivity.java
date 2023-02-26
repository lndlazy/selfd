package net.xnzn.app.selfdevice.menu;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.adapter.MenuFoodsAdapter;
import net.xnzn.app.selfdevice.menu.adapter.MenuShowAdapter;
import net.xnzn.app.selfdevice.menu.adapter.MenuTypeAdapter;
import net.xnzn.app.selfdevice.menu.adapter.MenuDateAdapter;
import net.xnzn.app.selfdevice.menu.bean.MenuDateShowBean;
import net.xnzn.app.selfdevice.menu.bean.MenuFoodsDetailBean;
import net.xnzn.app.selfdevice.menu.bean.MenuTypeBean;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

public class MenuShowActivity extends SelfCommonActivity {

    private ByRecyclerView brDateRecyclerView, brTypeRecyclerView, brMenuRecyclerView;

    //    private RecyclerView brMenuRecyclerView;
    protected TabLayout tabLayout;

    @Override
    protected void initView() {
        super.initView();

        brDateRecyclerView = findViewById(R.id.brDateRecyclerView);
        brTypeRecyclerView = findViewById(R.id.brTypeRecyclerView);
        brMenuRecyclerView = findViewById(R.id.brMenuRecyclerView);

        tabLayout = findViewById(R.id.tab);

    }

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return true;
    }

    @Override
    protected int showView() {
        return R.layout.activity_menu_show;
    }

    @Override
    protected void initData() {

        setDateAdapter();
        setTypeAdapter();
        setMenuShowAdapter();

        tabLayout.addTab(tabLayout.newTab().setText("早餐"));
        tabLayout.addTab(tabLayout.newTab().setText("午餐"));
        tabLayout.addTab(tabLayout.newTab().setText("下午餐"));
        tabLayout.addTab(tabLayout.newTab().setText("晚餐"));
        tabLayout.addTab(tabLayout.newTab().setText("夜宵"));

        countDown(10, 20);
    }

    private void setMenuShowAdapter() {

        List<MenuFoodsDetailBean> foodList = new ArrayList<>();
        foodList.add(new MenuFoodsDetailBean(1, "推荐", "", -1, new String[]{"微甜", "香脆"}, "80", "98%", "12"));
        foodList.add(new MenuFoodsDetailBean(1, "红烧狮子头", "", 1, new String[]{"微甜"}, "80", "98%", "12"));
        foodList.add(new MenuFoodsDetailBean(1, "盐水鸭", "", 1, new String[]{"微甜", "香脆", "浓厚"}, "80", "98%", "12"));
        foodList.add(new MenuFoodsDetailBean(1, "辣子鸡", "", 1, new String[]{"可口", "香脆", "香辣"}, "80", "98%", "12"));
        foodList.add(new MenuFoodsDetailBean(1, "热门推荐", "", -1, new String[]{"微甜", "香脆"}, "80", "98%", "12"));
        foodList.add(new MenuFoodsDetailBean(1, "青椒肉丝", "", 2, new String[]{"微甜"}, "80", "98%", "12"));
        MenuFoodsAdapter menuFoodsAdapter = new MenuFoodsAdapter(this, foodList);
//        MenuShowAdapter menuFoodsAdapter = new MenuShowAdapter(this, foodList);
        brMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL)
//                .setNoShowDivider(1, 1)
                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
                .setParam(R.color.translucent, 10, 0, 0);

        brMenuRecyclerView.addItemDecoration(itemDecoration);

        brMenuRecyclerView.setAdapter(menuFoodsAdapter);
    }

    private void setTypeAdapter() {

        List<MenuTypeBean> typeList = new ArrayList<>();
        typeList.add(new MenuTypeBean(1, "推荐"));
        typeList.add(new MenuTypeBean(2, "热门推荐"));
        typeList.add(new MenuTypeBean(3, "大荤"));
        typeList.add(new MenuTypeBean(4, "素菜"));
        typeList.add(new MenuTypeBean(5, "水果"));
        MenuTypeAdapter typeAdapter = new MenuTypeAdapter(this, typeList);
//        typeAdapter.setNewData(typeList);
        brTypeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        brTypeRecyclerView.setAdapter(typeAdapter);
        brTypeRecyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                typeAdapter.setCheckedPosition(position);
                typeAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setDateAdapter() {
        List<MenuDateShowBean> dateList = new ArrayList<>();
        dateList.add(new MenuDateShowBean(1, "10.1", "周一"));
        dateList.add(new MenuDateShowBean(2, "10.2", "周二"));
        dateList.add(new MenuDateShowBean(3, "10.3", "周三"));
        dateList.add(new MenuDateShowBean(4, "10.4", "周四"));
        dateList.add(new MenuDateShowBean(5, "10.5", "周五"));
        dateList.add(new MenuDateShowBean(6, "10.6", "周六"));
        dateList.add(new MenuDateShowBean(7, "10.7", "周日"));
        MenuDateAdapter dateAdapter = new MenuDateAdapter(this, dateList);
//        dateAdapter.setNewData(dateList);
        brDateRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        brDateRecyclerView.setAdapter(dateAdapter);//一周
        brDateRecyclerView.setOnItemClickListener((v, position) -> {
            dateAdapter.setCurrentChoosePosition(position);
            dateAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void initLisitener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }


}
