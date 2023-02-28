package net.xnzn.app.selfdevice.query;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_log.L;

import me.jingbin.library.ByRecyclerView;

public class QueryActivity extends SelfCommonActivity implements QueryView {

    private static final String TAG = "QueryActivity";
    final String[] tabs = new String[]{"我的订单", "营养摄入", "健康检测"};
    protected ViewPager2 viewPager2;
    protected TabLayout tabLayout;
    protected FragmentStateAdapter fragmentStateAdapter;
    //    protected OrderDetailFragment orderDetailFragment;
    private FrameLayout fragment_Detail;

    private QueryPresenter queryPresenter = new QueryPresenter(this);

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return true;
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_query;
    }

    @Override
    protected void initView() {
        super.initView();

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fragment_Detail = findViewById(R.id.fragment_Detail);

        //去除边界阴影
        View childAt = viewPager2.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            childAt.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

    }

    private TabLayoutMediator mediator;

    @Override
    protected void initData() {

        initFragments();
        //禁用预加载
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        fragmentStateAdapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                L.i(TAG + ":===createFragment===");
                //FragmentStateAdapter内部自己会管理已实例化的fragment对象 所以不需要考虑复用的问题
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return fragments.length;
            }
        };
        //Adapter
        viewPager2.setAdapter(fragmentStateAdapter);
        //viewPager 页面切换监听
        viewPager2.registerOnPageChangeCallback(changeCallback);

        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        mediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> { // TabLayout和ViewPager2关联到一起
//            tab.setText(menus[position]); // 设置Tab的标题
            if (position < tabs.length)
                tab.setText(tabs[position]);
//            tab.setCustomView(tabLayout.getTabAt(po)); // 设置Tab的图标和标题
        });
        mediator.attach();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                System.out.println("---------");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        countDown(10, 20);
    }

    private Fragment[] fragments;

    private void initFragments() {
        fragments = new Fragment[3];
        fragments[0] = new OrderFragment();
        fragments[1] = new NutritionFragment();
        fragments[2] = new HealthFragment();
//        fragments[3] = new OrderDetailFragment();
    }

    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageSelected(int position) {
            fragment_Detail.setVisibility(View.GONE);
            viewPager2.setVisibility(View.VISIBLE);
            L.i("=====onPageSelected=====:" + position);
        }
    };

    @Override
    protected void onDestroy() {
        if (mediator != null)
            mediator.detach();
        viewPager2.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }

    @Override
    protected void initLisitener() {

    }


    /**
     * 切换到订单详情
     *
     * @param id
     */
    public void orderDetail(int id) {
        fragment_Detail.setVisibility(View.VISIBLE);
        viewPager2.setVisibility(View.GONE);
//        orderDetailFragment = new OrderDetailFragment(id);
        //viewPager2.setCurrentItem(fragments.length - 1);

        View view = LayoutInflater.from(this).inflate(R.layout.fragment_order_detail, fragment_Detail);

        queryPresenter.showDetailView(view);

        TextView tvDetailComment = view.findViewById(R.id.tvDetailComment);//评价
        tvDetailComment.setOnClickListener(v -> {

            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

    }

    @Override
    public Context getMyAppContext() {
        return getApplicationContext();
    }
}