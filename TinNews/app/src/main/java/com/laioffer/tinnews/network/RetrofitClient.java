package com.laioffer.tinnews.network;

import android.content.Context;

import com.ashokvarma.gander.GanderInterceptor;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String API_KEY = "xxx";
    private static final String BASE_URL = "https://newsapi.org/v2/";

    // Retrofit Client
    public static Retrofit newInstance(Context context) {
        // OkHttpClient 配置拦截器
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new GanderInterceptor(context).showNotification(true))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        // APP 发送网络请求，Retrofit 配置请求参数, 通过 OkHttp 发生网络请求至服务器
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // Gson covert Json String to NewsResponse Object
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    // add header callback
    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("X-Api-Key", API_KEY)
                    .build();
            return chain.proceed(request);
        }
    }
}
