package net.xnzn.app.selfdevice.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.menu.bean.MenuChooseBean;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;

public class MenuChooseActivity extends SelfCommonActivity {

    private ByRecyclerView byRecyclerView;
    private ImageView ivBack;
    private TextView tvCount;
    private List<MenuChooseBean> chooseBeanList = new ArrayList<>();

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

    @Override
    protected boolean showTimeTitle() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        byRecyclerView = findViewById(R.id.byRecyclerView);
        ivBack = findViewById(R.id.ivBack);
        tvCount = findViewById(R.id.tvCount);
    }

    @Override
    protected int showView() {
        return R.layout.activity_menu_choose;
    }

    @Override
    protected void initData() {
        chooseBeanList.add(new MenuChooseBean(1, "自助餐菜谱", ""));
        chooseBeanList.add(new MenuChooseBean(2, "自助餐菜谱", ""));
        chooseBeanList.add(new MenuChooseBean(3, "自助餐菜谱", ""));
        byRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MenuChooseAdapter chooseAdapter = new MenuChooseAdapter(chooseBeanList);
        byRecyclerView.setAdapter(chooseAdapter);
        chooseAdapter.setNewData(chooseBeanList);
        byRecyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                startActivity(MenuShowActivity.class);
                //BG-31881908220060
            }
        });
    }

    @Override
    protected void initLisitener() {

        ivBack.setOnClickListener(view -> finish());
    }

    @Override
    protected void countDownFinish() {

    }

    @Override
    protected void showCountDownTime(int time) {

    }
}
