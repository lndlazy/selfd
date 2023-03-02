package net.xnzn.app.selfdevice.net;

import net.xnzn.app.selfdevice.charge.req.AddBalanceRequest;
import net.xnzn.app.selfdevice.login.bean.request.LoginUserResp;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginRequest;
import net.xnzn.leniu_http.yunshitang.model.YunContent;
import net.xnzn.lib_http.RxYstHttp;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_http.yunshitang.util.SignUtil;

import io.reactivex.rxjava3.core.Observable;

public class SelfApiService {

    //用户名密码登录
    public static final String POST_LOGIN_BY_PWD = "/auth/oauth/token";
    public static final String POST_MENUS_INFO = "/yunshitang/api/v1/android/ord/query/page/callnum";
    //充值
    public static final String POST_CHARGE = "/yunshitang/api/v1/android/acc/recharge/personal";


    public static Observable<YstResponse<LoginUserResp>> userLogin(UserLoginRequest request) {

        return RxYstHttp.postForm(POST_LOGIN_BY_PWD)
                .addAll(SignUtil.getParams(request))
//                .setHeader("Authorization", "Basic dGVzdDp0ZXN0")
                .asYstResponse(LoginUserResp.class);
    }

    public static Observable<YstResponse<Void>> charge(AddBalanceRequest request) {
//        return RxYstHttp.postForm(POST_CHARGE)
        return RxYstHttp.postBody(POST_CHARGE)
                .setJsonBody(new YunContent(request))
//                .addAll(SignUtil.getParams(request))
                .asYstResponse(Void.class);
    }

    public static Observable<YstResponse<Void>> menuInfo(YunContent request) {
        return RxYstHttp.postForm(POST_MENUS_INFO)
                .addAll(SignUtil.getParams(request))
                .asYstResponse(Void.class);
    }
} 