package net.xnzn.app.selfdevice.login;

import android.content.Intent;
import android.util.Log;

import net.xnzn.app.selfdevice.login.bean.request.UserLoginBean;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.net.SelfApiService;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class LoginPresenter {

    private static final String TAG = "LoginPresenter";
    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void loginIn( String serialNum, String custId, String nextPage) {

        Log.d(TAG, "serialNum:" + serialNum + ",custId:" + custId);
        //登录
        UserLoginBean userLoginBean = new UserLoginBean();
//        if (serialNum != null)
        userLoginBean.setSerialNum(serialNum);
//        userLoginBean.setMerchantId(faceId);
        userLoginBean.setCustId(custId);
        SelfApiService.queryUser(userLoginBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (response) -> {
                            if (response.isSuccess()) {
//                                Void data = response.getData();

                                loginSuc(nextPage, response);

//                                loginSuccess(response);
                            }
                        },
                        (throwable) -> {
                            loginFail(throwable);
                        }
                );


    }

    private void loginFail(Throwable throwable) {
        L.e("登录失败：" + throwable.getMessage());
        ToastUtil.showShort("登录失败：" + throwable.getMessage());
        loginView.loginFail("登录失败：" + throwable.getMessage());
    }

    private void loginSuc(String nextPage, YstResponse<List<YunUser>> response) {

        L.i("登录成功");

        if (loginView != null) {

            List<YunUser> data = response.getData();

            if (data == null || data.size() < 1) {
                L.i("未获取到用户的登录信息");
                loginView.loginFail("未获取到用户的登录信息");
                return;
            }

            LoginSuc.setLoginInfo(data.get(0));
            Log.d(TAG, "登录后的nextPage：" + nextPage);
            Intent m = new Intent(loginView.getMyAppContext(), NextPageAction.go2nextPage(nextPage));
            m.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginView.getMyAppContext().startActivity(m);
            loginView.loginSuccess(data.get(0));
        }
    }

}