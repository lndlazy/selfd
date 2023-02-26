package net.xnzn.app.selfdevice.query;

import android.view.View;

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

public class QueryActivity extends SelfCommonActivity {

    private static final String TAG = "QueryActivity";
    final String[] tabs = new String[]{"我的订单", "营养摄入", "健康检测"};
    protected ViewPager2 viewPager2;
    protected TabLayout tabLayout;

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
        //Adapter
        viewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                L.i(TAG + ":===createFragment===");
                //FragmentStateAdapter内部自己会管理已实例化的fragment对象 所以不需要考虑复用的问题
                return fragments[position];
            }

            @Override
            public int getItemCount() {
                return tabs.length;
            }
        });
        //viewPager 页面切换监听
        viewPager2.registerOnPageChangeCallback(changeCallback);

        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        mediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> { // TabLayout和ViewPager2关联到一起
//            tab.setText(menus[position]); // 设置Tab的标题
            tab.setText(tabs[position]);
//            tab.setCustomView(tabLayout.getTabAt(po)); // 设置Tab的图标和标题
        });
        mediator.attach();

        countDown(10, 20);
    }

    private Fragment[] fragments;

    private void initFragments() {
        fragments = new Fragment[3];
        fragments[0] = new OrderFragment();
        fragments[1] = new NutritionFragment();
        fragments[2] = new HealthFragment();
    }

    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            L.i("=====onPageSelected=====:" + position);
//            //可以来设置选中时tab的大小
//            int tabCount = tabLayout.getTabCount();
//            for (int i = 0; i < tabCount; i++) {
//                TabLayout.Tab tab = tabLayout.getTabAt(i);
//                TextView tabView = (TextView) tab.getCustomView();
//                if (tab.getPosition() == position) {
//                    tabView.setTextSize(activeSize);
//                    tabView.setTypeface(Typeface.DEFAULT_BOLD);
//                } else {
//                    tabView.setTextSize(normalSize);
//                    tabView.setTypeface(Typeface.DEFAULT);
//                }
//            }
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
     * 订单详情
     *
     * @param id
     */
    public void orderDetail(int id) {




    }


}