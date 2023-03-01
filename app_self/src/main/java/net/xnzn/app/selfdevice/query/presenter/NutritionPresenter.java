package net.xnzn.app.selfdevice.query.presenter;

import net.xnzn.app.selfdevice.query.bean.YingYangRequest;
import net.xnzn.app.selfdevice.query.view.NutritionView;
import net.xnzn.leniu_http.yunshitang.model.YunContent;


public class NutritionPresenter {

    private NutritionView view;

    public NutritionPresenter(NutritionView view) {
        this.view = view;
    }

    public void queryData(int dayOrWeek, Long custId) {
        YingYangRequest request = new YingYangRequest();
        request.setAnalyseScope(dayOrWeek);
        request.setCustId(custId);

        YunContent yunContent = new YunContent(request);
//        mModel.queryYingYangData(yunContent)
//                .subscribeOn(Schedulers.io())
////                .retryWhen(new RetryWithDelay(3, 3))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
//                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();//显示下拉刷新的进度条
//                }).subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() -> {
//                    mRootView.hideLoading();//隐藏下拉刷新的进度条
//                })
//                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
//                .subscribe(new ErrorHandleSubscriber<CoreBaseResponse<YingYangResponse>>(mErrorHandler) {
//                    @Override
//                    public void onNext(CoreBaseResponse<YingYangResponse> response) {
//                        YingYangResponse data = response.getData();
//                        if (response.isSuccess()) {
//                            mRootView.onQueryYYSuccess(data);
//                        } else {
//                            mRootView.onQueryYYFail(response.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        mRootView.onQueryYYFail(AppUtils.getHttpErrorInfo(t));
//                    }
//                });

    }

}