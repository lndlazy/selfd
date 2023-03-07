package net.xnzn.app.selfdevice.lib_facepass_3568;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.TextureView;

import net.xnzn.app.selfdevice.R;
import net.xnzn.app.selfdevice.common.Camera2Manager;
import net.xnzn.app.selfdevice.common.CameraPreviewData;
import net.xnzn.app.selfdevice.common.ComplexFrameHelper;
import net.xnzn.app.selfdevice.common.LeniuUsbManger;
import net.xnzn.app.selfdevice.config.DeviceConfig;
import net.xnzn.app.selfdevice.config.DeviceConfigCache;
import net.xnzn.app.selfdevice.utils.FileUtil;
import net.xnzn.lib_utils.AppUtil;
import net.xnzn.lib_utils.EncodeUtil;
import net.xnzn.lib_utils.ImageUtil;
import net.xnzn.lib_utils.ResourceUtil;
import net.xnzn.lib_utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import mcv.facepass.FacePassException;
import mcv.facepass.FacePassHandler;
import mcv.facepass.auth.AuthApi.AuthApi;
import mcv.facepass.auth.AuthApi.AuthApplyResponse;
import mcv.facepass.types.FacePassConfig;
import mcv.facepass.types.FacePassExtractFeatureResult;
import mcv.facepass.types.FacePassFeatureAppendInfo;
import mcv.facepass.types.FacePassImage;
import mcv.facepass.types.FacePassImageType;
import mcv.facepass.types.FacePassLivenessMode;
import mcv.facepass.types.FacePassLivenessResult;
import mcv.facepass.types.FacePassModel;
import mcv.facepass.types.FacePassPose;
import mcv.facepass.types.FacePassRCAttribute;
import mcv.facepass.types.FacePassRecogMode;
import mcv.facepass.types.FacePassRecognitionResult;
import mcv.facepass.types.FacePassTrackOptions;
import mcv.facepass.types.FacePassTrackResult;
import mcv.facepass.types.FacePassTrackedFace;

import static java.lang.Thread.sleep;

public class FacePassManage3568 {


    private static final int FACE_MIN_THRESHOLD = 150;
    private static final float SEARCH_SCORE = 80;
    private static FacePassManage3568 instance = new FacePassManage3568();
    protected int LIVE_RET_CODE = -1;//活体检测返回码 FacePassRetCode类 0是成功

    public static FacePassManage3568 getInstance() {
        return instance;
    }

    private InitListener initListener;
    private AuthListener authListener;

    //正式
    private static final String TAG = FacePassManage3568.class.getSimpleName();

    private static final String GROUP_NAME = "group1";

    private static ArrayBlockingQueue<RecognizeData> mDetectResultQueue = new ArrayBlockingQueue<>(5);//人脸识别队列

    private Context mContext;
    private FacePassHandler mFacePassHandler;
    private static boolean initFaceHandlerFinish = false;//FacePassHandler是否创建完成
    private float searchThreshold = 80;
    private int faceMinThreshold = FACE_MIN_THRESHOLD;


    private static boolean isFaceDetectThreadFinish = false;//人脸识别线程是否结果
    private static boolean isFaceRecognizeThreadFinish = false;//人脸检测线程是否结果

    private static final int cameraWidth = 640;//摄像头预览宽度
    private static final int cameraHeight = 480;//摄像头预览高度
    private static final String DEBUG_TAG = "DEBUG_TAG";
    private int mAlgorithmRotation = 0;


    private Camera2Manager rgbCameraMange;
    private Camera2Manager irCameraMange;
    private Callback callback;
    private TextureView sfvRgb, sfvIR;

    private boolean startVerify = false;

    private Handler handler = new Handler(Looper.getMainLooper());

    public static final String CERT_PATH = "/Download/leniu.cert";


    public void startVerify() {

        if (mContext == null) {
            return;
        }


        this.startVerify = true;
        Log.i(TAG, "开始检测人脸...");
        if (mFacePassHandler != null) {
            mDetectResultQueue.clear();
            mFacePassHandler.reset();
        }
    }

    public void stopVerify() {
        if (mContext == null) {
            return;
        }
        this.startVerify = false;
        mDetectResultQueue.clear();
        Log.i(TAG, "停止检测人脸...");
    }


    public FacePassManage3568() {
        mContext = AppUtil.getApplication();
    }

    public boolean insert(String faceId, Bitmap bitmap) {

        if (mFacePassHandler == null) {
            return false;
        }
        try {
            FacePassFeatureAppendInfo info = new FacePassFeatureAppendInfo();
            info.faceToken = faceId;
            FacePassExtractFeatureResult featureResult = mFacePassHandler.extractFeature(bitmap, true, 0);
            boolean unBindSuccess = mFacePassHandler.unBindGroup(GROUP_NAME, info.faceToken.getBytes());
            boolean deleteSuccess = mFacePassHandler.deleteFace(info.faceToken.getBytes());
            String result = mFacePassHandler.insertFeature(featureResult.featureData, info);
            boolean bindResult = mFacePassHandler.bindGroup(GROUP_NAME, result.getBytes());
            if (bindResult) {
                return true;
            }
            return false;

        } catch (FacePassException e) {
            e.printStackTrace();
            Log.e(TAG, "test face error:" + e.getMessage());
        }
        return false;

    }

    public void delete(String faceId) {
        if (mFacePassHandler == null) {
            return;
        }
        try {
            boolean unBindSuccess = mFacePassHandler.unBindGroup(GROUP_NAME, faceId.getBytes());
            boolean deleteSuccess = mFacePassHandler.deleteFace(faceId.getBytes());
        } catch (FacePassException e) {
            e.printStackTrace();
            Log.e(TAG, "test face error:" + e.getMessage());
        }

    }

    public int getFaceCount() {
        if (mFacePassHandler != null) {
            try {
                return mFacePassHandler.getLocalGroupFaceNum(GROUP_NAME);
            } catch (FacePassException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public interface InitListener {
        void onFaceInitSuccess();

        void onFaceInitFail(String msg);
    }


    public interface AuthListener {
        void onFaceAuthSuccess();

        void onFaceAuthFail(String msg);
    }

    public void auth(AuthListener listener) {
        this.authListener = listener;
        if (FacePassHandler.isAuthorized()) {
            Log.d(TAG, "FacePassHandler.isAuthorized() 已认证");
            authListener.onFaceAuthSuccess();
            return;
        }
        try {
            String cert = FileUtil.readExternal(CERT_PATH).trim();

            Log.d(TAG, "证书地址：" + cert);
            if (TextUtils.isEmpty(cert)) {
                Log.d(TAG, "授权证书不存在");
                authListener.onFaceAuthFail("授权证书不存在");
                return;
            }
            boolean prepareSucc = FacePassHandler.authPrepare(AppUtil.getApplication());
            Log.d(TAG, "证书是否认证成功::" + prepareSucc);
            FacePassHandler.authDevice(AppUtil.getApplication(), cert, "", new AuthApi.AuthDeviceCallBack() {
                @Override
                public void GetAuthDeviceResult(AuthApplyResponse result) {

                    if (result.errorCode == 0) {
                        authListener.onFaceAuthSuccess();
                        return;
                    }
                    authListener.onFaceAuthFail("旷视授权失败 " +
                            "errorCode:" + result.errorCode +
                            " errorMessage:" + result.errorMessage +
                            " count:" + result.count);

                    Log.i(TAG,
                            "旷视授权结果 " +
                                    "errorCode:" + result.errorCode +
                                    " errorMessage:" + result.errorMessage +
                                    " count:" + result.count);
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
            authListener.onFaceAuthFail("旷视授权异常 " + e.getMessage());
        }

    }


    public void init(InitListener listener) {
        this.initListener = listener;
        //初始化sdk
        FacePassHandler.initSDK(AppUtil.getApplication());
        new Thread(new Runnable() {
            @Override
            public void run() {
                auth(new AuthListener() {
                    @Override
                    public void onFaceAuthSuccess() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                init();
                            }
                        });
                    }

                    @Override
                    public void onFaceAuthFail(String msg) {
                        ToastUtil.showShort("授权失败：" + msg);
                    }
                });
            }
        }).start();

    }

    private static volatile boolean isReleased = false;


    public void init() {
        DeviceConfig config = DeviceConfigCache.get();
        searchThreshold = config.getFaceScore();
//        faceMinThreshold = config.getFaceSize();
        faceMinThreshold = FACE_MIN_THRESHOLD;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!initFaceHandlerFinish && FacePassHandler.isAuthorized() && !isReleased) {
                    Log.i(TAG, "start to build FacePassHandler");
                    FacePassConfig config;
                    try {
                        /* 填入所需要的配置   单目活体 CPU 配置 */
                        config = new FacePassConfig();
                        //用于人脸红外活体检测的模型，通过FacePassModel初始化成实例，需要同时置rgbIrLivenessEnabled为true
                        config.rgbIrLivenessModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_liveness_A));
                        config.LivenessModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_livenessrgb_A));
                        // 用于人脸识别的模型，通过FacePassModel初始化成实例
                        config.searchModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_feature_Ari));
                        //用于人脸角度、人脸模糊度质量判断的模型，通过FacePassModel初始化成实例
                        config.poseBlurModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_poseblur_A));
                        //用于获取人脸关键点的模型，通过FacePassModel初始化成实例
                        config.postFilterModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_postfilter_A));
                        //用于检人脸的属性模型，通过FacePassModel初始化成实例，需要设置rcAttributeEnabled=true
                        config.rcAttributeModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_rc_attribute_A));
                        //用于人脸框检测的模型，通过FacePassModel初始化成实例
                        config.detectModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_rk3568_det_A_det));
                        config.occlusionFilterModel = FacePassModel.initModel(mContext.getAssets(), mContext.getString(R.string.mcv_occlusion_B));

                        /* 送识别阈值参数 */
                        config.searchThreshold = searchThreshold;
                        config.faceMinThreshold = FACE_MIN_THRESHOLD;

//                        config.livenessThreshold = 85f;//活体检测阈值
                        config.livenessThreshold = 70f;//活体检测阈值
                        config.poseThreshold = new FacePassPose(45f, 45f, 45);//人脸角度阈值。
//                        config.edgefacecompThreshold = 0.75f;//人脸边缘完整度阈值
                        config.blurThreshold = 0.8f;//人脸模糊度阈值。
                        config.lowBrightnessThreshold = 30f;
                        config.highBrightnessThreshold = 210f;
                        config.brightnessSTDThreshold = 80f;//人脸照度标准差阈值。照度标准差是用来衡量人脸光照对比度的参数，分布在[0, 255]，越大对比越强烈。
                        config.brightnessSTDThresholdLow = 10f;//人脸照度标准差阈值。

                        config.rgbIrLivenessEnabled = true;//红外活体检测，需有iniRGB+IR双目相机。
                        config.rcAttributeEnabled = true;//属性模型开关，开启该功能后，feedFrame/feedFrameRGBIR接口会返回人脸的属性信息。

                        /* 其他设置 */
                        config.maxFaceEnabled = true;//最大人脸使能开关，默认关闭。true，使能最大人脸，如果同一帧数据中，检测到多个人脸框，只有最大的人脸才会送去识别。
                        //识别失败时的重试次数
                        config.retryCount = 99999;
                        //用于存储底库的文件根目录路径
                        config.fileRootPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//                        config.fileRootPath = FILE_ROOT_PATH;
//                                    getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

                        /* 创建SDK实例 */
                        mFacePassHandler = new FacePassHandler();
                        int code = FacePassHandler.initHandle(config);
                        Log.d("mmm", "初始化 -> " + (code == 0 ? "成功" : ("错误  code = " + code)));

                        /*设置双目图片配准参数, 偏移量系数需由客户根据自己的设备测试得到.*/
//                        mFacePassHandler.setIRConfig(1.0, 0.0, 1.0, 0.0, 0.3);
                        mFacePassHandler.setIRConfig(0.9974522, 22.739716, 0.99601936, -51.801147, 0);


                        /* 入库阈值参数 */
                        FacePassConfig addFaceConfig = mFacePassHandler.getAddFaceConfig();
                        addFaceConfig.poseThreshold.pitch = 35f;
                        addFaceConfig.poseThreshold.roll = 35f;
                        addFaceConfig.poseThreshold.yaw = 35f;
                        addFaceConfig.blurThreshold = 0.7f;
                        addFaceConfig.lowBrightnessThreshold = 70f;//人脸平均照度阈值。平均照度是人脸除去毛发和装饰物等杂物后，一个平均的亮度，数值越大越亮，不超过255
                        addFaceConfig.highBrightnessThreshold = 220f;//人脸平均照度阈值。
                        addFaceConfig.brightnessSTDThreshold = 60f;
//                        addFaceConfig.faceMinThreshold = 40;//最小人脸尺寸。表示允许进行识别的最小人脸尺寸。[0, 512]，
                        addFaceConfig.faceMinThreshold = FACE_MIN_THRESHOLD;//最小人脸尺寸。表示允许进行识别的最小人脸尺寸。[0, 512]，
                        mFacePassHandler.setAddFaceConfig(addFaceConfig);
                        /* 填入所需要的配置 */

                        //FacePassHandler初始化成功，检查是否创建group分组052
                        initFaceHandlerFinish = true;
                        Log.e(TAG, "facehandler init success");
                        if (checkGroup()) {
                            //sdk初始化之后，插入未入库的特征值
//                            updateFacePassFeature();
                        }

                        initSuccess();


                    } catch (FacePassException e) {
                        e.printStackTrace();
                        Log.e(TAG, "FacePassHandler init error" + e.getMessage());
                    }
                }

                try {
                    /* 如果SDK初始化未完成则需等待 */
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i(TAG, "旷视人脸sdk初始化失败：" + e.getMessage());
                }

            }


        }).start();


    }


    private void testFace() {
        String faceId = "0012345678912345678921234";

        //测试人脸数据

        //获取face.txt
        String base64Str = ResourceUtil.readAssets2String("face.txt");

        Bitmap bm = ImageUtil.bytes2Bitmap(EncodeUtil.base64Decode(base64Str));

        try {
            FacePassFeatureAppendInfo info = new FacePassFeatureAppendInfo();
            info.faceToken = faceId;

            FacePassExtractFeatureResult featureResult = mFacePassHandler.extractFeature(bm, true, 0);

            boolean unBindSuccess = mFacePassHandler.unBindGroup(GROUP_NAME, info.faceToken.getBytes());
            boolean deleteSuccess = mFacePassHandler.deleteFace(info.faceToken.getBytes());

            String result = mFacePassHandler.insertFeature(featureResult.featureData, info);

            boolean bindResult = mFacePassHandler.bindGroup(GROUP_NAME, result.getBytes());

        } catch (FacePassException e) {
            e.printStackTrace();
            Log.e(TAG, "test face error:" + e.getMessage());
        }
    }


    private void initSuccess() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                initListener.onFaceInitSuccess();
            }
        });
//        testFace();

    }

    public void startWork() {

        resetInfo();

        //开启识别线程和检测线程
        faceDetectThread();
        faceRecognizeThread();
        //打开rgb和ir摄像头
        openCamera();
    }

    private void resetInfo() {
        isReleased = false;
//        initFaceHandlerFinish = false;
        isFaceRecognizeThreadFinish = false;
        isFaceDetectThreadFinish = false;
        LIVE_RET_CODE = -1;
    }

    private void openCamera() {
        if (LeniuUsbManger.getInstance().hasHRgbCamera()) {
            mAlgorithmRotation = 180;
//            mAlgorithmRotation = 0;
//            Log.d(TAG, "hasHRgbCamera====>>>>");
        }

        rgbCameraMange = new Camera2Manager()
                .setCameraId(1)
                .setPreviewSize(new Size(cameraWidth, cameraHeight))
                .setPreviewSurface(sfvRgb)
                .setCallback(new Camera2Manager.Callback() {
                    @Override
                    public void onFramCallback(byte[] nv21, int width, int height, int previewRotation, boolean mirror) {
//                        Log.e("数据", "0:" + System.currentTimeMillis() + ":" + nv21.length);
                        if (startVerify) {
                            ComplexFrameHelper.addRgbFrame(new CameraPreviewData(nv21, width, height, mAlgorithmRotation, false));
                        }
                    }
                });

        irCameraMange = new Camera2Manager()
                .setCameraId(0)
                .setPreviewSize(new Size(cameraWidth, cameraHeight))
                .setPreviewSurface(sfvIR)
                .setCallback(new Camera2Manager.Callback() {
                    @Override
                    public void onFramCallback(byte[] nv21, int width, int height, int previewRotation, boolean mirror) {
//                        Log.e("数据", "1:" + System.currentTimeMillis() + ":" + nv21.length);
                        if (startVerify) {
                            ComplexFrameHelper.addIRFrame(new CameraPreviewData(nv21, width, height, mAlgorithmRotation, false));
                        }
                    }
                });

    }


    /**
     * 检查group是否创建，没有创建立即创建
     */
    private boolean checkGroup() {

        try {
            String[] localGroups = mFacePassHandler.getLocalGroups();
            for (String group : localGroups) {
                if (GROUP_NAME.equals(group)) {
                    return true;
                }
            }
            boolean isGroupCreateSuccess = mFacePassHandler.createLocalGroup(GROUP_NAME);
            if (isGroupCreateSuccess) {
                return true;
            }
        } catch (FacePassException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 人脸检测线程
     */
    public void faceDetectThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isFaceDetectThreadFinish) {

                    Log.d(TAG, "====人脸检测====" + Thread.currentThread().getName());

                    try {
                        sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /* 将相机预览帧转成SDK算法所需帧的格式 FacePassImage */
                    Pair<CameraPreviewData, CameraPreviewData> framePair;
                    try {
                        framePair = ComplexFrameHelper.takeComplexFrame();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("FacePassDemo", e.getMessage());
                        continue;
                    }
                    if (mFacePassHandler == null) {
                        Log.d(DEBUG_TAG, " mFacePassHandler == null !");
                        continue;
                    }


                    FacePassImage imageRGB;
                    FacePassImage imageIR;
                    try {

                        imageRGB = new FacePassImage(framePair.first.nv21Data,
                                framePair.first.width, framePair.first.height, framePair.first.rotation, FacePassImageType.NV21);
                        imageIR = new FacePassImage(framePair.second.nv21Data,
                                framePair.second.width, framePair.second.height, framePair.second.rotation, FacePassImageType.NV21);

                    } catch (FacePassException e) {
                        e.printStackTrace();
                        continue;
                    }

                    /* 将每一帧FacePassImage 送入SDK算法， 并得到返回结果 */

                    try {
                        FacePassTrackResult detectionResult = mFacePassHandler.feedFrameRGBIR(imageRGB, imageIR);

//                        detectionResult.trackedFaces[0].quality.

                        if (detectionResult == null || detectionResult.trackedFaces.length == 0) {
                            /* 当前帧没有检出人脸 */

                        } else {

//                        Log.d(TAG, "检测出人脸");
                            /* 将识别到的人脸在预览界面中圈出，并在上方显示人脸位置及角度信息 */
                            final FacePassTrackedFace[] bufferFaceList = detectionResult.trackedFaces;

//                            detectionResult.message

                            if (isOcc(detectionResult)) {
//                            Log.d(TAG, "isOcc===>" + true);
                                continue;
                            }

//                        Log.d(TAG, "detectionResult.message.length 个数::" + detectionResult.message.length);
                            if (detectionResult.message.length != 0) {

                                FacePassLivenessResult[] facePassLivenessResults = mFacePassHandler.livenessClassify(detectionResult.message, FacePassLivenessMode.FP_REG_MODE_LIVENESS);
//                                if (facePassLivenessResults != null) {
//                                    Log.d(TAG, "facePassLivenessResults长度:" + facePassLivenessResults.length);
//
//                                    for (FacePassLivenessResult fa : facePassLivenessResults)
//                                        Log.d(TAG, "fa::" + fa.livenessScore + "," + fa.livenessThreshold + "," + fa.livenessState + "," + fa.retCode);
//
//                                }

                                /*送识别的人脸框的属性信息*/
                                FacePassTrackOptions[] trackOpts = new FacePassTrackOptions[detectionResult.images.length];
                                for (int i = 0; i < detectionResult.images.length; ++i) {
//                                    mFacePassHandler.feedFrame(detectionResult.images[i]);
//                                    Log.d(TAG, "旋转角度:" + detectionResult.images[i].facePassImageRotation);
//                                    Log.d(TAG, "宽高:" + detectionResult.images[i].width + "," + detectionResult.images[i].height);
//                                    Log.d(TAG, "位置:" + detectionResult.images[i].rect.top + "," + detectionResult.images[i].rect.bottom
//                                            + "," + detectionResult.images[i].rect.left + "," + detectionResult.images[i].rect.right);
//                                    Log.d(TAG, "是否被遮挡:" + detectionResult.images[i].lmkoccsta.eyeocc + "," + detectionResult.images[i].lmkoccsta.mouthocc
//                                            + "," + detectionResult.images[i].lmkoccsta.noseocc + "," + detectionResult.images[i].lmkoccsta.valid);
//                                    Log.d(TAG, "人脸质量:" + detectionResult.images[i].facePassQuality.flag + "," + detectionResult.images[i].facePassQuality.blur
//                                            + "," + detectionResult.images[i].facePassQuality.facePassQualityCheck);

                                    for (FacePassLivenessResult fa : facePassLivenessResults) {
                                        Log.d(TAG, "fa::" + fa.livenessScore + "," + fa.livenessThreshold + "," + fa.livenessState + "," + fa.retCode);
                                        LIVE_RET_CODE = fa.retCode;
                                    }


                                    if (detectionResult.images[i].rcAttr.respiratorType != FacePassRCAttribute.FacePassRespiratorType.NO_RESPIRATOR) {
                                        float searchThreshold = 60f;
                                        float livenessThreshold = 75f; // -1.0f will not change the liveness threshold
                                        trackOpts[i] = new FacePassTrackOptions(detectionResult.images[i].trackId, searchThreshold, livenessThreshold);
                                    } else {
                                        trackOpts[i] = new FacePassTrackOptions(detectionResult.images[i].trackId, -1f, -1f);
//                                        Log.d(TAG, "====> == NO_RESPIRATOR");
                                    }

//                                    Log.d(TAG, "searchThreshold==>" + trackOpts[i].searchThreshold + ",livenessThreshold:" + trackOpts[i].livenessThreshold);
                                    Log.d(TAG, "检测线程trackId:" + trackOpts[i].trackId + ",liveId:" + trackOpts[i].livenessThreshold + ",searchId:" + trackOpts[i].searchThreshold);

                                    Log.d(DEBUG_TAG, String.format("rc attribute in FacePassImage, hairType: 0x%x beardType: 0x%x hatType: 0x%x respiratorType: 0x%x glassesType: 0x%x skinColorType: 0x%x",
                                            detectionResult.images[i].rcAttr.hairType.ordinal(),
                                            detectionResult.images[i].rcAttr.beardType.ordinal(),
                                            detectionResult.images[i].rcAttr.hatType.ordinal(),
                                            detectionResult.images[i].rcAttr.respiratorType.ordinal(),
                                            detectionResult.images[i].rcAttr.glassesType.ordinal(),
                                            detectionResult.images[i].rcAttr.skinColorType.ordinal()));
                                }
                                Log.d(DEBUG_TAG, "mRecognizeDataQueue.offer(mRecData);");
                                RecognizeData mRecData = new RecognizeData(detectionResult.message, trackOpts);

                                mDetectResultQueue.offer(mRecData);
                            }

                        }
                    } catch (FacePassException e) {
                        e.printStackTrace();
                    }
                }

            }

            private boolean isOcc(FacePassTrackResult detectionResult) {
                for (int i = 0; i < detectionResult.trackedFaces.length; i++) {
                    if (detectionResult.trackedFaces[i].rcAttr.respiratorType == FacePassRCAttribute.FacePassRespiratorType.COVER_MOUTH_NOSE
                            || detectionResult.trackedFaces[i].rcAttr.respiratorType == FacePassRCAttribute.FacePassRespiratorType.COVER_MOUTH) {
                        //如果发现遮挡，过滤本次检测结果

//                        Log.d(DEBUG_TAG, "有口罩  " + new Gson().toJson(detectionResult.trackedFaces[i].rcAttr));
                        mFacePassHandler.reset();
                        return true;
                    }
                }
                return false;
            }
        }).start();

    }

    private static final String FACE_FIRST_DATA = "QUFFQ0F3UUZCZ2NJQ1FvTERBME9ENVhReVNHLUJkaERNclIzXzI3bXpyamJ4SGFoU09UMFI1UUVrY3c4NzBMR0lBMHplS2txOHB0dDQtNTIxS25yQmNjb3lDdzFZMEdBSHA3ek9obzdIRGowdW81dFVuUF83TkpVazhINUtHRXVHSDVqS05jdE1KdjZEZmVzSFVpWUk5YWNkNWFpNDBqMXFIdGZGV3Z5ZnNuQW85MjRfZjgwcHZsLWxzVkVaZWR6UXZKSVFyeWVkV1pGU3JTdVl5bjlCWnVGbU1HTW9OeVZmUWJSUXMzaGYzWXRVeTBJTktMZnVTV0RNOGRzNFk2NnNqR1RUc0M3OE5uQnMwOU42ZGxvTnRlOUMxWnB3YVNmTEF1VnZmWWRaUFhlWS1BNmxKMFFLWkloQS1mSDF6eGxUaU84Vm96anFIWWh3ZzJrZWtsV0ZHLTJzMmt4eV9tWDV5V0JWZnBXSFE1N0FKVmQxcHNaUllqNXFkbU9sM0Q3OUFDV1VSU0xOeUxqcTN0MG1Xb2hScnF6UDVRcVdtM0J1VGhDQ3JzSjlkWDA1bldBdmF5SjM4cFZkQTJsdkU4ZTUwMHBxd3dXdWNyS3BHbk8tZkFyRVZKZ1lLNGtSSG4wcW5UVHJoaFp2bWpKSlpmV01qdldwSWxIX0swNkRLQ2wxdk81RW1wbXR1bXpWS3NsQm5KNnN6dGlkU0pYLTJsMEJPUFRhM3pfZllSREF4ZThkeXVRVy1YRndFd2RNU2JBM1IyUWFsVjllYmtHWW01MlAzWXlkc2xOZDg3bWtDMEEyMkJHTVd6OFFXczFwQ1F4RXRYQW1ZWVJkUzgzbVRabU5TZE1uODN6OFZoLXFqSm9JdktxWDZMZFRBelZrWWFBNF9nRy1GdW92S0RxX1B5bGhMRWFocUEteEg4ZER4NXE0d29ja0wwbldQb2lQaUVGUGpBdkREUWFBVUFKY3NTMWpqMXl5QU1DdVo4OGVnVXdWdUxhenR5dXZsWGlqeVRQOGJaOUpoSUNJYjNjWHBBN2dScHpZWG4zbDYzWm5XeFo1cE0tNFZVZFlidlNteVJ4TWVrVVZqSFhmYUZFOUF1aklpYXE0TTN2enB3MkY2VDF4WVJCamZYbURPckVrWldTYjJMN1ZKMUxxeEd1b0tLN2pMSC1FSkVkcHdLWVZmbThPZ1Z1M2dPWXh5OGlUMHZkT0VkRThHbDQwTGtVX3h6UlJVYXpUcFR1Vno3STV4WC00bF91N2FDU0dKOTU5X2ZfUGMyWkRXQW90cTEwUmhudjlVTlV5UDZybXRteFNxSW1DeU0zZE9adW5NNGczdGZJckZQUU1SSFZxaVVMU1hvcTFWM2k0am1ETFRaWWRpS1JLby1BUkNEazhhR2MyVXIzWFZnS3JsV3ZfYzlLTWViNzR5cjdvVTVrREpMTUhObVJnV1A3MHhIXzIxakJYVmhmTDVNTElDeVdScTkzbldfUTlNdzJUMGlpTmJCMkNPTjFUXzQzLWFLa0lrUEozY2s1VVotSndTUnBhTjZ6Q01MT2VIX1M1dGV2VEFWcVhFYm1CRThtUUw0enYtRDdaWWpQcEFjVG9CY3JMMlBEdWR3R2c4Z3dEYjRMWkdITG12cVFNcmpfYzdVRG42T0NhV1c4Nlo0M0tqNU9ocllpSXRjRHpaU3ctcWdLVlB5eGxzbzhkbWhBY1ZKY1ZuZGJoa04xUXJwS2hnazFEMXdkWmNLS0dDeVczcmxUYVpZcHYzTXhxeDNqQTdnR2FkSXhLNm5SeXZPTzRmTmQ1SG5rSjk5a1NPb2VBTjlwRnpNLV9zTmY0enFsWlY3QWxfY2loQTREWFVaUk4xSlRacm1SdWI4RlROS1NCdUwtc2NUNnZIV20yUG82amVsSW9idndGZ1Q1NlhobmoyZndvZHVsVGJwZE9MdWg2dFVJNTJHWXV5Y1RhVjh1b002cnZYeklKZTAzTU03eUhPTTFqUERSQlVhY0JZZ3VOSTN0TUZWT0JiTURpRDFWX2lfZ0wyVUxxd0t4LVJXSUk1dVlEZ0VISVQzMkJpUFd4MjF3a3JpOUY5cEhHdTF1ZmdVOFd6NmJDN28wcFBLUkNNWEFMNjVQdXRjUm9MNEg4YTZnbjZkY0ZpNzlmR2xmWXQwT0E2YUQ4ZE16MVpHUWR6QUZIR0U3QlF1YXM5bUwzbjJXRnJDVExMMk1HdGdDMVZUdlptcFJlbnZlaXBRbUtQLVkwV1lGdkJzcEYybzlnd3dqdEJHZXpRemtLWVNjdFV5Vk5xMGpoR3N4X0daTWZId2dSQmN5bFpFeFhlb2xkWnhWdGJicXNmbncwNThkaEtfTlRERy1kVUotN2c4dkFic3JUd1NWVl9LOE8tM3JuMzh5cHo3Z0NrbVV0X1BJUFNmb1RDZjVBUDNUNVk0cnQ4Vy02MTNnVDcxd3JZYVN5TVZ4cG5XZExnM0dySjdTclJfdUw1WEE4YUhINEpWWVJUSEQ5S19lVl95aURIR19URGdKTlBUeGExSlpHdVYtdUxEX3FtVENBcy15ckdSaUNXMDB2aDVYcDNpZmhqTURLcWdsd0ItdU1NdHJPN3g3WENabWVGZ2hzUkJlSkpUSHR0ZVU0MXZqRkhqaUExZG56QkF4bTliNF8zYjdPS00wdGRaQ0dqbTdkTmZLejIxbE5EQnRpd1ZBbTI4bDU4cmdYX0otTFFxOFZJajhzOWdjVHdick9EZjdmelFNZWR4bTV5aDRQS2NzdnV1T2VJQnZlRGZldUQzeFppRTRvd2FpQkdfZms5SjlkcGcwSU9sbEl5cS1ScndKZVFPS081T2J0MFFRWHdOUDh5R1JHdk9FREpxc2xza0FjVjdkTWFkQWk0ZUtyUHdkRTJxd0FpUllsT1NldG0zZ050TEdncGlkREFFNlVDamlPbmF1eXNqazJDaDZVUWI3dU5yYzA0ZDNrMUZHU21SeHhCMHlPUlM3UmRoMG83NnhERWY1ZVV1b2U1bWo0T3NLdjFfRjRXOExIbUFIUkE2bGpJUnZhNmxvRGpwOUFMLXhIZ1l1X1JJVWtfdnRuXzBReHFLTHRKemZpWTZQUFRBUlBocWw2UXRWVEdiZzBrZTlWRmJrLXYxbEFuVFFpU1VJVHJYc2hDd3N0SmY0RmllTkxJd0x5TFNKdFpBT3hFeTBEaVVDVWxDVlhzVkRJRTlhX1djbkRUaUt2RnE0ZXR0eEZELXhwdVlyQlAxTlVSQzlfdU5veGhPTTJJVXNDUE40cjFFREQzMHI3X0ZHTnRrRDJXSkNMMmlIX3dvN0ZFODl2bFZCMmExRENLVkg5WklpUkZlNjhpRUllRHRXcHdBeGNVb0ZJR0hmbl9IU2g5YU5xV1B6eG91d0RLSmVVdlRRWTBnb3R2OHIycGRyMzhBRnE5bGxFQ1E2ZWhuYjh2YzBub1pMTkVHWWJ2MXFMWGU3Z2M3Q2tRLUVNYmZmaU1iZnl6MGg5ekNwRDRtWEVycGcxNE9XMlZES0Y3bW9UYkxNU1NUeEdYaDVEY0UyNnAycGtJd0xqRGdTOE01Ri1hZnljVEs5S2c0cjJfMnBQYXpUME5lUkZhdDlmTl9id3lvWlZiOU9Rb0JiVk5ZVnRzWk15aGlrQTlxQTRzQ05raHRIVl9kdmUyRHJtc3Z6aGlWZFVubW9oMU5oRlRXQzkzaHdVczFmamxwRENZcVFtb19QOFhaQ25tUWUzbHRKRWJSWmx2eVpvd2RWZFd1cTg2QXZ3MXlqcHhKck9CWTRJSGxORTJnTDFIMWhTZExLZThoOXdfMnd6VGc5QmVUc0lCYTFtYnNoWjRfcmNJMHotTUxEM0t0QThyZm1nb0txYWRQUHpyWU1hQ1ZpczV6OUw2ZGlGTEFMV0YwRzZuOHU1Z2QwQ1Eta0JzVE1RV201ZXhZZmpIVFdOLVZwM2tqR3R2WVFyYWlsd3FIUEtHY2pYZlpZWV9iV1QxV011azhFU0hDeGlZQXg1OVpoSUo2ZktPNWM4d1daRFIyNWRqMVdqTVd0TTR3aTVCM0dIS2NsQ0J6azYtVjZIQ3E4TlprNm9wdEdiVS1wUWxxMDZHbllWUGhhZzdVR0dBbjlPSTNBcllPRS11QVNXT0J1SzJhRW8tX1lMSy16a01uLVZZeVd6MmlneGpNRnY0d21TcHVfb1psZG5rYUp6QlozOFVtSGxhd0cweUtiRjZtQ0EwTWQ2M2RnVWVRRUhaWmt4dGNNUE1hY3J6THRicWFQNGZuZEVpa1RHMjdyay15aEhIM1ZiQ0Fob2pBdVRROHh5X2M1NGEzWGxhRW5HNU9XeHFUWFc5X251QW5yRzhlTWZzWW1xdDBLS2dMYVZ6WHVIU2cwUjlpY3UxSVFabFgxR043U19OT0tUTDhLaG1QeHNlbTZXR2x1R3hsR2RqaEVHWWR3YmFkVUgwOEtNbFgxaWpQSEVTR1ZScjh0a3lINkJpY2Rod0oxN044bnM4V2o1M0F5Vmc5M3c0bVZObVJDcGRPRUNmQ0pPS3B6VWdUbDNtRzhQVFhJSE9kSEtkd3llQnJtVzkyeG93c1J5VDE2Q1ZMMDRMalkwV3ZDTkhqM0RLRjREV2pvNVRuenhSUHgxVVNfX1ZXLUlyV2t0NWV2MnB1WnU2OTRKM2pObUJpLWV0SmRpcXVHUHZ2a3lUS3VOSzRZbFRiSGxmUUZpZE0wRUZVX1VqRjdvRFU4cHFtWFRJUVZ6Z3hIMFQzWFRwd01fNkhIbFR1LTFQVVhfWVdnRk5BdEZfTF9mbTIzbzZjTi0xYzlsQm5tYzZZeHJDXzlJaTNGcHJzS3JtQUVIT3hJam5IZGJrbVRXbWdkaFlNUkpUY2dBZ3hva0dLVnJaWGNwMUhzczVvb3ltYkZYQlVtR1NNdGFyaVpIZ3NLb3ItaFEzeFBqS3BWbDY5bkljdkE1dmRkZFdCVXhEM280b05BSGdoMVdwS1poTnVoREFySUlHNktHSXI1TTNVVjRIV29fRVlNYTdaWEhPZkpTZmc0NTRlaUpQc2tSeEpwMWFsY0J4aENoS3pLVXBXOEZJaG5JMDJrUENyaUN0MU9za0o3R0VwVFUza1F1NTgzeUlYX1BzWG1tRUV0VzZhVkhtTDZBQzBxMGRNUkNTc3BPVUdpY0NaSVh6V2VndGZsY0JVVnVWeDNUVHd4SzVXWGx1NkszbXJEeFBlWVZyNWZJZ0Z5MDhtT2JGUE9nUndOTDloYXlUVXRPU0dhNXNaTzhYWmdjY2N2UTBjUzB6XzZXY3VBWDFDOURraDBDclN1WWI4ZjZyTDZWbjloTUdSWW93RDFiejUxaDVDdGdlVU4xTFVNN2FsM1lEQVhrRTVoNEVmWVh3ME5zdXMtRmF4X1hOQU1ONFY3ZHI5VHZhNUlNMzdBemxRbzRkek1IMk9qQ0NlQ1pCTTV3c2s2TW4zWmIzUnZGS3RvTXppMDVITklfYWJEOGptVkZGb3oxY001S01hbjhMd0o2elVsUkNvQ2VzbU1VcGZHYk1NdzdlT2VaY0lvQzNmUnU2VW1mNmNLbXJhbTRITXBKdTNFcWs1TzZYOUtuR05PRkJINnc4TmtBRVdrZWpGUVREZUNjOHdKcnNzaURhUDhwb3ZBdmpvczF4cmotS0NwTHI1dXVRajhYZktmR2VVczR2WlNub3NwQ3pOVXJwTWJLM2hCUkwybUV3bGx3d1NZZ0lIVThWUzBZVnptbDdyeFpjQW9jUnRVMFVHd0dPT0U4clZjVzNvNmRaclhQTlVWcldGYUVmRXN5YkFSbnN2X3d3OERPMXZqMXZ0TDZ5MDRmZ1JFMTliMEo4eFNDTDgtNE0yeFZaV0k4c2VDUUVfVXptZktmVGZ6b01CZVpjZGM3eHVSeFFBY2E1Q3h1S3pSVHhRRTVuMXUyZ2h6Tm8tcjRRS0l6aDAxeEQxUWNnc3JkSlV4NGRHR1g1eGlwenVxaG16NnZnYkdMWUtxZTFkcHNhYlo1ajY1YUlxYmNOODlFSEhMRVliY0hQQmxpb1g3bGpOdS1kNGlrYTg3NGs4bVJkdDhST3o0NUpCdHFKUm5LSFh6NktNYUhldVNQZzNsUDFNNHktUVU2TEVNSEN3Q2tRV3llM0xVNHFWb3N3UlBqUnQ5R1pDeGNNcjNLRWRmY01GYndhaXRQQURaTExnUUxDdkZGQS10cjB0OHdSQU5hX25oZVJsU01SN2VlaWVSQk5yamtoYlNfV1pWaE8yWW5EdzdtZVJqNW9LRHZORldJaV8wUXY3ZUxvc2VoOHJ4cGZwS3BpT3llUFFLaG1TYTBud3BDbWhNUXBZVFBCMjBrcG41aVJNYzZ3UGxIR1BNUWNjZWFxTllUVlh3MHFPQ0hYbWdBajFtbFJBYlJ2Q1B6QUJfbDFtR3cyRTgxTENGbUlfTFQ4Umg1SGNzSVExUktJMHpwVXpyU1hOd0ZUMkJwVmlyV2Niak94RUhPdUVhUUxTbXRXLVhuam5EUEg1bGNkbGVnWmxyV0kwVThUcjVhRVBObmNpQ1hmZTZyeVlCNjgyb2NyZWI4NW1KVnVVZHV6WU94TmZCMU0xUUxrOEVfdmlBSzhCb0ZaR3Zxd1VPVTBSXzFlLVFTSTJ5OWw1TFZMMmtIWlVHVlRhMWoxNVQwenNhVzVRTmNOTFk3d3RacGxodGFPUTZkdWNpaDlKTEsxZkVkX3ZWb2lGaEtBQk1Pd1ZmWm9jX01ZTTN2aXdWZ05JWlZxOEx4eFlHSjB4UDNVYjdhdlhBMXN2RmhtcWVtcDZ0dmtQenRxcFVRSkFVbU1uZkdUX3hOQ2NMSFhlUjRoaEJQV3BKRlJsSzJ1S3hZTzRSSlhZNkpMa1pWU2J6Q3E4MFo1TElFU3ZtWGk0SS13MHJiNl83b3ItdWdybUdrX2FOVzhJMnFNWFZvRlA4SG1uSXhqS2JZOWZXQzlhTWhkTTg4UFJKMUVZSEtrRktTazdpU3Y3XzU5cVMwM2hESEk1OGxSZHZKaXZsU0x3S1JDemR3Z3d6MlBFVFk3U2QyLXJ2bUw1eHJZYTFtdW53ZHo5MUp2bkkwOEw1TllsOGRlZS1mVldnNE1MdjdHYzc5eklueGlyWEpJWEp0eFpLZ3FOWjUyQTJLVVYxYVIxYV9QZWhOSTZRU1hvSlh1TWQzQmpBMDV3dEdBQkluRUpQclYtenVuOVNvZ25LdWR2SkVKQ0VVOERxYzVNZ0ZYdW5uMnpSR08tRG1aQ0haUjNvLUhhdnQ2SDV4NVU4aDVWX1RnMmlGVU9DM1VmX09EOE1UelV6WGVMZlJwM2k4ZkhWMW1VbXVBYXUxVlRzMkRFdVhqUVdZb3BmMDAtTnByWDRlQnQ4UGhQSzRENlF2UEJlZ0k3b2xURk5SU3huUDlCcmpLVjE0Y0dUZ3FTWDQ3MGdia2pVMG0ydjBHZ0pmNGpRWVo0Z2s3eHN6WUtHdnpaSzJ4WHNDak96WnJSWW1PWHBTeWR0WXlWVDZORlcwUlJIaWNtNnhYSERQTEtZUklxZ09JdS1oUkg5QnRES3o0UGstckJDVUsyN3pkbVZuYmpRZGtoVDdzd1gzSWlvSEhzVU9Fd3k1cF9IMEZKcDR4RWhjdERpeFlzWGdaVzdBTDh5Rkd5Uk5UZ1Z1em5PY0oxV0pqYU5pNU5nVmlVTFRBdnRGLVI1TWJBRWlhNTRFb3AtSUNmSFJZa0NpdndyVkRVU29ZbE5DWVBSRzdYWGpVTUs4SWwxbzNhUl91Si12YjI3WklaYVZkM3NyOTJLMWVfWG81TmpteFM1dUhZSWhkbmtEektmYm04eTI3dUN3VjdGSFZzSXVaX044SXFyWnQzN2tqVC1XSkJwZHNLZWxYRWpIY2xSeGNGdUc2TXR3R2t1N1pNckdPNGpJTUwtRjJXa2toRUxtTzhnNUFHTDNqdGNSVFduMVNGMTg1bDV2REdXb1I1VEIyWlA3QmVFWV9YSVRzVjVZUlRsaGJNYk84U1hYa3dqZ0pzWEZ6bjU1ME1vY2FHc3dOV1ZfTWMxRXV6YVZEdlNMcmd3WEtOalJHc0U1RXpfNEliN1JhNno4NlpKTUxSbm0wbFpDZlFmem1zUWNJSnFfVi1YMmkxWW1YMkcyRlZsZThVVFF3Q1BwX3VzNmNVPQ";


    /**
     * 人脸识别线程
     */
    public void faceRecognizeThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!isFaceRecognizeThreadFinish) {

                    Log.d(TAG, "====人脸识别===" + Thread.currentThread().getName());
                    try {
                        RecognizeData recognizeData = mDetectResultQueue.take();
                        if (recognizeData != null && recognizeData.trackOpt != null) {
                            Log.d(TAG, "recognizeData::" + recognizeData.trackOpt.length);

                            for (FacePassTrackOptions op : recognizeData.trackOpt) {
                                if (op == null)
                                    continue;
                                Log.d(TAG, "op.trackId:" + op.trackId + ",livenessThreshold:" + op.livenessThreshold + ",searchThreshold:" + op.searchThreshold
                                        + "," + op.mRecognizeMode);
                            }

                            if (recognizeData.trackOpt[0] == null) {
                                return;
                            }

                            FacePassRecognitionResult[] recognizeResult = mFacePassHandler.recognize(
                                    GROUP_NAME,
                                    recognizeData.message,
                                    1,
                                    recognizeData.trackOpt[0].trackId,
                                    FacePassRecogMode.FP_REG_MODE_DEFAULT,
//                                recognizeData.trackOpt[0].livenessThreshold,
//                                recognizeData.trackOpt[0].searchThreshold);
                                    -1,
                                    -1);

                            if (recognizeResult != null && recognizeResult.length > 0) {
                                Log.d(DEBUG_TAG, "FacePassRecognitionResult length = " + recognizeResult.length);


                                for (FacePassRecognitionResult result : recognizeResult) {
                                    if (null == result.faceToken) {
                                        Log.d(DEBUG_TAG, "result.faceToken is null.");
                                        continue;
                                    }
                                    String faceToken = new String(result.faceToken);
                                    Log.e(DEBUG_TAG,
                                            "result.detail.faceId: " + faceToken
                                                    + ",livenessThreshold:" + result.detail.livenessThreshold
                                                    + ",result.detail.searchScore: " + result.detail.searchScore
                                                    + ",result.detail.livenessScore: " + result.detail.livenessScore
                                                    + ",result.recognitionState " + result.recognitionState);

                                    Log.d(DEBUG_TAG, "FacePassRecognitionState.RECOGNITION_PASS = " + result.recognitionState);


                                    if (result.detail.searchScore > SEARCH_SCORE && LIVE_RET_CODE == 0) {
                                        //识别结果
                                        startVerify = false;
                                        mDetectResultQueue.clear();
                                        FacePassRecognitionResult finalFaceId = result;

                                        if (callback != null) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.onFaceVerifySuccess(faceToken, result.detail.searchScore, result.detail.livenessScore);
                                                }
                                            });
                                        }

                                    }
//
//                                    if (FacePassRecognitionState.RECOGNITION_PASS == result.recognitionState) {
//                                        //识别结果
//                                        startVerify = false;
//                                        mDetectResultQueue.clear();
//                                        FacePassRecognitionResult finalFaceId = result;
//
//                                        if (callback != null) {
//                                            handler.post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    callback.onFaceVerifySuccess(faceToken, result.detail.searchScore, result.detail.livenessScore);
//                                                }
//                                            });
//                                        }
//                                    }
                                    else {
                                        if (callback != null) {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.onFaceVerifyFail(result.retCode, "", result.detail.searchScore, result.detail.livenessScore);
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }


                    } catch (InterruptedException | FacePassException e) {
                        Log.e(DEBUG_TAG, "失败失败：" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                Log.i(TAG, "结束人脸识别=============================");

            }
        }).start();


    }


    public void release() {
        isReleased = true;
        initFaceHandlerFinish = false;
        isFaceRecognizeThreadFinish = true;
        isFaceDetectThreadFinish = true;
        if (mDetectResultQueue != null) {
            mDetectResultQueue.clear();
        }
        if (mFacePassHandler != null) {
            mFacePassHandler.release();
        }
        if (rgbCameraMange != null) {
            rgbCameraMange.releaseCamera();
        }
        if (irCameraMange != null) {
            irCameraMange.releaseCamera();
        }
    }


    public FacePassManage3568 setCallback(Callback listener) {
        this.callback = listener;
        return this;
    }


    public FacePassManage3568 setRgb(TextureView sfvRgb) {
        this.sfvRgb = sfvRgb;
        return this;
    }

    public FacePassManage3568 setIR(TextureView sfvIR) {
        this.sfvIR = sfvIR;
        return this;
    }


    public int getLocalGroupFaceNum() {
        int num = 0;
        try {
            num = mFacePassHandler.getLocalGroupFaceNum(GROUP_NAME);
        } catch (FacePassException e) {
            e.printStackTrace();
        }
        return num;
    }


    public boolean clearAllGroupsAndFaces() {
        try {
            return mFacePassHandler.clearAllGroupsAndFaces();
        } catch (FacePassException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getLocalGroupInfo() {
        try {
            byte[][] faceTokens = mFacePassHandler.getLocalGroupInfo(GROUP_NAME);
            if (faceTokens != null && faceTokens.length > 0) {
                List<String> faceTokenList = new ArrayList<>();
                for (int j = 0; j < faceTokens.length; j++) {
                    if (faceTokens[j].length > 0) {
                        faceTokenList.add(new String(faceTokens[j]));
                    }
                }
                return faceTokenList;
            }


        } catch (FacePassException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FacePassManage3568 setContext(Context context) {
        this.mContext = context;
        return this;
    }


    public interface Callback {


        void onFaceVerifySuccess(String faceId, float searchScore, float livenessScore);

        void onFaceVerifyFail(int result, String msg, float searchScore, float livenessScore);
    }


}
