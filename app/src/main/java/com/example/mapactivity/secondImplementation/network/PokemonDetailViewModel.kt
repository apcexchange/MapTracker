package com.example.mapactivity.secondImplementation.network

import com.example.mapactivity.secondImplementation.data.Pokemon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class PokemonDetailViewModel: ViewModel() {
    private val _pokemonDetailLiveData: MutableLiveData<Pokemon> = MutableLiveData()
    val pokemonDetailLiveData: MutableLiveData<Pokemon> = _pokemonDetailLiveData

    fun getPokemonDetail(id: String){
        val retrofitInstance = RetrofitClient.pokemonEndPoint().getPokemonKey(id)
        retrofitInstance.enqueue(object : Callback<Pokemon> {
            override fun onResponse(
                call: Call<Pokemon>,
                response: Response<Pokemon>
            ) {
                if (response.isSuccessful){
                    _pokemonDetailLiveData.postValue(response.body())
                }else{
                    _pokemonDetailLiveData.postValue(null)
                }
            }
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                _pokemonDetailLiveData.postValue(null)
            }
        })
    }
}
