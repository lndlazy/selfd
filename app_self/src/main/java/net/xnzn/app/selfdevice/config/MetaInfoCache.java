package net.xnzn.app.selfdevice.config;

import com.google.gson.Gson;

import net.xnzn.lib_utils.CacheDoubleStaticUtils;

public class MetaInfoCache {
    public static final String KEY = "app_bind_alipay_meta_info_config";

    public MetaInfoCache() {

    }

    public static void put(MetaInfoConfig config) {
        CacheDoubleStaticUtils.put(KEY, new Gson().toJson(config));
    }

    public static MetaInfoConfig get() {
        String data = CacheDoubleStaticUtils.getString(KEY);
        return data == null ? new MetaInfoConfig() : new Gson().fromJson(data, MetaInfoConfig.class);
    }

}
