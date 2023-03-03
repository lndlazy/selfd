package net.xnzn.app.selfdevice.net;

import android.util.Log;

import net.xnzn.app.selfdevice.charge.req.AddBalanceRequest;
import net.xnzn.app.selfdevice.login.bean.request.LoginUserResp;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginBean;
import net.xnzn.app.selfdevice.login.bean.request.UserLoginRequest;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.setting.bean.SelfSettingBean;
import net.xnzn.app.selfdevice.sign.bean.Req.BookListRequest;
import net.xnzn.leniu_http.yunshitang.model.YunContent;
import net.xnzn.lib_http.RxYstHttp;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_http.yunshitang.util.SignUtil;
import net.xnzn.lib_utils.AppUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;

public class SelfApiService {
    private static final String TAG = "SelfApiService";

    private static final String MERCHANT_ID = "103307466409783296";

    //用户名密码登录
    public static final String POST_LOGIN_BY_PWD = "/auth/oauth/token";
    public static final String POST_MENUS_INFO = "/yunshitang/api/v1/android/ord/query/page/callnum";
    //充值
    public static final String POST_CHARGE = "/yunshitang/api/v1/android/acc/recharge/personal";

    //通过sn更新设备设置信息（设备设置信息查看同目录）
    public static final String POST_UPDATE_DEVICE_BY_SN = "/api/v3/android/device/updatemetadataBySn";

    //根据 卡码脸 查询用户信息
    public static final String QUERY_USER = "/yunshitang/api/v1/android/cust/custacctemp/queryCustAccTemp";

//    //查询设备订单信息
//    public static final String GET_ORDER_INFO = "/yunshitang/api/v1/android/ordInfo/MachineOrdInfo";

//    //报餐列表  XX
//    public static final String BOOK_LIST = "/yunshitang/api/v1/ord/book/cust/list";

    //登录
    public static Observable<YstResponse<LoginUserResp>> userLogin(UserLoginRequest request) {

        return RxYstHttp.postForm(POST_LOGIN_BY_PWD)
                .addAll(SignUtil.getParams(request))
//                .setHeader("Authorization", "Basic dGVzdDp0ZXN0")
                .asYstResponse(LoginUserResp.class);
    }

    //充值
    public static Observable<YstResponse<String>> charge(AddBalanceRequest request) {

        return RxYstHttp.postBody(POST_CHARGE)
                .setJsonBody(new YunContent(request))
                .setHeader("MERCHANT-ID", MERCHANT_ID)
                .asYstResponse(String.class);
    }


//    //报餐列表
//    public static Observable<YstResponse<List<String>>> bookList(BookListRequest request) {
//        return RxYstHttp.postBody(BOOK_LIST)
//                .setJsonBody(new YunContent(request))
//                .setHeader("MERCHANT-ID", MERCHANT_ID)
////                .addAll(SignUtil.getParams(request))
//                .asYstResponseList(String.class);
//    }

    //菜单详情
    public static Observable<YstResponse<Void>> menuInfo(YunContent request) {
        return RxYstHttp.postForm(POST_MENUS_INFO)
                .addAll(SignUtil.getParams(request))
                .asYstResponse(Void.class);
    }

    //查询用户信息
    public static Observable<YstResponse<List<YunUser>>> queryUser(UserLoginBean request) {

        HashMap<String, String> params = SignUtil.getParams(request);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d(TAG, "key:" + key + ",value:" + value);
        }

        return RxYstHttp.get(QUERY_USER)
                .addAll(params)
                .setHeader("MERCHANT-ID", MERCHANT_ID)
                .asYstResponseList(YunUser.class);
    }

    //通过sn更新设备设置信息（设备设置信息查看同目录）
    public static Observable<YstResponse<Void>> deviceInfo(SelfSettingBean request) {
        return RxYstHttp.postBody(POST_UPDATE_DEVICE_BY_SN)
                .setHeader("deviceSn", AppUtil.getSn())
                .setJsonBody(new YunContent(request))
                .asYstResponse(Void.class);
    }


//    //查询设备订单信息
//    public static Observable<YstResponse<List<Void>>> getOrderList(OrderListRequest request) {
//
//        HashMap<String, String> params = SignUtil.getParams(request);
////        RxYstHttp.getEncrypt()
//        return RxYstHttp.get(GET_ORDER_INFO)
//                .addAll(params)
//                .setHeader("MERCHANT-ID", MERCHANT_ID)
//                .asYstResponseList(Void.class);
//    }


} 