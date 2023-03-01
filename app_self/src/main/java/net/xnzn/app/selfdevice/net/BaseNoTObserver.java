package net.xnzn.app.selfdevice.net;


import android.util.Log;

import io.reactivex.Observer;

/**
 * Created by Aaron on 17/4/17.
 */

public abstract class BaseNoTObserver<T extends ResultBaseEntity> implements Observer<T> {

    private static final String TAG = "BaseNoTObserver";

    @Override
    public void onNext(T t) {

        if (ApiService.REQUEST_SUCCESS.equals(t.getSuccess()))
//            onReLogin();
            onHandleSuccess(t);

//        else if (t.getCode() == 0)
//            onReLogin(t.getCode_msg());
        else {
            onHandleError(t.getError());
            Log.e(TAG, "错误提示::" + t.getError());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "  ******  onError  ******  网络错误 : " + e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    public abstract void onHandleSuccess(T t);

    public abstract void onHandleError(String message);

    //public abstract void onReLogin(String message);

}
