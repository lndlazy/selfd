package net.xnzn.app.selfdevice.config;

import com.google.gson.Gson;

import net.xnzn.lib_utils.CacheDoubleStaticUtils;

public class DeviceConfigCache {
    public static final String KEY = "app_ct_device_config";

    public DeviceConfigCache() {
        
    }

    public static void put(DeviceConfig config) {
        CacheDoubleStaticUtils.put(KEY, new Gson().toJson(config));
    }

    public static DeviceConfig get() {
        String data = CacheDoubleStaticUtils.getString(KEY);
        return data == null ? new DeviceConfig() : new Gson().fromJson(data, DeviceConfig.class);
    }


}
