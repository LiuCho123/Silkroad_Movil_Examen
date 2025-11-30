package com.example.hollow_knight_silkroad.Network

import retrofit2.Response
import retrofit2.http.GET

interface SpeedrunApiService {

    @GET("api/v1/leaderboards/hollowknight/category/7dgrrxk4?top=1")
    suspend fun getWorldRecord(): Response<LeaderboardResponse>
}