package net.xnzn.app.selfdevice;

import android.app.Application;

import net.xnzn.lib_log.L;
import net.xnzn.lib_utils.ToastUtil;

public class SelfApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init();
        L.init();
    }
}
