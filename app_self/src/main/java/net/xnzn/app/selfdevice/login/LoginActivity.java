package net.xnzn.app.selfdevice.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

public class LoginActivity extends SelfCommonActivity implements View.OnClickListener {

    private TextView tvCodeLogin, tvPwdLogin, tvGetCode, tvLogin;

    private View line1, line2;
    private EditText etUsername, etPwd;
    private ImageView ivCard, ivFace, ivScan;
    private int loginType;

    private static final int LOGIN_TYPE_PWD = 0;
    private static final int LOGIN_TYPE_CODE = 1;

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    @Override
    protected void countDownFinish() {
        //倒计时完成
        finish();
    }

    @Override
    protected void showCountDownTime(int time) {
        L.i("时间::" + time);
        tvCount.setText(time < 0 ? "" : (time + "s"));
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
    protected boolean showTimeTitle() {
        return false;
    }

    @Override
    protected void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.initView();
        tvCodeLogin = findViewById(R.id.tvCodeLogin);
        tvPwdLogin = findViewById(R.id.tvPwdLogin);

//        tvPwd = findViewById(R.id.tvPwd);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvLogin = findViewById(R.id.tvLogin);

        etUsername = findViewById(R.id.etUsername);
        etPwd = findViewById(R.id.etPwd);

        ivCard = findViewById(R.id.ivCard);
        ivFace = findViewById(R.id.ivFace);
        ivScan = findViewById(R.id.ivScan);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
    }

    @Override
    protected int showView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

        changeCodeLogin();

        setNoActionTime(10);
        setCountTime(20);
        startCountDown();

    }

    @Override
    protected void initLisitener() {
        tvPwdLogin.setOnClickListener(this);
        tvCodeLogin.setOnClickListener(this);

        tvLogin.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);

        ivCard.setOnClickListener(this);
        ivFace.setOnClickListener(this);
        ivScan.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvPwdLogin://切换用户名密码登录
                changePwdLogin();
                break;

            case R.id.tvCodeLogin:// 切换手机验证码登录
                changeCodeLogin();
                break;

            case R.id.tvGetCode://获取验证码
                if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtil.showShort(getString(R.string.toast_input_phone));
                    return;
                }
                getLoginCode();
                break;

            case R.id.tvLogin://登录

                loginCheck();

                break;

            case R.id.ivCard://切换刷卡登录
                break;

            case R.id.ivFace://切换刷脸登录
                break;

            case R.id.ivScan://切换扫码登录
                break;

        }
    }

    private void loginCheck() {

        switch (loginType) {
            case LOGIN_TYPE_PWD:
                if (TextUtils.isEmpty(etUsername.getText().toString())) {
                    ToastUtil.showShort(getResources().getString(R.string.etUserLoginInit));
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtil.showShort(getResources().getString(R.string.etPwdPlease));
                    return;
                }

                loginAction();

                break;
            case LOGIN_TYPE_CODE:

                if (TextUtils.isEmpty(etUsername.getText().toString())) {
                    ToastUtil.showShort(getResources().getString(R.string.etPhoneLoginInit));
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtil.showShort(getResources().getString(R.string.etCodeLogInit));
                    return;
                }
                loginAction();
                break;
        }

    }

    //TODO
    private void loginAction() {

    }

    private void getLoginCode() {

    }

    private void changePwdLogin() {
        loginType = LOGIN_TYPE_PWD;

        etUsername.setInputType(InputType.TYPE_CLASS_TEXT);
        etUsername.setHint(R.string.etUserLoginInit);
//        tvPwd.setText(R.string.login_pwd);
        etPwd.setHint(R.string.etPwdLogInit);
        tvGetCode.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.VISIBLE);
    }

    private void changeCodeLogin() {
        loginType = LOGIN_TYPE_CODE;

        etUsername.setInputType(InputType.TYPE_CLASS_PHONE);
        etUsername.setHint(R.string.etPhoneLoginInit);
//        tvPwd.setText(R.string.login_code);
        etPwd.setHint(R.string.etCodeLogInit);
        tvGetCode.setVisibility(View.VISIBLE);
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.GONE);
    }

}
