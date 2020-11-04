package com.example.imageloadmoreview

import android.content.Context
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.picasso.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ProvideRetrofit {

    companion object {
        fun create(context: Context, removeLog: Boolean = false): ApiService {
            val baseUrl = MyConstant.BASE_URL
            val defaultTimeOut = 45L
//            val defaultTimeOut = 10L



            fun release(): Retrofit {
                return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                        GsonConverterFactory.create(
                            GsonBuilder()
//                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .setLenient()
                                .create()
                        )
                    )
                    .baseUrl(baseUrl)
                    .client(OkHttpClient().newBuilder()
                        .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
                        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                        .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                        .addInterceptor(ChuckInterceptor(context))
                        .build())
                    .build()
            }

            fun debug(): Retrofit {
                return if (removeLog) {
                    Retrofit.Builder()
                        .addConverterFactory(
                            GsonConverterFactory.create(
                                GsonBuilder()
//                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .setLenient()
                                    .create()
                            )
                        )
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(baseUrl)
                        .client(OkHttpClient().newBuilder()
                            .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                            .addInterceptor(ChuckInterceptor(context))
                            .build())
                        .build()
                } else {
                    Retrofit.Builder()
                        .addConverterFactory(
                            GsonConverterFactory.create(
                                GsonBuilder()
//                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    .setLenient()
                                    .create()
                            )
                        )
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(baseUrl)
//                        .client(
//                            OkHttpClient.Builder()
//                                .addInterceptor(ChuckInterceptor(context))
//                                .build()
//                        )
                        .client(OkHttpClient().newBuilder()
                            .connectTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .readTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .writeTimeout(defaultTimeOut, TimeUnit.SECONDS)
                            .addInterceptor(ChuckInterceptor(context))
                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                            .build())
                        .build()
                }
            }
            return if (BuildConfig.DEBUG) {
                debug().create(ApiService::class.java)
            } else
                release().create(ApiService::class.java)
        }
    }
}