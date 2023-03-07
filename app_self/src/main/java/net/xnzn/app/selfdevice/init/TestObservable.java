package net.xnzn.app.selfdevice.init;

import android.text.TextUtils;
import android.util.Log;

import net.xnzn.leniu_db.LeniuDB;
import net.xnzn.leniu_db.table.Product;
import net.xnzn.leniu_http.yunshitang.api.DeviceApi;
import net.xnzn.leniu_http.yunshitang.api.FoodApi;
import net.xnzn.leniu_http.yunshitang.config.YunBaseInfoCache;
import net.xnzn.leniu_http.yunshitang.config.YunDeviceCache;
import net.xnzn.leniu_http.yunshitang.config.YunFoodCache;
import net.xnzn.leniu_http.yunshitang.model.request.RecipeRequest;
import net.xnzn.leniu_http.yunshitang.model.response.Merchant;
import net.xnzn.leniu_http.yunshitang.model.response.YunBaseInfo;
import net.xnzn.leniu_http.yunshitang.model.response.YunDevice;
import net.xnzn.leniu_http.yunshitang.model.response.YunFood;
import net.xnzn.leniu_http.yunshitang.model.response.YunFoodAll;
import net.xnzn.lib_http.config.HttpConfig;
import net.xnzn.lib_http.config.HttpConfigCache;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_utils.AppUtil;
import net.xnzn.lib_utils.NetworkUtil;
import net.xnzn.lib_utils.TimeUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestObservable {


    private static final String TAG = TestObservable.class.getSimpleName();


    public static Observable<InitInfo> testIp() {
        return Observable.create(new ObservableOnSubscribe<InitInfo>() {
            @Override
            public void subscribe(ObservableEmitter<InitInfo> emitter) throws Exception {

                Log.e(TAG, "thread1 id:" + Thread.currentThread().getId() + Thread.currentThread().getName());

                //1、服务器ip连通性测试
                String ip = null;
                if (HttpConfigCache.get().isIpMode()) {
                    ip = HttpConfigCache.get().getIp();
                } else {
                    ip = HttpConfigCache.get().getDomain();
                }

                boolean result = NetworkUtil.isAvailableByPing(ip);
                if (result) {
                    emitter.onNext(new InitInfo(true, "服务器IP(" + ip + ") 连通性，正常"));
                } else {
                    emitter.onNext(new InitInfo(false, "服务器IP(" + ip + ") 连通性，异常"));
                }
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<InitInfo> testPort() {
        return Observable.create(new ObservableOnSubscribe<InitInfo>() {

            @Override
            public void subscribe(ObservableEmitter<InitInfo> emitter) throws Exception {

                Log.e(TAG, "thread2 id:" + Thread.currentThread().getId() + Thread.currentThread().getName());

                if (HttpConfigCache.get().isIpMode()) {
                    Socket client = null;
                    //ip方式
                    try {
                        client = new Socket(HttpConfigCache.get().getIp(), Integer.parseInt(HttpConfigCache.get().getPort()));
                        emitter.onNext(new InitInfo(true, "服务器port(" + HttpConfigCache.get().getPort() + ") 连通性，正常"));

                    } catch (IOException e) {
                        e.printStackTrace();
                        emitter.onNext(new InitInfo(false, "服务器port(" + HttpConfigCache.get().getPort() + ") 连通性，异常"));

                    } finally {
                        try {
                            if (client != null) {
                                client.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //域名模式
                    emitter.onNext(new InitInfo(true, "服务器port(域名访问方式)"));
                }

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<InitInfo> testServer() {
        return DeviceApi
                .getMerchantId()
                .map(new Function<YstResponse<Merchant>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<Merchant> response) throws Throwable {
//                        if (response.getCode() == 10000 && response.getData() == null) {
//                            return new InitInfo(false, "设备未在管理平台添加，请先添加");
//                        } else {
//                            return new InitInfo(true, "服务器联通性，正常");
//                        }
                        return new InitInfo(true, "服务器连通性，正常");
                    }
                }).subscribeOn(Schedulers.io());
    }


    public static Observable<InitInfo> testDeviceAdd() {
        return DeviceApi
                .getMerchantId()
                .map(new Function<YstResponse<Merchant>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<Merchant> response) throws Throwable {
                        if (response.getCode() == 10000 && response.getData() != null) {
                            //判断当前获取的商户号和本地商户号是否一致，不一致需要清空
                            Long merchantId = response.getData().getMerchantId();
                            HttpConfig config = HttpConfigCache.get();

                            if (TextUtils.isEmpty(config.getHeaderMerchantId())) {
                                //第一次请求数据
                                config.setHeaderMerchantId(merchantId + "");
                            } else {
                                //非第一次请求数据
                                if (TextUtils.equals(merchantId + "", config.getHeaderMerchantId())) {
                                    //商户号相同
                                } else {
                                    //商户号不同
                                    config.setHeaderMerchantId(merchantId + "");
                                }
                            }

                            HttpConfigCache.put(config);

                            return new InitInfo(true, "设备已在管理平台添加");
                        } else {
                            throw new Throwable("设备未在管理平台添加，请先添加");
//                            return new InitInfo(false, "设备未在管理平台添加，请先添加");
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }


    public static Observable<InitInfo> getDeviceInfo() {
        return DeviceApi
                .getDeviceInfo()
                .map(new Function<YstResponse<YunDevice>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<YunDevice> response) throws Throwable {
                        if (response.getCode() == 10000) {
                            YunDevice device = response.getData();
                            HttpConfig httpConfig = HttpConfigCache.get();
                            httpConfig.setHeaderMachineNum(device.getMchNum());
                            HttpConfigCache.put(httpConfig);

                            YunDeviceCache.put(response.getData());

                            return new InitInfo(true, "获取设备基础信息,成功");
                        } else {
                            return new InitInfo(false, "获取设备基础信息,失败 " + response.getMsg());
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }


    public static Observable<InitInfo> getBaseInfo() {
        return DeviceApi
                .getBaseInfo()
                .map(new Function<YstResponse<YunBaseInfo>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<YunBaseInfo> response) throws Throwable {
                        if (response.isSuccess()) {
                            YunBaseInfo device = response.getData();
                            YunBaseInfoCache.put(device);
                            return new InitInfo(true, "获取卡片、餐次信息,成功");
                        } else {
                            return new InitInfo(false, "获取卡片、餐次信息,失败 " + response.getErrorInfo());
                        }
                    }
                });
    }


    public static Observable<InitInfo> queryFoods() {
        Long recipeId = YunDeviceCache.get().getRecipeId();
        RecipeRequest request = new RecipeRequest(recipeId, TimeUtil.getCurrentYmd());
        return FoodApi
                .queryFoodsByRecipeId(request)
                .map(new Function<YstResponse<List<YunFood>>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<List<YunFood>> response) throws Throwable {
                        if (response.getCode() == 10000) {
                            if (response.getData() == null || response.getData().size() == 0) {
                                LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().deleteAllByProductType(1); //删除时段菜品
                                return new InitInfo(true, "获取菜谱,成功:菜品信息为空");
                            }
                            //存储本地菜品信息
                            YunFoodCache.put(response.getData());
                            //存储到数据库

                            List<Product> data = tran2IntervalProduct(request.getApplyDate(), response.getData());
                            LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().deleteAllByProductType(1); //删除时段菜品
                            LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().insert(data);

                            return new InitInfo(true, "获取菜谱,成功");
                        } else {
                            return new InitInfo(false, "获取菜谱,失败 " + response.getMsg());
                        }
                    }
                });
    }


    public static Observable<InitInfo> getAllFood() {
        return FoodApi
                .getAllFood()
                .map(new Function<YstResponse<List<YunFoodAll>>, InitInfo>() {
                    @Override
                    public InitInfo apply(YstResponse<List<YunFoodAll>> response) throws Throwable {
                        if (response.getCode() == 10000) {
                            if (response.getData() == null || response.getData().size() == 0) {
                                // TODO: 2022-11-16
                                //清空本地数据库全量菜谱
                                LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().deleteAll();
                                return new InitInfo(true, "获取全量菜品,成功:菜品信息为空，清空本地全量菜谱");
                            }
                            List<Product> data = tran2Product(response.getData());
                            LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().deleteAllByProductType(2); //删除全量菜品
                            LeniuDB.getInstance(AppUtil.getApplication()).ProductDao().insert(data);
                            return new InitInfo(true, "获取全量菜品,成功：" + response.getData().size() + "个菜品");
                        } else {
                            return new InitInfo(false, "获取全量菜品,失败 " + response.getMsg());
                        }
                    }
                });
    }


    private static List<Product> tran2Product(List<YunFoodAll> foods) {
        List<Product> products = new ArrayList<>(foods.size());
        for (int i = 0; i < foods.size(); i++) {
            YunFoodAll item = foods.get(i);
            Product product = new Product();
            product.setProductType(2);//全量菜品
            product.setProductId(item.getGoodsId());
            product.setProductName(item.getProductName());
            product.setCustomId(item.getCustomId());
            product.setDetailId(item.getDetailId());
//            product.setProductTypeId(item.getTypeId());
            product.setProductTypeName(item.getTypeName());
            product.setImageUrl(item.getImageUrl());
            product.setOriginalPrice(item.getOriginalPrice());
//            product.setSalesMode();
            product.setSalesPrice(item.getSalePrice());
//            product.setDiscountPrice();
            product.setCalories(item.getCalories() == null ? "0" : item.getCalories() + "");
            product.setProtein(item.getProtein() == null ? "0" : item.getProtein() + "");
            product.setFat(item.getFat() == null ? "0" : item.getFat() + "");
            product.setCarbohydrate(item.getCarbohydrate() == null ? "0" : item.getCarbohydrate() + "");
            product.setDietaryFiber(item.getDietaryFiber() == null ? "0" : item.getDietaryFiber() + "");
            product.setCholesterol(item.getCholesterol() == null ? "0" : item.getCholesterol() + "");
            product.setCalcium(item.getCalcium() == null ? "0" : item.getCalcium() + "");
            product.setSodium(item.getSodium() == null ? "0" : item.getSodium() + "");
            products.add(product);
        }

        return products;

    }

    private static List<Product> tran2IntervalProduct(String applyDate, List<YunFood> data) {


        List<Product> products = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {

            List<YunFood.FoodType> types = data.get(i).getTypeList();
            if (types != null && types.size() > 0) {
                for (int j = 0; j < types.size(); j++) {

                    List<YunFood.FoodDetail> foods = types.get(j).getTypeDetail();
                    if (foods != null && foods.size() > 0) {

                        for (int k = 0; k < foods.size(); k++) {
                            YunFood.FoodDetail item = foods.get(k);
                            Product product = new Product();
                            product.setProductType(1);//时段菜品

                            product.setProductDate(applyDate);
                            product.setIntervalId(data.get(i).getIntervalId());
                            product.setIntervalName(data.get(i).getIntervalName());
                            product.setDetailId(data.get(i).getDetailId());

                            product.setProductTypeId(types.get(j).getTypeId());
                            product.setProductTypeName(types.get(j).getTypeName());


                            product.setProductId(item.getProductId());
                            product.setProductName(item.getProductName());
                            product.setCustomId(item.getCustomId());

                            product.setImageUrl(item.getImageUrl());
                            product.setOriginalPrice(item.getOriginalPrice());
                            product.setSalesMode(item.getSalesMode());
                            product.setSalesPrice(item.getSalePrice());
                            // product.setDiscountPrice();
                            product.setCalories(item.getCalories() + "");
                            product.setProtein(item.getProtein() + "");
                            product.setFat(item.getFat() + "");
                            product.setCarbohydrate(item.getCarbohydrate() + "");
                            product.setDietaryFiber(item.getDietaryFiber() + "");
                            product.setCholesterol(item.getCholesterol() + "");
                            product.setCalcium(item.getCalcium() + "");
                            product.setSodium(item.getSodium() + "");
                            products.add(product);
                        }
                    }

                }
            }
        }

        return products;

    }


    /**
     * 更新用户数据
     */
    public static Observable<InitInfo> getUserList() {
        return null;
    }
}
