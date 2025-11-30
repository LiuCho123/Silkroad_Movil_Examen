package com.example.hollow_knight_silkroad.Network

import com.google.gson.annotations.SerializedName

data class LeaderboardResponse(
    val data: LeaderboardData
)

data class LeaderboardData(
    val runs: List<RunContainer>
)

data class RunContainer(
    val place: Int,
    val run: RunInfo
)

data class RunInfo(
    val times: RunTimes,
    val players: List<PlayerInfo>,

    @SerializedName("weblink")
    val link: String
)

data class RunTimes(
    @SerializedName("primary_t")
    val tiempoSegundos: Double
)

data class PlayerInfo(
    val name: String? = "Anónimo",
    val id: String?
)