package net.xnzn.app.selfdevice.init;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.common.LeniuUsbManger;
import net.xnzn.app.selfdevice.home.HomeActivity;
import net.xnzn.app.selfdevice.lib_facepass_3568.FacePassManage3568;
import net.xnzn.app.selfdevice.setting.SettingActivity;
import net.xnzn.leniu_db.LeniuDB;
import net.xnzn.leniu_db.table.User;
import net.xnzn.leniu_http.yunshitang.api.UserApi;
import net.xnzn.leniu_http.yunshitang.model.request.UserRequest;
import net.xnzn.leniu_http.yunshitang.model.response.UserResponse;
import net.xnzn.lib_commin_ui.base.BaseTitleBarActivity;
import net.xnzn.lib_commin_ui.base.constant.Constant;
import net.xnzn.lib_http.HttpManager;
import net.xnzn.lib_http.RxYstHttp;
import net.xnzn.lib_http.config.HttpConfig;
import net.xnzn.lib_http.config.HttpConfigCache;
import net.xnzn.lib_http.yunshitang.YstResponse;
import net.xnzn.lib_log.L;
import net.xnzn.lib_qrcode.zxing.util.QRCodeCreater;
import net.xnzn.lib_utils.AppUtil;
import net.xnzn.lib_utils.CacheDoubleStaticUtils;
import net.xnzn.lib_utils.ImageUtil;
import net.xnzn.lib_utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import me.jingbin.library.ByRecyclerView;
import rxhttp.wrapper.exception.HttpStatusCodeException;

public class InitActivity extends BaseTitleBarActivity {

    private static final String TAG = InitActivity.class.getSimpleName();
    private Handler handler = new Handler(Looper.getMainLooper());

    //UI
    private ByRecyclerView rv;
    private TextView tvSn, tvKey, tvInitEnd, tvDevice;
    private ImageView ivSn;
    private TextView
            tvNetWorkType,
            tvIp,
            tvMask,
            tvGateway,
            tvDns0,
            tvDns1;


    //data
    private InitAdapter adapter;
    private List<InitInfo> data = new ArrayList<>();


    @Override
    protected boolean showNavBar() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_init;
    }

    @Override
    protected void initView() {
        tvKey = findViewById(R.id.tvKey);
        ivSn = findViewById(R.id.ivSn);
        tvSn = findViewById(R.id.tvSn);
        rv = findViewById(R.id.rv);
        tvNetWorkType = findViewById(R.id.tvNetWorkType);
        tvIp = findViewById(R.id.tvIp);
        tvMask = findViewById(R.id.tvMask);
        tvGateway = findViewById(R.id.tvGateway);
        tvDns0 = findViewById(R.id.tvDns0);
        tvDns1 = findViewById(R.id.tvDns1);
        tvInitEnd = findViewById(R.id.tvInitEnd);
        tvDevice = findViewById(R.id.tvDevice);


//        FacePassAuth.getInstance().auth(this, new FacePassAuth.Lisitener() {
//            @Override
//            public void onAuthResult(FacePassAuthResult authResult) {
//                Log.e("FacePassAuth", "旷视初始化结果:" + authResult.toString());
//            }
//        });


    }

    @Override
    protected void initData() {

//        DeviceConfig config = DeviceConfigCache.get();
//        if (config.getWorkMode() == 2) {
//            startActivity(InputActivity.class);
//            finish();
//            return;
//        }

        requestPermission(Constant.WRITE_READ_EXTERNAL_CODE, Constant.WRITE_READ_EXTERNAL_PERMISSION);

        setTitle("设备初始化");
        setRightTitle("进入设置");

        userUpdatetime = LeniuDB.getInstance(InitActivity.this).UserDao().queryLastUserTime();

        adapter = new InitAdapter();
        adapter.setNewData(data);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        //初始化信息
        showDeviceNetworkInfo();

    }


    private String userUpdatetime;
    private int userUpdateCount;

    public void getUserInfo() {
        UserRequest request = new UserRequest(1000, userUpdatetime);
        UserApi
                .getUserList(request)
                .doOnNext(new Consumer<YstResponse<List<UserResponse>>>() {
                    @Override
                    public void accept(YstResponse<List<UserResponse>> response) throws Throwable {
                        List<UserResponse> users = response.getData();
                        if (users != null && users.size() > 0) {
                            //存储到数据库
                            Log.i(TAG, "用户信息个数::" + users.size());
                            List<User> dbUsers = new ArrayList<>(users.size());
                            for (int i = 0; i < users.size(); i++) {
                                UserResponse item = users.get(i);
                                User user = new User();

                                user.setFaceId(item.getIdtemporary());
                                user.setCustId(item.getCustId());
                                user.setCustName(item.getCustName());
                                user.setCustNum(item.getCustNum());
                                user.setSerialNum(item.getSerialNum());
                                user.setDepartment(item.getOrgFullName());
                                user.setCustPhotoUrl(item.getCustPhotoUrl());
                                user.setAccStatus(item.getAccStatus());
                                user.setCardType(item.getCardType());
                                user.setCustState(item.getCustState());
                                user.setCardStatus(item.getCardStatus());
                                user.setFaceStatus(item.getPhotoState());
                                user.setUpdateTime(item.getUptime());
                                user.setAddFace(0);

                                dbUsers.add(user);
                            }
                            LeniuDB.getInstance(InitActivity.this).UserDao().insert(dbUsers);


                        }
                    }
                })
                .to(RxLife.toMain(this))
                .subscribe(new Observer<YstResponse<List<UserResponse>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull YstResponse<List<UserResponse>> response) {
                        List<UserResponse> users = response.getData();
                        if (users == null || users.size() == 0) {
                            updateUserFinish();
                        } else {
                            if (userUpdateCount == 0) {
                                userUpdateCount = userUpdateCount + users.size();
                                adapter.addData(new InitInfo(true, "更新用户：" + userUpdateCount + "条"));
                            } else {
                                userUpdateCount = userUpdateCount + users.size();
                                adapter.getData().get(adapter.getItemCount() - 1).setContent("更新用户：" + userUpdateCount + "条");
                                adapter.notifyItemChanged(adapter.getItemCount() - 1);
                            }
                            userUpdatetime = users.get(users.size() - 1).getUptime();

                            getUserInfo();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DeviceManage.getInstance().setOnline(false);
                        updateUserFinish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void updateUserFinish() {
        if (!LeniuUsbManger.getInstance().hasFaceCamera()) {
            gotoMain();
            return;
        }
        initFace();
    }

    private void initFace() {
        adapter.addData(new InitInfo(true, "人脸sdk初始化..."));
        FacePassManage3568.getInstance().init(new FacePassManage3568.InitListener() {
            @Override
            public void onFaceInitSuccess() {
                adapter.getData().get(adapter.getItemCount() - 1).setContent("人脸sdk初始化成功");
                adapter.notifyItemChanged(adapter.getItemCount() - 1);

                initFaceSdkFinish();


            }

            @Override
            public void onFaceInitFail(String msg) {
                adapter.getData().get(adapter.getItemCount() - 1).setSuccess(false);
                adapter.getData().get(adapter.getItemCount() - 1).setContent("人脸sdk初始化失败：" + msg);
                adapter.notifyItemChanged(adapter.getItemCount() - 1);
                initFaceSdkFinish();
            }
        });

//        gotoMain();

    }


    private int insertSuccessCount;
    private int insertFailCount;


    private void initFaceSdkFinish() {
        insertFeature();
    }


    private void insertFeature() {
        List<User> users = LeniuDB.getInstance(InitActivity.this).UserDao().queryNoAddFaceUserLimit(1);
        if (users != null && users.size() > 0) {

            if (!TextUtils.isEmpty(users.get(0).getCustPhotoUrl())) {
                String faceId = users.get(0).getFaceId();
                String url = users.get(0).getCustPhotoUrl();
                if (!users.get(0).getCustPhotoUrl().startsWith("http")) {
                    HttpConfig httpConfig = HttpConfigCache.get();
                    url = httpConfig.getAddr() + users.get(0).getCustPhotoUrl();
                }

                if (users.get(0).getCustState() == 1) {
                    //正常用户状态，需要入算法底库
                    RxYstHttp.get(url)
                            .asDownload("/sdcard/leniu/cache/face/" + faceId + ".jpg") //传入本地路径
                            .subscribe(new Observer<String>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@NonNull String fileName) {
                                    Bitmap bitmap = ImageUtil.getBitmap(fileName);

                                    //人员正常，加入底库人脸
                                    boolean success = FacePassManage3568.getInstance().insert(faceId, bitmap);
                                    Log.e(TAG, "插入特征值成功：" + users.get(0).getFaceId());
                                    if (success) {
                                        insertSuccessCount++;
                                    } else {
                                        insertFailCount++;
                                    }
                                    users.get(0).setAddFace(1);
                                    Long custId = LeniuDB.getInstance(InitActivity.this).UserDao().insert(users.get(0));
                                    Log.e(TAG, "数据库标记成功：custId:" + custId + " name:" + users.get(0).getCustName());

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.getData().get(adapter.getItemCount() - 1).setContent("更新人脸:" + insertSuccessCount + "条");
                                            adapter.notifyItemChanged(adapter.getItemCount() - 1);
                                        }
                                    });


                                    insertFeature();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    insetFeatureFinish();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });


                } else if (users.get(0).getCustState() == 2) {
                    //人员注销，删除底库人脸
                    Log.e(TAG, "删除注销人员：custId:" + users.get(0).getCustId() + " name:" + users.get(0).getCustName());
                    FacePassManage3568.getInstance().delete(faceId);
                    users.get(0).setAddFace(1);
                    Long custId = LeniuDB.getInstance(InitActivity.this).UserDao().insert(users.get(0));
                    Log.e(TAG, "数据库标记成功：custId:" + custId + " name:" + users.get(0).getCustName());
                    insertFeature();
                } else {
                    insertFeature();
                }


            }

        } else {
            insetFeatureFinish();
        }
    }


    private void insetFeatureFinish() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int nofaceCount = LeniuDB.getInstance(InitActivity.this).UserDao().queryNoFaceCount();
                adapter.addData(new InitInfo(true, "人脸入库成功：" + insertSuccessCount + "条"));
                adapter.addData(new InitInfo(true, "人脸入库失败：" + insertFailCount + "条"));
                adapter.addData(new InitInfo(true, "已入库：" + FacePassManage3568.getInstance().getFaceCount() + "，无照片信息：" + nofaceCount + "条"));
                gotoMain();
            }
        });
    }


    private void deviceInitTest() {

        Observable
                .concatArray(
                        TestObservable.testIp(),
                        TestObservable.testPort(),
                        TestObservable.testServer(),
                        TestObservable.testDeviceAdd(),
                        TestObservable.getDeviceInfo(),
                        TestObservable.getBaseInfo()
                )
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLife.toMain(this))
                .subscribe(new Observer<InitInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(InitInfo initInfo) {
                        Log.e(TAG, "onNext:" + new Gson().toJson(initInfo));
                        adapter.addData(initInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError:" + e.getMessage());
//                        adapter.addData(new InitInfo(false, "服务器连通性，异常 " + e.getMessage()));
                        if (e instanceof HttpStatusCodeException) {
                            HttpStatusCodeException exception = (HttpStatusCodeException) e;
//                            adapter.addData(new InitInfo(false, exception.getStatusCode() + ":" + exception.getRequestUrl()));
                            adapter.addData(new InitInfo(false, exception.toString()));
                        } else {
                            adapter.addData(new InitInfo(false, e.getMessage()));
                        }

                        getUserInfo();
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
//                        gotoMain();
                        getUserInfo();
                    }
                });


    }


    private void gotoMain() {
        tvInitEnd.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceManage.getInstance().setOnline(NetworkUtil.isConnected());
                startActivity(HomeActivity.class);
                finish();
            }
        }, 1000);
    }

    private void showDeviceNetworkInfo() {
        tvNetWorkType.setText("以太网");
        tvIp.setText(TestManage.getInstance().getDeviceIp());
        tvMask.setText(TestManage.getInstance().getDeviceMask());
        tvGateway.setText(TestManage.getInstance().getDeviceGateway());
        tvDns0.setText(TestManage.getInstance().getDeviceDns(1));
        tvDns1.setText(TestManage.getInstance().getDeviceDns(2));
    }


    @Override
    protected void onRightClick() {
        super.onRightClick();
        startActivity(SettingActivity.class);
    }

    @Override
    public void doSDCardPermission() {
        super.doSDCardPermission();
        L.init();
        CacheDoubleStaticUtils.getString("");
        HttpManager.init();

        //设置sourceType
        HttpConfig httpConfig = HttpConfigCache.get();
//        httpConfig.setHeaderSourceType(SourceType.CT.getCode()+"");
        httpConfig.setHeaderSourceType("64");
        HttpConfigCache.put(httpConfig);

        //读取设备key
        String key = AppUtil.getDeviceKey();
        tvKey.setText("key:" + key);

        tvSn.setText("SN:" + AppUtil.getSn());
//        int width = (int) getResources().getDimension(R.dimen.dp_300);
        int width = 300;
        Bitmap qrCodeBitmap = QRCodeCreater.createQRCode(
                width,
                width,
                AppUtil.getSn());
        if (qrCodeBitmap != null) {
            ivSn.setImageBitmap(qrCodeBitmap);
        }

        deviceInitTest();

    }


    @Override
    protected void initLisitener() {

    }


}