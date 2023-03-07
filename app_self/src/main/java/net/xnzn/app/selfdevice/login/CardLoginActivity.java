package net.xnzn.app.selfdevice.login;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginBean;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.net.SelfApiService;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_log.L;
import net.xnzn.lib_reader.ReaderManage;
import net.xnzn.lib_reader.base.IReader;
import net.xnzn.lib_reader.config.ReaderConfig;
import net.xnzn.lib_utils.ToastUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class CardLoginActivity extends SelfCommonActivity implements LoginView {

    private static final String TAG = "CardLoginActivity";
    protected String nextPage;

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @Override
    protected void initView() {
        super.initView();
        nextPage = getIntent().getStringExtra("nextPage");

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
        super.initData();

        initReader();
    }

    @Override
    protected void initLisitener() {

    }


    private void initReader() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ReaderConfig readerConfig = new ReaderConfig();
                ReaderManage instance = ReaderManage.getInstance();
                instance.setDebug(false)
                        .setReaderConfig(readerConfig)
                        .addReaderListener(new IReader.Listener() {
                            @Override
                            public void onReadSuccess(String kxh, Map map) {
                                Log.d(TAG, "读取到的卡号信息:" + kxh);
//                        ToastUtil.showShort("卡号：" + kxh);
//                        request.setNuClearMode(1);
//                        request.setSerialNum(kxh);
//                        stopVerify();
//                        uploadData();

                                cardLogin(kxh);
                            }

                            @Override
                            public void onReadFail(int i, String s) {
                                Log.d(TAG, "读卡失败onReadFail:" + s);
                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.d(TAG, "读卡失败onError:" + s);

                            }
                        });
                SystemClock.sleep(2000);
                instance.startRead();
            }
        }).start();

    }

    private void cardLogin(String kxh) {

        loginPresenter.loginIn(kxh, null, nextPage);
//        //登录
//        UserLoginBean userLoginBean = new UserLoginBean();
//        userLoginBean.setSerialNum(kxh);
////        userLoginBean.setCustId("-1");
//        SelfApiService.queryUser(userLoginBean)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (response) -> {
//                            if (response.isSuccess()) {
////                                Void data = response.getData();
//                                L.i("queryUser成功：");
//
//                                loginSuccess(response);
//
//                            }
//                        },
//                        (throwable) -> {
//                            L.e("queryUser失败：" + throwable.getMessage());
//                            ToastUtil.showShort("登录失败：" + throwable.getMessage());
//                        }
//                );

    }

//    private void loginSuccess(YstResponse<List<YunUser>> response) {
//        List<YunUser> data = response.getData();
//
//        if (data == null || data.size() < 1)
//            return;
//
//        LoginSuc.setLoginInfo(data.get(0));
//
//        startActivity(NextPageAction.go2nextPage(nextPage));
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ReaderManage.getInstance().release();
    }

    @Override
    public void loginSuccess(YunUser user) {
        finish();
    }

    @Override
    public void loginFail(String msg) {

    }

}