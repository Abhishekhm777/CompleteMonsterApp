package com.example.androiddata.data

import com.example.androiddata.model.Monster
import retrofit2.Response
import retrofit2.http.GET

interface MonsterService {
    @GET("/feed/monster_data.json")
    suspend fun getMonsterdata(): Response<List<Monster>>
}