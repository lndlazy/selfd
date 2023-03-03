package net.xnzn.app.selfdevice.common;

import android.util.Pair;

import java.util.concurrent.ArrayBlockingQueue;


public class ComplexFrameHelper {

    private static ArrayBlockingQueue<Pair<CameraPreviewData, CameraPreviewData>> complexFrameQueue
            = new ArrayBlockingQueue<>(2);
    private static CameraPreviewData rgbFrameBuffer = null;
    private static CameraPreviewData irFrameBuffer = null;

    private static void makeComplexFrame() {
//        Log.d("FacePassDemo", "rgbFrameBuffer = " + (rgbFrameBuffer != null) + "  irFrameBuffer =" + (irFrameBuffer != null));
        if ((rgbFrameBuffer != null) && (irFrameBuffer != null)) {
            if (complexFrameQueue.remainingCapacity() > 0) {
//                Log.d("FacePassDemo", "凑齐两帧！");
                complexFrameQueue.offer(new Pair(rgbFrameBuffer, irFrameBuffer));
            }
            rgbFrameBuffer = null;
            irFrameBuffer = null;
        }
    }

    public static void addRgbFrame(CameraPreviewData rgbFrame) {
        synchronized (ComplexFrameHelper.class) {
            if (rgbFrameBuffer == null) {
                rgbFrameBuffer = rgbFrame;
//                Log.d("FacePassDemo", "凑齐RGB+ir！");
            }
            makeComplexFrame();
        }
    }

    public static void addIRFrame(CameraPreviewData infraFrame) {
        synchronized (ComplexFrameHelper.class) {
            if (irFrameBuffer == null) {
                irFrameBuffer = infraFrame;
            }
            makeComplexFrame();
        }
    }

    public static Pair<CameraPreviewData, CameraPreviewData> takeComplexFrame() throws InterruptedException {

        return complexFrameQueue.take();
    }


}
