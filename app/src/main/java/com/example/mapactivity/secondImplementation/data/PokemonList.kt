package com.example.mapactivity.secondImplementation.data

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)