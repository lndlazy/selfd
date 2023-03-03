package net.xnzn.app.selfdevice.login;

import android.graphics.RectF;
import android.hardware.camera2.params.Face;
import android.os.SystemClock;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.NonNull;

import com.cs.camera.view.FaceView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.common.LeniuUsbManger;
import net.xnzn.app.selfdevice.lib_facepass_3568.FacePassManage3568;
import net.xnzn.app.selfdevice.ui.SelfCommonActivity;
import net.xnzn.lib_commin_ui.base.constant.Constant;
import net.xnzn.lib_reader.ReaderManage;

import java.util.ArrayList;

public class FaceLoginActivity extends SelfCommonActivity implements Camera2HelperFace.FaceDetectListener {

    private static final String TAG = "FaceLoginActivity";
    //    protected TextureView mTextureView;
//    protected FaceView faceView;
//    private Camera2HelperFace camera2HelperFace;
    private TextureView sfvIR, sfvRgb;

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

        requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
        if (LeniuUsbManger.getInstance().hasFaceCamera()) {
            initFace();
        }
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

        new Thread(new Runnable() {
            @Override
            public void run() {

                FacePassManage3568.getInstance().init(new FacePassManage3568.InitListener() {
                    @Override
                    public void onFaceInitSuccess() {
                        Log.d(TAG, "人脸sdk初始化成功");
                        startFace();
                    }

                    @Override
                    public void onFaceInitFail(String msg) {
                        Log.d(TAG, "人脸sdk初始化失败");

                    }
                });

            }
        }).start();

    }

    private void startFace() {

        FacePassManage3568.getInstance()
                .setRgb(sfvRgb)
                .setIR(sfvIR)
                .setCallback(new FacePassManage3568.Callback() {

                    @Override
                    public void onFaceVerifySuccess(String faceId, float searchScore, float livenessScore) {
//                        ToastUtil.showShort("id：" + faceId);
                        Log.d(TAG, "faceId++====>>" + faceId);
//                        request.setNuClearMode(2);
//                        request.setCustId(Long.parseLong(faceId.substring(12)));
//                        stopVerify();
//                        uploadData();

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

        SystemClock.sleep(3000);
        FacePassManage3568.getInstance().startVerify();
    }


}