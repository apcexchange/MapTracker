package com.example.mapactivity.secondImplementation.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapactivity.secondImplementation.data.PokemonList
import com.example.mapactivity.secondImplementation.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListViewModel : ViewModel() {
    private val _pokemonListLiveData: MutableLiveData<PokemonList> = MutableLiveData()
    val pokemonListLiveData: MutableLiveData<PokemonList> = _pokemonListLiveData


    var loadError = MutableStateFlow("")
    var isLoading = MutableStateFlow(false)
    var endReached = MutableStateFlow(false)

    private var cachedPokemonList = listOf<PokemonList>()
    private var isSearchStarting = true
    var isSearching = MutableStateFlow(false)

    fun makeApiCall(myLimit: Int, offset: Int){
        val retrofitInstance = RetrofitClient.pokemonEndPoint().getData(myLimit, offset)
        retrofitInstance.enqueue(object : Callback<PokemonList> {
            override fun onResponse(
                call: Call<PokemonList>,
                response: Response<PokemonList>
            ) {
                if (response.isSuccessful){
                    _pokemonListLiveData.postValue(response.body())
                }else{
                    _pokemonListLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<PokemonList>, t: Throwable) {
                _pokemonListLiveData.postValue(null)
            }
        })
    }

    fun searchPokemon(query: String){
        val instance = RetrofitClient
        val listeToSeach = if (isSearchStarting){
            pokemonListLiveData.value
        }else{
            cachedPokemonList
        }



    }
}