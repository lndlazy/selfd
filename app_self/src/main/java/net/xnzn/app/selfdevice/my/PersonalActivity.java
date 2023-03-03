package net.xnzn.app.selfdevice.my;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.my.fragment.MyAddressFragment;
import net.xnzn.app.selfdevice.my.fragment.MyBaseFragment;
import net.xnzn.app.selfdevice.my.fragment.MyFaceFragment;
import net.xnzn.app.selfdevice.my.fragment.MyHealthFragment;
import net.xnzn.app.selfdevice.my.fragment.MyModifyPwdFragment;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.SelfConstant;
import net.xnzn.lib_log.L;

public class PersonalActivity extends SelfCommonActivity {

    protected TabLayout tabLayout;
    private static final String TAG = "PersonalActivity";
    final String[] tabs = new String[]{"基本信息", "健康信息", "我的地址", "人脸上传", "修改密码"};
    protected ViewPager2 viewPager2;

    protected FragmentStateAdapter fragmentStateAdapter;
    //    protected OrderDetailFragment orderDetailFragment;
//    private FrameLayout fragment_Detail;

    @Override
    protected void initView() {
        super.initView();

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
//        fragment_Detail = findViewById(R.id.fragment_Detail);
        tvBarContent.setText("首页> 个人信息");

        //去除边界阴影
        View childAt = viewPager2.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            childAt.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    @Override
    protected boolean showTitleBar() {
        return false;
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
        return R.layout.activity_personal;
    }

    private TabLayoutMediator mediator;

    @Override
    protected void initData() {
        super.initData();

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
        
    }

    private Fragment[] fragments;

    private void initFragments() {
        fragments = new Fragment[5];
        fragments[0] = new MyBaseFragment();
        fragments[1] = new MyHealthFragment();
        fragments[2] = new MyAddressFragment();
        fragments[3] = new MyFaceFragment();
        fragments[4] = new MyModifyPwdFragment();
    }

    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageSelected(int position) {
//            fragment_Detail.setVisibility(View.GONE);
//            viewPager2.setVisibility(View.VISIBLE);
//            L.i("=====onPageSelected=====:" + position);
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

}