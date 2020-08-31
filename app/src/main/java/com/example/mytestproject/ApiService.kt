package com.example.mytestproject

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search?limit=10&order=DESC&api_key=7fd8c10a-a96d-4e0f-8d6a-6a83ca4ffbce")
    fun search(@Query("page") perPage: Int): Observable<List<Cat>>

    companion object Factory {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.thecatapi.com/v1/images/")
                .build()

            return retrofit.create(ApiService::class.java);
        }
    }
}