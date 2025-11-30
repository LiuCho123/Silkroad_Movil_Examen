package com.example.hollow_knight_silkroad.Network

import retrofit2.Response
import retrofit2.http.GET

interface SpeedrunApiService {

    @GET("api/v1/leaderboards/hollowknight/category/02q8o4p2?top=1&var-yn2p3085=81w7r6vq")
    suspend fun getWorldRecord(): Response<LeaderboardResponse>
}