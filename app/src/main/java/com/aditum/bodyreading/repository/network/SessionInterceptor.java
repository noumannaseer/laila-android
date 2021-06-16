package com.aditum.bodyreading.repository.network;

import android.text.TextUtils;
import android.util.Log;

import com.aditum.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//******************************************************
public class SessionInterceptor implements Interceptor
//******************************************************
{
    public static final String TAG = "SessionToken";
    public static final String CONTENT_TYPE = "application/json";

    //******************************************************
    @Override
    public Response intercept(Chain chain)
            throws IOException
    //******************************************************
    {
        Request request = chain.request();
        long t1 = System.nanoTime();
        request = processRequest(request);
        Response response = chain.proceed(request);
        response = processResponse(response);
        return response;
    }

    //Add cookies for all subsequent requests
    //******************************************************
    private Request processRequest(Request request)
    //******************************************************
    {
        if (!request.url().toString().contains("login/1")) {
            String session = SharedPreferencesUtils.getHeader();
            if (TextUtils.isEmpty(session))
                return request;

            Log.d(TAG, "processRequest: Cookie: " + session);

            if (!session.startsWith("session="))
                session = "session=" + session;
            request = request.newBuilder().addHeader("Cookie", session).build();
        }
        Log.d(TAG, "processRequest: requestHeaders - >" + request.headers()
                .toString());
        return request;
    }

    //Extract cookies from login url
    //******************************************************
    private Response processResponse(Response response)
    //******************************************************
    {
        if (response.request().url().toString().contains("login/1")) {
            final List<String> cookieHeaders = response.headers("Set-Cookie");
            String session = "";
            for (String cookieHeader : cookieHeaders) {
                if (cookieHeader.contains("session="))
                    // session = cookieHeader.substring(0, cookieHeader.indexOf(';'));
                    session = cookieHeader.split(";")[0];
            }

            Log.d(TAG, "processResponse: sessionStorage: " + session);

            if (!TextUtils.isEmpty(session))
                SharedPreferencesUtils.setHeader(session);
        }
        return response;
    }
}
