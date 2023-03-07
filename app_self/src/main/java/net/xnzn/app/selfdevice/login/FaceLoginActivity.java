package net.xnzn.app.selfdevice.login;

import android.content.Context;
import android.graphics.RectF;
import android.hardware.camera2.params.Face;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.cs.camera.view.FaceView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.common.LeniuUsbManger;
import net.xnzn.app.selfdevice.lib_facepass_3568.FacePassManage3568;
import net.xnzn.app.selfdevice.login.bean.respo.YunUser;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_commin_ui.base.constant.Constant;
import net.xnzn.lib_reader.ReaderManage;

import java.util.ArrayList;

public class FaceLoginActivity extends SelfCommonActivity implements Camera2HelperFace.FaceDetectListener, LoginView {

    private static final String TAG = "FaceLoginActivity";
    //    protected TextureView mTextureView;
//    protected FaceView faceView;
//    private Camera2HelperFace camera2HelperFace;
    private TextureView sfvIR, sfvRgb;

    private LoginPresenter loginPresenter = new LoginPresenter(this);
    protected String nextPage;

    @Override
    protected void initView() {
        super.initView();
        sfvIR = findViewById(R.id.sfvIR);
        sfvRgb = findViewById(R.id.sfvRgb);
//        mTextureView = findViewById(R.id.textureView);
//        faceView = findViewById(R.id.faceView);
//        FaceDetector
    }

    @Override
    protected boolean showTitle() {
        return false;
    }

    @Override
    protected boolean showBar() {
        return false;
    }

    @Override
    protected int showView() {
        return R.layout.activity_face_login;
    }

    @Override
    protected void initData() {
        super.initData();
        nextPage = getIntent().getStringExtra("nextPage");

        requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);

        if (LeniuUsbManger.getInstance().hasFaceCamera()) {
            initFace();
            Log.d(TAG, "hasFaceCamera==>");
        }
        startFace();

//        camera2HelperFace = new Camera2HelperFace(this, mTextureView);
//        camera2HelperFace.setFaceDetectListener(this);
    }

    @Override
    protected void initLisitener() {

    }

    @Override
    public void onFaceDetect(@NonNull Face[] faces, @NonNull ArrayList<RectF> facesRect) {
//        faceView.setFaces(facesRect);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        try {
//            if (camera2HelperFace != null) {
//                camera2HelperFace.releaseCamera();
//                camera2HelperFace.releaseThread();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        FacePassManage3568.getInstance().stopVerify();
        FacePassManage3568.getInstance().release();

    }


    private void initFace() {

        FacePassManage3568.getInstance().init(new FacePassManage3568.InitListener() {
            @Override
            public void onFaceInitSuccess() {
                Log.d(TAG, "人脸sdk初始化成功");
            }

            @Override
            public void onFaceInitFail(String msg) {
                Log.d(TAG, "人脸sdk初始化失败");

            }
        });

    }

    private int current_status;

    private static final int STATUS_LOGINING = 1; //正在登录中
    private static final int STATUS_LOGIN_SUCCESS = 2; //登录成功
    private static final int STATUS_WAIT_LOGIN = 0;//可以登录

    private void startFace() {

        FacePassManage3568.getInstance()
                .setRgb(sfvRgb)
                .setIR(sfvIR)
                .setCallback(new FacePassManage3568.Callback() {

                    @Override
                    public void onFaceVerifySuccess(String faceId, float searchScore, float livenessScore) {
//                        ToastUtil.showShort("id：" + faceId);
                        Log.d(TAG, "faceId++====>>success:" + faceId);
                        login(faceId);

                    }

                    @Override
                    public void onFaceVerifyFail(int result, String msg, float searchScore, float livenessScore) {
//                        if (presentation != null) {
//                            presentation.showSearchScore("识别分：" + searchScore);
//                        }
                        Log.d(TAG, "faceId++====>>onFaceVerifyFail:" + msg);

                    }
                })
                .startWork();

//        SystemClock.sleep(2000);

        FacePassManage3568.getInstance().startVerify();

    }

    private void login(String faceId) {

        if (TextUtils.isEmpty(faceId))
            return;

        if (current_status == STATUS_LOGINING || current_status == STATUS_LOGIN_SUCCESS)
            return;

        faceId = faceId.replace("xnznkjxnznkj", "");

        current_status = STATUS_LOGINING;

        loginPresenter.loginIn(null, faceId, nextPage);
    }


    @Override
    public void loginSuccess(YunUser user) {
        current_status = STATUS_LOGIN_SUCCESS;
        finish();
    }

    @Override
    public void loginFail(String msg) {

        current_status = STATUS_WAIT_LOGIN;

    }


}