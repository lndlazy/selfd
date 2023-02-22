package net.xnzn.app.selfdevice.menu;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import me.jingbin.library.ByRecyclerView;

public class MenuShowActivity extends SelfCommonActivity {

//    @Override
//    protected int setLayoutId() {
//        return R.layout.activity_menu_show;
//    }

    private ByRecyclerView brDateRecyclerView, brTypeRecyclerView, brMenuRecyclerView;


    @Override
    protected void initView() {
        super.initView();


        brDateRecyclerView = findViewById(R.id.brDateRecyclerView);
        brTypeRecyclerView = findViewById(R.id.brTypeRecyclerView);
        brMenuRecyclerView = findViewById(R.id.brMenuRecyclerView);


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
    protected boolean showTimeTitle() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_menu_show;
    }

    @Override
    protected void initData() {


//        brDateRecyclerView.setAdapter();

    }

    @Override
    protected void initLisitener() {

    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected void countDownFinish() {

    }

    @Override
    protected void showCountDownTime(int time) {

    }


}
