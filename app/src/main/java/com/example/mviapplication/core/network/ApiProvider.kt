package com.example.mviapplication.core.network

import retrofit2.Retrofit

/**
 * @author s.buvaka
 */
interface ApiProvider {

    fun provide(): PixelsApi
}

class ApiProviderImpl(private val retrofit: Retrofit) : ApiProvider {

    override fun provide(): PixelsApi = retrofit.create(PixelsApi::class.java)
}
