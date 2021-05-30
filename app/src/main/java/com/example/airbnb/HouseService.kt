package com.example.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/53313412-d9d9-4aeb-81e7-19c4d56f7d4f")
    fun getHouseList() : Call<HouseDto>

}