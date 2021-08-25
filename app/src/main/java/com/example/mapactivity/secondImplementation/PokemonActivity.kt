package com.example.mapactivity.secondImplementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapactivity.R
import com.example.mapactivity.databinding.ActivityPokemonBinding
import com.example.mapactivity.secondImplementation.adapters.PokemonListAdapter
import com.example.mapactivity.secondImplementation.livedata.PokemonListViewModel
import com.example.mapactivity.secondImplementation.network.ConnectivityLiveData
import com.example.mapactivity.secondImplementation.network.OnClickPokemon
import com.example.mapactivity.secondImplementation.network.PokemonApiInterface
import kotlin.properties.Delegates

class PokemonActivity : AppCompatActivity(), OnClickPokemon {
    lateinit var connectivityLiveData: ConnectivityLiveData
    lateinit var pokemonListAdapter: PokemonListAdapter
    lateinit var binding: ActivityPokemonBinding
    var count by Delegates.notNull<Int>()
    var condition by Delegates.notNull<Boolean>()
    var offset by Delegates.notNull<Int>()
    var limit: Int = 20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set up adapter
        pokemonListAdapter = PokemonListAdapter(this,this)

        //Set up recyclerview
        binding.recyclerView.adapter = pokemonListAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(this,2)

        connectivityLiveData = ConnectivityLiveData(application)
        makeApiCallFromViewModel(50,0)

    }

    private fun makeApiCallFromViewModel(limit: Int, offset: Int) {
        val viewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]
        viewModel.pokemonListLiveData.observe(this,{
            if (it != null){

//                add to recyclerView
            val pokemons = it.results
                count = it.count

                //Populate response on recyclerview
                pokemonListAdapter.addPokemons(pokemons)

                Log.d("TAG","makeApiCallFromViewModel: ${it.results}")
            }else{
                Log.d("TAG","makeApiCallFromViewModel: Error fetching data")

            }
        })

        connectivityLiveData.observe(this,{isAvalable->
            when(isAvalable){
                true -> {
                    viewModel.makeApiCall(limit,offset)  //handles ui view
                }
                false -> {
                    //handles ui
                    Toast.makeText(this,"Error fetching data", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun accessPokemonDetails(position: Int, name: String, url: String) {
        TODO("Not yet implemented")
    }

    //move to next activity

}