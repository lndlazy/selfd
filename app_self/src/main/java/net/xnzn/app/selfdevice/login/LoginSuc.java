package net.xnzn.app.selfdevice.login;

import net.xnzn.app.selfdevice.UserInfo;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;

public class LoginSuc {

    public static void setLoginInfo(YunUser yunUser) {

        UserInfo.isLogin = true;
        UserInfo.yunUser = yunUser;

    }
} 