package com.movieapp.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService {
    private var httpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var apiClient: MovieClient? = null

    /*fun getClient(): Retrofit? {
        if(retrofit==null)
            retrofit = createRetrofitInstance();
        return retrofit
    }*/

    fun getApiClient(): MovieClient {
        if (apiClient == null) {
            apiClient = ServiceGenerator().createService(
                MovieClient::class.java,
                RequestHeaderInterceptor()
            )
        }
        return apiClient as MovieClient
    }

    internal class ServiceGenerator {
        private var httpClientBuilder: OkHttpClient.Builder? = OkHttpClient.Builder()
        private fun getHttpClientBuilder(): OkHttpClient.Builder {
            if (httpClientBuilder == null) {
                httpClientBuilder = OkHttpClient.Builder()
            }
            return httpClientBuilder as OkHttpClient.Builder
        }

        private var httpClient: OkHttpClient? = null
        private fun getOkHTTPClient(interceptor: Interceptor): OkHttpClient {
            if (httpClient == null) {
                httpClient = getHttpClientBuilder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .build()
            }
            return httpClient as OkHttpClient
        }

        fun <S> createService(serviceClass: Class<S>, interceptor: Interceptor): S {
            val httpClient = getOkHTTPClient(interceptor)
            httpClient.dispatcher.cancelAll()
            val retrofit = createRetrofitInstance()
            return retrofit.create<S>(serviceClass)
        }

        private fun createRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(ApiConstants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }
    }


    internal class RequestHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val originalRequestHeaders = original.headers
            // Request customization: add request headers
            val requestBuilder: Request.Builder = original.newBuilder()
            val request = requestBuilder.build()
            Log.e("REQUEST = ", request.toString())
            val response = chain.proceed(request)
            Log.e("RESPONSE = ", response.toString())
            return response
        }
    }
}