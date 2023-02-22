package net.xnzn.app.selfdevice.count;

import android.view.MotionEvent;

import net.xnzn.lib_commin_ui.base.BaseTitleBarActivity;
import net.xnzn.lib_log.L;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class CountDownBaseActivity extends BaseTitleBarActivity {

    private int countTime = 30;//页面显示的倒计时时间
    private int noActionTime = 10;//用户无操作时间， 在该时间后显示 倒计时时间
    private Disposable mDDisposable;
    private static final String TAG = "CountDownBaseActivity";
    private boolean isStop = false;
    private int COUNT_TIME_TIME = 30;
    private int NO_ACTION_TIME = 10;

    public long getCountTime() {
        return countTime;
    }

    public void setCountTime(int countTime) {
        COUNT_TIME_TIME = countTime;
        this.countTime = countTime;
    }

    public long getNoActionTime() {
        return noActionTime;
    }

    public void setNoActionTime(int noActionTime) {
        NO_ACTION_TIME = noActionTime;
        this.noActionTime = noActionTime;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                resetTime();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 用户点击了屏幕 取消倒计时操作
     */
    protected void resetTime() {

        try {
            if (mDDisposable != null)
                mDDisposable.dispose();

            showCountDownTime(-1);
            setCountTime(COUNT_TIME_TIME);
            setNoActionTime(NO_ACTION_TIME);
            startCountDown();

        } catch (Exception e) {
            e.printStackTrace();
            L.e(e.getMessage());
        }

    }

    protected abstract void countDownFinish();

    protected abstract void showCountDownTime(int time);

    protected void startCountDown() {

        //第一个参数 序列的起点,第二个参数 事件的数量,第三个参数 首次事件延迟发送的事件,第四个参数 事件发送间隔时间,第五个参数 时间单位
        Observable.intervalRange(0, getCountTime() + getNoActionTime() + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())   //在子线程在执行计时
                .observeOn(AndroidSchedulers.mainThread()) //在主线程上更新UI
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDDisposable = d;
                        L.i(TAG + ":onSubscribe");
                    }

                    @Override
                    public void onNext(Long value) {
                        //记录当前计时数
//                        playRecordTime = value;
//                        String format = sdf.format(new Date((answerTime - playRecordTime) * 1000));
//                        mTimeTv.setText(format);
                        L.i(TAG + ": onNext:" + value);

                        if (isStop)
                            return;

                        if (value > getNoActionTime())
                            showCountDownTime(COUNT_TIME_TIME + NO_ACTION_TIME - value.intValue() + 1);

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.i(TAG + ":onError");
                    }

                    @Override
                    public void onComplete() {
                        countDownFinish();
                        L.i(TAG + ":onComplete");
                    }
                });

    }


    protected void stop() {

        try {
            if (mDDisposable != null)
                mDDisposable.dispose();

            isStop = true;

        } catch (Exception e) {
            e.printStackTrace();
            L.e(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
}
