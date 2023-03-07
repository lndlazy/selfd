package net.xnzn.app.selfdevice;

import net.xnzn.leniu_common_sdk.app.BaseApplication;
import net.xnzn.lib_log.L;
import net.xnzn.lib_webservice.WebService;

public class SelfApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        WebService.getInstance().init(this);
    }
}
