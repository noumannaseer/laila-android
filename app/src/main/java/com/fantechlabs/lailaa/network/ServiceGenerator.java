package com.fantechlabs.lailaa.network;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.network.services.SessionInterceptor;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.fantechlabs.lailaa.utils.Constants.BASE_URL;


//************************************************
public class ServiceGenerator
//************************************************
{
    private @Getter
    static Retrofit mRetroFit;

    //**************************************
    public static Retrofit getRetrofitInstance(boolean isMultipartCall, String baseUrl)
    //**************************************
    {
        if (mRetroFit == null || true)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(new SessionInterceptor())
                    .build();
            if (isMultipartCall)
            {
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addNetworkInterceptor(new SessionInterceptor())
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .cookieJar(new JavaNetCookieJar(cookieManager))
                        .build();

            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            mRetroFit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return mRetroFit;
    }
    //**************************************
    public static Retrofit getRetrofitInstanceForXml(boolean isMultipartCall, String baseUrl)
    //**************************************
    {
        if (mRetroFit == null || true)
        {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(new SessionInterceptor())
                    .build();
            if (isMultipartCall)
            {
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addNetworkInterceptor(new SessionInterceptor())
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .cookieJar(new JavaNetCookieJar(cookieManager))
                        .build();

            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            mRetroFit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                    .build();
        }
        return mRetroFit;
    }
    //**************************************
    public static <S> S createService(Class<S> serviceClass)
    //**************************************
    {
        if (!AndroidUtil.isNetworkStatusAvialable())
        {
            AndroidUtil.toast(false, AndroidUtil.getString(R.string.no_internet));
            return null;
        }
        return getRetrofitInstance(false, BASE_URL).create(serviceClass);
    }

    //**************************************
    public static <S> S createService(Class<S> serviceClass, boolean isMultipartCall, String baseUrl)
    //**************************************
    {
        if (!AndroidUtil.isNetworkStatusAvialable())
        {
            AndroidUtil.toast(false, AndroidUtil.getString(R.string.no_internet));
            return null;
        }
        return getRetrofitInstance(isMultipartCall, baseUrl).create(serviceClass);
    }
    //**************************************
    public static <S> S createServiceForXml(Class<S> serviceClass, boolean isMultipartCall, String baseUrl)
    //**************************************
    {
        if (!AndroidUtil.isNetworkStatusAvialable())
        {
            AndroidUtil.toast(false, AndroidUtil.getString(R.string.no_internet));
            return null;
        }
        return getRetrofitInstanceForXml(isMultipartCall, baseUrl).create(serviceClass);
    }
}
