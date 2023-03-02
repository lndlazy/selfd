package net.xnzn.app.selfdevice.setting;

import android.view.View;

import com.allen.library.SuperTextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.SelfConstant;
import net.xnzn.leniu_common_ui.setting.SettingHttpActivity;
import net.xnzn.leniu_common_ui.setting.SettingSystemActivity;
import net.xnzn.leniu_common_ui.setting.SettingUpdateLogActivity;

public class SettingActivity extends SelfCommonActivity implements View.OnClickListener {

    private SuperTextView stvParams;
    private SuperTextView stvUser;
    private SuperTextView stvHttpConfig;
    private SuperTextView stvSystemConfig;
    private SuperTextView stvDeviceInfo;
    private SuperTextView stvUpdate;

    @Override
    protected void initView() {
        super.initView();
        stvParams = findViewById(R.id.stvParams);
        stvUser = findViewById(R.id.stvUser);
        stvHttpConfig = findViewById(R.id.stvHttpConfig);
        stvSystemConfig = findViewById(R.id.stvSystemConfig);
        stvDeviceInfo = findViewById(R.id.stvDeviceInfo);
        stvUpdate = findViewById(R.id.stvUpdate);
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
        
    }

    @Override
    protected void initLisitener() {
        stvParams.setOnClickListener(this);
        stvUser.setOnClickListener(this);
        stvHttpConfig.setOnClickListener(this);
        stvSystemConfig.setOnClickListener(this);
        stvDeviceInfo.setOnClickListener(this);
        stvUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stvParams) {

        } else if (view.getId() == R.id.stvUser) {

        } else if (view.getId() == R.id.stvHttpConfig) {
            startActivity(SettingHttpActivity.class);
        } else if (view.getId() == R.id.stvSystemConfig) {
            startActivity(SettingSystemActivity.class);
        } else if (view.getId() == R.id.stvDeviceInfo) {

        } else if (view.getId() == R.id.stvUpdate) {
            //更新日志
            startActivity(SettingUpdateLogActivity.class);
        }
    }
}
