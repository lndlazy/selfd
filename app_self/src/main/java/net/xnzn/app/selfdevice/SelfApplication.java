package net.xnzn.app.selfdevice;

import net.xnzn.leniu_common_sdk.app.BaseApplication;
import net.xnzn.lib_log.L;

public class SelfApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        L.init();

    }
}
