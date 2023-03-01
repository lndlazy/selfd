package net.xnzn.app.selfdevice.net;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class RetrofitHttpUtil {

    private static final String TAG = "RetrofitHttpUtil";
    private static RetrofitHttpUtil netService = new RetrofitHttpUtil();

    public static RetrofitHttpUtil getInstance() {
        return netService;
    }

    private RetrofitHttpUtil() {
    }

    private ApiService service;

    public ApiService getService(Context context) {

        if (service == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//log日志
            //  interceptor.setLevel(
//              BuildConfig.isNetLog ? HttpLoggingInterceptor.Level.BODY :
//                      HttpLoggingInterceptor.Level.NONE);//log日志
            OkHttpClient client = new OkHttpClient().newBuilder()
                    //.sslSocketFactory(MSSLSocket.get(context))
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)//错误重连
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
//                    .addNetworkInterceptor()//网络拦截器
                    .build();

            //Retrofit配置
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.API_HOST)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();

            service = retrofit.create(ApiService.class);

        }

        return service;
    }

    /**
     * 转换器
     *
     * @return
     */
    private ObservableTransformer schedulersTransformer() {

        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }

    private ObservableTransformer schedulersTransformerChild() {

        return new ObservableTransformer() {

            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };

    }


    /**
     * 登录
     */
    public void loginIn(Context context, Observer<ResultBaseEntity> subscriber, String position, int source) {
        getService(context).loginIn(position, source)
                .compose(schedulersTransformer())
                .subscribe(subscriber);
    }

} 