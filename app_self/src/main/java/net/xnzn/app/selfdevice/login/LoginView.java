package net.xnzn.app.selfdevice.login;

import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.ui.BaseView;

public interface LoginView extends BaseView {

    void loginSuccess(YunUser user);


    void loginFail(String msg);
}
