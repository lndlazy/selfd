package net.xnzn.app.selfdevice.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.charge.ChargeActivity;
import net.xnzn.app.selfdevice.home.HomeActivity;
import net.xnzn.app.selfdevice.login.bean.request.LoginUserResp;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginRequest;
import net.xnzn.app.selfdevice.my.PersonalActivity;
import net.xnzn.app.selfdevice.net.SelfApiService;
import net.xnzn.app.selfdevice.query.QueryActivity;
import net.xnzn.app.selfdevice.sign.SignActivity;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;

import net.xnzn.app.selfdevice.utils.NextPageConstant;
import net.xnzn.app.selfdevice.utils.SelfConstant;
import net.xnzn.leniu_http.util.AesUtil;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_http.yunshitang.util.SignUtil;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;


public class LoginActivity extends SelfCommonActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private TextView tvCodeLogin, tvPwdLogin, tvGetCode, tvLogin;

    private View line1, line2;
    private EditText etUsername, etPwd;
    private ImageView ivCard, ivFace, ivScan;
    private int loginType;

    private static final int LOGIN_TYPE_PWD = 0;
    private static final int LOGIN_TYPE_CODE = 1;
    protected String nextPage;

    @Override
    protected boolean showTitleBar() {
        return false;
    }

//    @Override
//    protected void countDownFinish() {
//        //???????????????
//        finish();
//    }

//    @Override
//    protected void showCountDownTime(int time) {
//        L.i("??????::" + time);
//        tvCount.setText(time < 0 ? "" : (time + "s"));
//    }

    @Override
    protected boolean showTitle() {
        return true;
    }

    @Override
    protected boolean showBar() {
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
        in_title.setBackground(null);
    }

    @Override
    protected int showView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        super.initData();//????????????initData ?????????????????????

        nextPage = getIntent().getStringExtra("nextPage");
        changeCodeLogin();


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

//            case R.id.ivBack:
//            case R.id.tvBack:
//                Log.d(TAG, "loginActivity????????????-->");
//                startActivity(HomeActivity.class);
//                break;

            case R.id.tvPwdLogin://???????????????????????????
                changePwdLogin();
                break;

            case R.id.tvCodeLogin:// ???????????????????????????
                changeCodeLogin();
                break;

            case R.id.tvGetCode://???????????????
                if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtil.showShort(getString(R.string.toast_input_phone));
                    return;
                }
                getLoginCode();
                break;

            case R.id.tvLogin://??????

                loginCheck();

                break;

            case R.id.ivCard://??????????????????
                Intent m = new Intent(this, CardLoginActivity.class);
                m.putExtra("nextPage", nextPage);
                startActivity(m);
                finish();
//                startActivity(CardLoginActivity.class);

                break;

            case R.id.ivFace://??????????????????

                Intent m1 = new Intent(this, FaceLoginActivity.class);
                m1.putExtra("nextPage", nextPage);
                startActivity(m1);
                finish();

//                startActivity(FaceLoginActivity.class);
                break;

            case R.id.ivScan://??????????????????
                Intent m2 = new Intent(this, ScanLoginActivity.class);
                m2.putExtra("nextPage", nextPage);
                startActivity(m2);
//                startActivity(ScanLoginActivity.class);

                finish();
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

    //??????
    private void loginAction() {
        try {
            String pwdEn = AesUtil.Encrypt("CSai@123");

            UserLoginRequest loginRequest = new UserLoginRequest("13900000000", pwdEn, "server", "password", "manager");

            SelfApiService.userLogin(loginRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (response) -> {
                                if (response.isSuccess()) {
                                    loginSuccess(response);
                                } else {
                                    ToastUtil.showShort(response.getMsg());
                                }
                            },
                            (throwable) -> {
                                L.i("???????????????" + throwable.getMessage());
                            }
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //????????????
    private void loginSuccess(YstResponse<LoginUserResp> response) {

        if (response == null || response.getData() == null)
            return;

        LoginUserResp logResp = response.getData();

//        UserInfo.isLogin = true;
//        UserInfo.yunUser =
//        go2nextPage();
        startActivity(NextPageAction.go2nextPage(nextPage));
        finish();
    }

//    //?????????????????????
//    private void go2nextPage() {
//
//        if (TextUtils.isEmpty(nextPage))
//            return;
//
//        switch (nextPage) {
//
//            case NextPageConstant.CHARGE_PAGE:
//                startActivity(ChargeActivity.class);
//                break;
//
//            case NextPageConstant.QUERY_PAGE:
//                startActivity(QueryActivity.class);
//                break;
//
//            case NextPageConstant.PERSON_PAGE:
//                startActivity(PersonalActivity.class);
//                break;
//
//            case NextPageConstant.SIGN_PAGE:
//                startActivity(SignActivity.class);
//                break;
//
//        }
//
//    }

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
