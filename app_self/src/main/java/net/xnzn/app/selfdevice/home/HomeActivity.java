package net.xnzn.app.selfdevice.home;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.charge.ChargeActivity;
import net.xnzn.app.selfdevice.login.LoginActivity;
import net.xnzn.app.selfdevice.menu.MenuChooseActivity;
import net.xnzn.app.selfdevice.query.QueryActivity;
import net.xnzn.app.selfdevice.setting.SettingActivity;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.DateHelper;
import net.xnzn.lib_commin_ui.CommonDialog;
import net.xnzn.lib_commin_ui.base.constant.Constant;
import net.xnzn.lib_log.L;

import java.util.ArrayList;
import java.util.List;

import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.decoration.SpacesItemDecoration;

public class HomeActivity extends SelfCommonActivity implements View.OnClickListener {

    private TextView tvTime, tvDate, tvWeek, tvUserName;
    private ImageView ivSetting, ivHeadPic;
    private ByRecyclerView recyclerView;

    private List<HomeItem> homeItems = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected boolean showTitle() {
        return false;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

//    @Override
//    protected boolean showTimeTitle() {
//        return true;
//    }

//    @Override
//    protected int setLayoutId() {
//        return R.layout.activity_home;
//    }

    @Override
    protected void initView() {
        super.initView();
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        tvWeek = findViewById(R.id.tvWeek);
        tvUserName = findViewById(R.id.tvUserName);

        ivSetting = findViewById(R.id.ivSetting);
        ivHeadPic = findViewById(R.id.ivHeadPic);
        recyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    protected int showView() {
        return R.layout.activity_home;
    }

    private void initHomeItem() {

        homeItems.add(new HomeItem(R.mipmap.ic_menu, "菜单公示", ""));
        homeItems.add(new HomeItem(R.mipmap.ic_charge, "余额/充值", ""));
        homeItems.add(new HomeItem(R.mipmap.ic_query, "综合查询", "订单、营养、健康"));
        homeItems.add(new HomeItem(R.mipmap.ic_personal, "个人信息", "照片、地址、密码"));
        homeItems.add(new HomeItem(R.mipmap.ic_sign, "就餐签到", ""));

    }

    @Override
    protected void initData() {

        updateTime(System.currentTimeMillis());
        requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);

        initHomeItem();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        HomeAdapter homeAdapter = new HomeAdapter(this, homeItems);

        homeAdapter.setNewData(homeItems);
////         选择2：设置颜色、高度、间距等
//        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL)
////                .setNoShowDivider(1, 1)
//                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
//                .setParam(R.color.translucent, 30, 0, 0);
        SpacesItemDecoration itemDecoration2 = new SpacesItemDecoration(this, SpacesItemDecoration.HORIZONTAL)
//                .setNoShowDivider(1, 1)
                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
                .setParam(R.color.translucent, 20, 0, 0);

//        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(itemDecoration2);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                switch (position) {
                    case 0:
                        startActivity(MenuChooseActivity.class);
                        break;
                    case 1:
                        startActivity(ChargeActivity.class);
                        break;
                    case 2:
                        startActivity(QueryActivity.class);
                        break;

                }
            }
        });
    }

    @Override
    protected void initLisitener() {
        ivSetting.setOnClickListener(this);
        ivHeadPic.setOnClickListener(this);
    }

    @Override
    protected boolean openTimeChange() {
        return true;
    }


    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected boolean showNavBar() {
        return false;
    }

    @Override
    protected void countDownFinish() {

    }

    @Override
    protected void showCountDownTime(int time) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserInfo.isLogin) {
            tvUserName.setVisibility(View.VISIBLE);
            tvUserName.setText(UserInfo.userName);
        } else {
            tvUserName.setVisibility(View.GONE);
            tvUserName.setText("");
        }
    }

    @Override
    protected void timeChange(long time) {
        super.timeChange(time);

        L.i("time change ：" + time);

        updateTime(time);
    }

    private void updateTime(long time) {
        tvTime.setText(DateHelper.getHmByTime(time));
        tvDate.setText(DateHelper.getMdByTime(time));
        tvWeek.setText(DateHelper.getCurrentWeek());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivSetting:
                startActivity(UserInfo.isLogin ? SettingActivity.class : LoginActivity.class);
                break;
            case R.id.ivHeadPic:
//                if (!UserInfo.isLogin)
//                    startActivity(LoginActivity.class);
//                else {

                if (dialog == null) {
                    dialog = new Dialog(this);
                    dialog.setContentView(R.layout.dialog_login_out);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.findViewById(R.id.tvCancel).setOnClickListener(view1 -> dialog.dismiss());
                    dialog.findViewById(R.id.tvSure).setOnClickListener(v -> loginOut());
                }
                if (!dialog.isShowing())
                    dialog.show();

//                CommonDialog commonDialog = new CommonDialog(this, "", "是否确认退出登录？", "确认", "取消", new CommonDialog.DialogClickListener() {
//                    @Override
//                    public void onDialogClick() {
//                        L.i("退出确认");
//                    }
//                });
//
//                commonDialog.show();
//                }
                break;
        }
    }


}