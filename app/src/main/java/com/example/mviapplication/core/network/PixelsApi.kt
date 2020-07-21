package com.example.mviapplication.core.network

import com.example.mviapplication.core.models.PixelsResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author s.buvaka
 */
interface PixelsApi {

    @GET("search?query=people")
    fun getImageList(): Observable<Response<PixelsResponse>>
}
