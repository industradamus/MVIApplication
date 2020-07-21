package com.example.mviapplication.core.network

import retrofit2.Retrofit

/**
 * @author s.buvaka
 */
interface ApiProvider {

    fun provide(): PicsumApi
}

class ApiProviderImpl(private val retrofit: Retrofit) : ApiProvider {

    override fun provide(): PicsumApi = retrofit.create(PicsumApi::class.java)
}
