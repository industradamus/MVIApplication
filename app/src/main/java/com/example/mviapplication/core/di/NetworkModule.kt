@file:Suppress("RemoveExplicitTypeArguments")

package com.example.mviapplication.core.di

import com.example.mviapplication.core.network.*
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author s.buvaka
 */
private const val TIMEOUT_IN_SECOND = 30
private const val HEADER_AUTHORIZATION = "Authorization"

val networkModule = module {

    factory<ApiDataProvider> { ApiDataProviderImpl() }
    factory<OkHttpClient> { buildOkHttpClient(get<ApiDataProvider>()) }
    factory<Retrofit> {
        buildRetrofit(
            get<ApiDataProvider>(), get<OkHttpClient>(), GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            ), RxJava2CallAdapterFactory.create()
        )
    }
    factory<ApiProvider> { ApiProviderImpl(get<Retrofit>()) }
    factory<PixelsApi> { providePicsumApi(get<ApiProvider>()) }
}

private val loggingInterceptor = HttpLoggingInterceptor()

private fun buildOkHttpClient(apiDataProvider: ApiDataProvider): OkHttpClient {
    return OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .addInterceptor(getApiKeyInterceptor(apiDataProvider.provideApiKey()))
        .addInterceptor(loggingInterceptor)
        .readTimeout(TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT_IN_SECOND.toLong(), TimeUnit.SECONDS)
        .build()
}

private fun buildRetrofit(
    apiDataProvider: ApiDataProvider,
    client: OkHttpClient,
    converterFactory: Converter.Factory,
    callAdapterFactory: CallAdapter.Factory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(apiDataProvider.provideBaseUrl())
        .client(client)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .build()
}

private fun providePicsumApi(provider: ApiProvider): PixelsApi = provider.provide()

private fun getApiKeyInterceptor(apiKey: String) = Interceptor { chain ->
    val url = chain.request()
        .url
        .newBuilder()
        .build()

    val newRequest = chain.request()
        .newBuilder()
        .addHeader(HEADER_AUTHORIZATION, apiKey)
        .url(url)
        .build()

    chain.proceed(newRequest)
}
