package net.xnzn.app.selfdevice.setting;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

public class SettingActivity extends SelfCommonActivity {


    @Override
    protected void initView() {
        super.initView();

    }


    @Override
    protected boolean showTitle() {
        return false;
    }

    @Override
    protected boolean showBar() {
        return false;
    }


    @Override
    protected int showView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        countDown(10, 20);
    }

    @Override
    protected void initLisitener() {

    }
}
