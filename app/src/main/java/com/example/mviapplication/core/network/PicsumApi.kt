package com.example.mviapplication.core.network

import com.example.mviapplication.core.models.PicsumImage
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author s.buvaka
 */
interface PicsumApi {

    @GET("v2/list")
    fun getImage(): Observable<Response<List<PicsumImage>>>
}
