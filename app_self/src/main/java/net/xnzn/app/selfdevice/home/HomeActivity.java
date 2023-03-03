package net.xnzn.app.selfdevice.home;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.charge.ChargeActivity;
import net.xnzn.app.selfdevice.login.LoginActivity;
import net.xnzn.app.selfdevice.login.LoginSuc;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginBean;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.menu.MenuChooseActivity;
import net.xnzn.app.selfdevice.my.PersonalActivity;
import net.xnzn.app.selfdevice.net.SelfApiService;
import net.xnzn.app.selfdevice.query.QueryActivity;
import net.xnzn.app.selfdevice.query.bean.OrderListRequest;
import net.xnzn.app.selfdevice.setting.SettingActivity;
import net.xnzn.app.selfdevice.setting.bean.SelfSettingBean;
import net.xnzn.app.selfdevice.sign.SignActivity;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.DateHelper;
import net.xnzn.app.selfdevice.utils.NextPageConstant;
import net.xnzn.app.selfdevice.utils.TimeUtil;
import net.xnzn.lib_commin_ui.base.constant.Constant;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import me.jingbin.library.ByRecyclerView;
import me.jingbin.library.adapter.BaseByViewHolder;
import me.jingbin.library.adapter.BaseRecyclerAdapter;
import me.jingbin.library.decoration.SpacesItemDecoration;

public class HomeActivity extends SelfCommonActivity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";
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

        testApi();

        updateTime(System.currentTimeMillis());
        requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);

        initHomeItem();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        HomeAdapter homeAdapter = new HomeAdapter(this, homeItems);

//        homeAdapter.setNewData(homeItems);
////         选择2：设置颜色、高度、间距等
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this, SpacesItemDecoration.VERTICAL)
//                .setNoShowDivider(1, 1)
                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
                .setParam(R.color.translucent, 30, 0, 0);
//        SpacesItemDecoration itemDecoration2 = new SpacesItemDecoration(this, SpacesItemDecoration.HORIZONTAL)
////                .setNoShowDivider(1, 1)
//                // 颜色，分割线间距，左边距(单位dp)，右边距(单位dp)
//                .setParam(R.color.translucent, 20, 0, 0);

        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.addItemDecoration(itemDecoration2);
        BaseRecyclerAdapter<HomeItem> homeAdapter = new BaseRecyclerAdapter<HomeItem>(R.layout.layout_home_item) {

            @Override
            protected void bindView(BaseByViewHolder<HomeItem> holder, HomeItem bean, int position) {
                TextView tvDesc = holder.getView(R.id.tvDesc);

                holder.setBackgroundRes(R.id.cl, homeItems.get(position).getResId());
//                holder.setImageResource(R.id.ivImg, homeItems.get(position).getResId());
                holder.setText(R.id.tvContent, homeItems.get(position).getContent());

                if (TextUtils.isEmpty(homeItems.get(position).getDesc())) {
                    tvDesc.setVisibility(View.GONE);
                } else {
                    tvDesc.setVisibility(View.VISIBLE);
                    holder.setText(R.id.tvDesc, homeItems.get(position).getDesc());

                }

            }
        };
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.setNewData(homeItems);
        recyclerView.setOnItemClickListener(new ByRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {

                switch (position) {
                    case 0:
                        startActivity(MenuChooseActivity.class);
                        break;
                    case 1:
                        if (checkIsLogin(NextPageConstant.CHARGE_PAGE)) return;

                        startActivity(ChargeActivity.class);
                        break;
                    case 2:
                        if (checkIsLogin(NextPageConstant.QUERY_PAGE)) return;

                        startActivity(QueryActivity.class);
                        break;
                    case 3:
                        if (checkIsLogin(NextPageConstant.PERSON_PAGE)) return;

                        startActivity(PersonalActivity.class);
                        break;
                    case 4:
                        if (checkIsLogin(NextPageConstant.SIGN_PAGE)) return;

                        startActivity(SignActivity.class);
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
            ivHeadPic.setVisibility(View.VISIBLE);
            if (UserInfo.yunUser != null)
                tvUserName.setText(UserInfo.yunUser.getCustName());
        } else {
            tvUserName.setVisibility(View.GONE);
            ivHeadPic.setVisibility(View.GONE);
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
//                startActivity(UserInfo.isLogin ? SettingActivity.class : LoginActivity.class);
//                startActivity(SettingHttpActivity.class);
                showRootDialog();
                break;
            case R.id.ivHeadPic:

                //if (checkIsLogin()) return;

                showExitDialog();

                break;
        }
    }

    private void showExitDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_login_out);
            dialog.setCanceledOnTouchOutside(true);
            dialog.findViewById(R.id.tvCancel).setOnClickListener(view1 -> dialog.dismiss());
            dialog.findViewById(R.id.tvSure).setOnClickListener(v ->
            {
                loginOut();
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            });
        }
        if (!dialog.isShowing())
            dialog.show();
    }

    private Dialog settingDialog;

    private void showRootDialog() {

        if (settingDialog == null) {
            settingDialog = new Dialog(this);
            settingDialog.setContentView(R.layout.dialog_setting_input);
            settingDialog.setCanceledOnTouchOutside(true);
            settingDialog.findViewById(R.id.tvRootCancel).setOnClickListener(view1 -> settingDialog.dismiss());
            settingDialog.findViewById(R.id.tvRootSure).setOnClickListener(v ->
                    {
                        settingDialog.dismiss();
                        go2Setting();
                    }
            );
        }
        if (!settingDialog.isShowing())
            settingDialog.show();
    }

    private void go2Setting() {
        startActivity(SettingActivity.class);
    }

    private boolean checkIsLogin(String nextPage) {

//        Log.d(TAG, "isLogin::" + UserInfo.isLogin);
        if (!UserInfo.isLogin) {
            Intent m = new Intent(this, LoginActivity.class);
            m.putExtra("nextPage", nextPage);
            startActivity(m);
//            startActivity(LoginActivity.class);
            return true;
        }
        return false;
    }

    private void testApi() {

//        SelfApiService.menuInfo(new YunContent())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (response) -> {
//                            if (response.isSuccess()) {
//                                Void data = response.getData();
//                                L.i("menuInfo成功：");
//                            }
//                        },
//                        (throwable) -> {
//                            L.e("menuInfo失败：" + throwable.getMessage());
//                        }
//                );
//
//    }

//        //个人设置
//        SelfSettingBean settingBean = new SelfSettingBean();
//        SelfSettingBean.SelfServiceMetadate selfServiceMetadate = new SelfSettingBean.SelfServiceMetadate();
//        selfServiceMetadate.setIdentityFace("1");
//        selfServiceMetadate.setIdentityQR("1");
//        selfServiceMetadate.setIdentityCard("1");
//        settingBean.setMetadata(selfServiceMetadate);
//        SelfApiService.deviceInfo(settingBean)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (response) -> {
//                            if (response.isSuccess()) {
//                                Void data = response.getData();
//                                L.i("deviceInfo成功：");
//                            }
//                        },
//                        (throwable) -> {
//                            L.e("deviceInfo失败：" + throwable.getMessage());
//                        }
//                );

//        //登录
//        UserLoginBean userLoginBean = new UserLoginBean();
//        userLoginBean.setSerialNum("623E1E2F");
////        userLoginBean.setCustId("-1");
//        SelfApiService.queryUser(userLoginBean)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (response) -> {
//                            if (response.isSuccess()) {
////                                Void data = response.getData();
//                                L.i("queryUser成功：");
//                                List<YunUser> data = response.getData();
//
//                                if (data == null || data.size() < 1)
//                                    return;
//
//                                LoginSuc.setLoginInfo(data.get(0));
//
//                            }
//                        },
//                        (throwable) -> {
//                            L.e("queryUser失败：" + throwable.getMessage());
//                            ToastUtil.showShort("登录失败：" + throwable.getMessage());
//                        }
//                );

//        //列表
//        OrderListRequest orderListRequest = new OrderListRequest(1, TimeUtil.getCurrentYmd2(), 1, -1);
//        SelfApiService.getOrderList(orderListRequest)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (response) -> {
//                            if (response.isSuccess()) {
////                                Void data = response.getData();
//                                L.i("queryUser成功：");
//                            }
//                        },
//                        (throwable) -> {
//                            L.e("queryUser失败：" + throwable.getMessage());
//                        }
//                );

    }

}