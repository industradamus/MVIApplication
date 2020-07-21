package com.example.mviapplication.core.network

import com.example.mviapplication.core.models.PixelsResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author s.buvaka
 */
interface PixelsApi {

    @GET("search/?per_page=15&query=people")
    fun getImageList(@Query("page") page: Int): Observable<Response<PixelsResponse>>
}
