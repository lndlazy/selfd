package net.xnzn.app.selfdevice.common.auth;

import android.content.Context;
import android.text.TextUtils;

import com.srp.AuthApi.AuthApi;
import com.srp.AuthApi.AuthApplyResponse;
import com.srp.AuthApi.ErrorCodeConfig;


import net.xnzn.app.selfdevice.utils.FileUtil;

import java.io.IOException;


public class FacePassAuth {
    public static final String CERT_PATH = "/Download/leniu.cert";
    private Lisitener mLisitener;
    private static FacePassAuth facePassAuthUtil = new FacePassAuth();

    private FacePassAuth() {

    }

    public static FacePassAuth getInstance() {
        return facePassAuthUtil;
    }

    public void auth(Context applicationContext, Lisitener lisitener) {
        this.mLisitener = lisitener;
        FacePassAuthResult authResult = new FacePassAuthResult();


        AuthApi obj = new AuthApi();
        if (obj.authCheck(applicationContext)) {
            authResult.setAuth(true);
            authResult.setMessage("已授权");
            if (mLisitener != null) {
                mLisitener.onAuthResult(authResult);
            }
            return;
        }


        String cert = null;
        try {
            cert = FileUtil.readExternal(CERT_PATH).trim();
        } catch (IOException e) {
            e.printStackTrace();
            authResult.setAuth(false);
            authResult.setMessage("读取授权证书发生异常：" + e.getMessage());
            if (mLisitener != null) {
                mLisitener.onAuthResult(authResult);
            }
            return;
        }

        if (TextUtils.isEmpty(cert)) {
            authResult.setAuth(false);
            authResult.setMessage("证书不存在");
            if (mLisitener != null) {
                mLisitener.onAuthResult(authResult);
            }
            return;
        }

        obj.authDevice(applicationContext, cert, "", new AuthApi.AuthDeviceCallBack() {
            @Override
            public void GetAuthDeviceResult(final AuthApplyResponse result) {
                if (result.errorCode == ErrorCodeConfig.AUTH_SUCCESS) {
                    authResult.setAuth(true);
                    authResult.setMessage("Apply update: OK");
                } else {
                    authResult.setAuth(false);
                    authResult.setMessage("授权失败，错误码:" + result.errorCode + " , 错误信息:" + result.errorMessage);
                }
            }
        });

        if (mLisitener != null) {
            mLisitener.onAuthResult(authResult);
        }

    }


    public interface Lisitener {
        void onAuthResult(FacePassAuthResult authResult);
    }

}
