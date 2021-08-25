package com.example.mapactivity.secondImplementation.network

import com.example.mapactivity.secondImplementation.data.Pokemon
import com.example.mapactivity.secondImplementation.data.PokemonList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiInterface {
    @GET("pokemon")
    fun getData(
        @Query("limit") limit:Int,
        @Query("offset") offset:Int
    ): Call<PokemonList>


    @GET("pokemon/{id}")
    fun getPokemonKey(@Path("id") id:Int): Call<Pokemon>
}