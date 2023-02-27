package net.xnzn.app.selfdevice.login;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

public class CardLoginActivity extends SelfCommonActivity {

    @Override
    protected void initView() {
        super.initView();


    }

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_card_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initLisitener() {

    }
}