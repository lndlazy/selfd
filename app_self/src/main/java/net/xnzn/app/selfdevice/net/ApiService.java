package net.xnzn.app.selfdevice.net;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface ApiService {

    String API_HOST = "";//接口地址
    String REQUEST_SUCCESS = "1";


    //获取首页彩票类型
    @FormUrlEncoded
    @POST("home/getLotTypeList")
    Observable<ResultBaseEntity> loginIn(@Field("sign") String dataInfo, @Field("source") int source);



}
