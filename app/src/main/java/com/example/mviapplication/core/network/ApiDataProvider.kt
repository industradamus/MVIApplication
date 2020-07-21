package com.example.mviapplication.core.network

/**
 * @author s.buvaka
 */
interface ApiDataProvider {

    fun provideBaseUrl(): String
}

class ApiDataProviderImpl : ApiDataProvider {

    override fun provideBaseUrl(): String = "https://picsum.photos/"
}
