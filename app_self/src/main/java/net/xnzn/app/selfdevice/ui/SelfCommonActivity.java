package net.xnzn.app.selfdevice.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.count.CountDownBaseActivity;
import net.xnzn.app.selfdevice.home.HomeActivity;

public abstract class SelfCommonActivity extends CountDownBaseActivity {

    private static final String TAG = "SelfCommonActivity";
    protected ImageView ivBack, ivBarHeadPic;
    protected TextView tvCount, tvBarContent, tvBarName, tvBack, tvExit;
    protected View in_title;
    protected View in_bar;
//    protected View in_time_title;

    //是否显示返回按钮
    protected abstract boolean showTitle();

    //是否显示导航栏目
    protected abstract boolean showBar();

    @Override
    protected void showCountDownTime(int time) {
//        L.i(TAG + "===showCountDownTime：：" + time);
        tvCount.setText(time < 0 ? "" : (time + "s"));
    }

    @Override
    protected void countDownFinish() {
        UserInfo.userName = "";
        UserInfo.isLogin = false;
        startActivity(HomeActivity.class);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_self_common;
    }

    @Override
    protected void initView() {

        in_title = findViewById(R.id.in_title);
        in_bar = findViewById(R.id.in_bar);
//        in_time_title = findViewById(R.id.in_time_title);

        ivBack = findViewById(R.id.ivBack);
        tvCount = findViewById(R.id.tvCount);
        tvBack = findViewById(R.id.tvBack);

        tvExit = findViewById(R.id.tvExit);
        ivBarHeadPic = findViewById(R.id.ivBarHeadPic);
        tvBarContent = findViewById(R.id.tvBarContent);
        tvBarName = findViewById(R.id.tvBarName);

        FrameLayout frame_home = findViewById(R.id.frame_home);
        LayoutInflater.from(this).inflate(this.showView(), frame_home);

        if (in_title != null)
            in_title.setVisibility(showTitle() ? View.VISIBLE : View.GONE);

        if (in_bar != null)
            in_bar.setVisibility(showBar() ? View.VISIBLE : View.GONE);

//        if (in_time_title != null)
//            in_time_title.setVisibility(showTimeTitle() ? View.VISIBLE : View.GONE);

        ivBack.setOnClickListener(view -> finish());
        tvBack.setOnClickListener(view -> finish());

        if (!UserInfo.isLogin) {
            tvExit.setVisibility(View.GONE);
            tvBarName.setVisibility(View.GONE);
            ivBarHeadPic.setVisibility(View.GONE);

        } else {
            tvExit.setVisibility(View.VISIBLE);
            tvBarName.setVisibility(View.VISIBLE);
            ivBarHeadPic.setVisibility(View.VISIBLE);
            tvBarName.setText(UserInfo.userName);
            tvExit.setOnClickListener(v -> {
                loginOut();
            });
        }
    }

    //TODO 退出登录
    protected void loginOut() {

    }

    protected abstract int showView();

    /**
     * 开始倒计时,
     *
     * @param noActionTime  无操作时间
     * @param countDownTime 倒计时时间
     */
    protected void countDown(int noActionTime, int countDownTime) {
        setCountTime(countDownTime);
        setNoActionTime(noActionTime);
        startCountDown();
    }
}
