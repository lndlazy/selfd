package net.xnzn.app.selfdevice.login;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.login.bean.ScanBean;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.qr.BarCodeManage;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.app.selfdevice.utils.SelfConstant;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.GsonUtil;
import net.xnzn.lib_utils.ToastUtil;

public class ScanLoginActivity extends SelfCommonActivity implements LoginView {

    private static final String TAG = "ScanLoginActivity";
    protected String nextPage;

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    private LoginPresenter loginPresenter = new LoginPresenter(this);

    private long lastTime;
    private String lastCode = "";

    @Override
    protected void initView() {
        super.initView();

        EditText etInput = findViewById(R.id.etInput);
        etInput.setInputType(InputType.TYPE_NULL);
        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String result = etInput.getText().toString();
                    Log.d(TAG, "扫描结果:" + result);

                    if (TextUtils.isEmpty(result))
                        return false;

                    if (System.currentTimeMillis() - lastTime < SelfConstant.SCAN_INTERVAL && result.equals(lastCode)) {
                        L.i(TAG + ",相同码连词扫描间隔小于最小时间秒");
                        return false;
                    }

                    lastCode = result;
                    lastTime = System.currentTimeMillis();

                    startLogin(result);

                }

                return false;
            }

        });
    }


    private void startLogin(String result) {

        L.i(TAG + ",开始二维码登录" + result);

        result = result.replace("xnzn", "");
        try {
            ScanBean scanBean = new Gson().fromJson(result, ScanBean.class);
            if (scanBean != null && !TextUtils.isEmpty(scanBean.getP()))
                loginPresenter.loginIn(null, scanBean.getP(), nextPage);
            else {
                L.i(TAG + "二维码未获取到用户信息");
                ToastUtil.showShort("未获取到用户信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.i(TAG + e.getMessage());

            ToastUtil.showShort("未获取到用户信息");
        }

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
        return R.layout.activity_scan_login;
    }

    @Override
    protected void initData() {
        super.initData();
        nextPage = getIntent().getStringExtra("nextPage");
//        BarCodeManage.getInstance().setListener(new BarCodeManage.Listener() {
//            @Override
//            public void onScan(String barCode) {
//
//                Log.d(TAG, "二维码内容::" + barCode);
//            }
//        });
    }

    @Override
    protected void initLisitener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        BarCodeManage.getInstance().release();
    }


    @Override
    public void loginSuccess(YunUser user) {
        finish();
    }

    @Override
    public void loginFail(String msg) {

    }

    @Override
    public Context getMyAppContext() {
        return getApplicationContext();
    }
}