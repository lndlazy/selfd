package net.xnzn.app.selfdevice.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.count.CountDownBaseActivity;

public abstract class SelfCommonActivity extends CountDownBaseActivity {

    protected ImageView ivBack, ivBarExit, ivBarHeadPic;
    protected TextView tvCount, tvBarContent, tvBarName, tvBack;
    protected View in_title;
    protected View in_bar;
    protected View in_time_title;

    //是否显示返回按钮
    protected abstract boolean showTitle();

    //是否显示导航栏目
    protected abstract boolean showBar();

    //是否显示时间栏
    protected abstract boolean showTimeTitle();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_self_common;
    }

    @Override
    protected void initView() {

        in_title = findViewById(R.id.in_title);
        in_bar = findViewById(R.id.in_bar);
        in_time_title = findViewById(R.id.in_time_title);

        ivBack = findViewById(R.id.ivBack);
        tvCount = findViewById(R.id.tvCount);
        tvBack = findViewById(R.id.tvBack);

        ivBarExit = findViewById(R.id.ivBarExit);
        ivBarHeadPic = findViewById(R.id.ivBarHeadPic);
        tvBarContent = findViewById(R.id.tvBarContent);
        tvBarName = findViewById(R.id.tvBarName);

        FrameLayout frame_home = findViewById(R.id.frame_home);
        LayoutInflater.from(this).inflate(this.showView(), frame_home);

        if (in_title != null)
            in_title.setVisibility(showTitle() ? View.VISIBLE : View.GONE);

        if (in_bar != null)
            in_bar.setVisibility(showBar() ? View.VISIBLE : View.GONE);

        if (in_time_title != null)
            in_time_title.setVisibility(showTimeTitle() ? View.VISIBLE : View.GONE);

        ivBack.setOnClickListener(view -> finish());
        tvBack.setOnClickListener(view -> finish());
    }

    protected abstract int showView();


}
