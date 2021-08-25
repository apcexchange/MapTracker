package com.example.mapactivity.secondImplementation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mapactivity.databinding.ActivityPokemonDetailBinding
import com.example.mapactivity.secondImplementation.constants.Constants.POKEMON_IMAGE_BASE_URL
import com.example.mapactivity.secondImplementation.network.ConnectivityLiveData
import com.example.mapactivity.secondImplementation.network.PokemonDetailViewModel

private lateinit var binding: ActivityPokemonDetailBinding
private lateinit var connectivityLiveData: ConnectivityLiveData
private lateinit var apiUrl: String
private lateinit var endPoint: String

class PokemonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

//        val name: TextView=findViewById(R.id.tvdName)
//        val image: ImageView=findViewById(R.id.ivdImage)
//        val heigth: TextView=findViewById(R.id.tvdHeigth)
//        val wiigth: TextView=findViewById(R.id.tvdWeight)
//        val abilities: TextView=findViewById(R.id.tvdAbilities)
//        val base_exp: TextView=findViewById(R.id.tvdBase_Exp)
//        val form_list: TextView=findViewById(R.id.tvdForm_List)
//        val game_index: TextView=findViewById(R.id.tvdGame_Index)
//        val held_itmes: TextView=findViewById(R.id.tvdHeld_Items)
//        val movies_list: TextView=findViewById(R.id.tvdMovies_list)
//        val order: TextView=findViewById(R.id.tvdOrder)
//        val species: TextView=findViewById(R.id.tvdSpecies)
//        val stats: TextView=findViewById(R.id.tvdStarts)
//        val type:TextView=findViewById(R.id.tvdType)

        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_pokemon_detail)

        //Get url including endpoint from previous activity
        apiUrl = intent.getStringExtra("pokemonDetails").toString()

        //Get endpoint from url
        val splitUrl: List<String> = apiUrl.split("/")
        endPoint = splitUrl[splitUrl.size-2]


        connectivityLiveData = ConnectivityLiveData(application)
        Log.d("TAG", "onCreate: before function", )
        getPokemonDetails(endPoint)
        Log.d("TAG", "onCreate: after function", )
        
    }
fun getPokemonDetails(endpoint : String){

  val viewModel = ViewModelProvider(this)[PokemonDetailViewModel::class.java]
    viewModel.pokemonDetailLiveData.observe(this,{
        //update ui
        if (it != null){
            val capitalizeName = it.name[0].uppercase() + it.name.substring(1)
            Log.d("TAG", "$it: log It")
            Log.d("TAG", "getPokemonDetails: capitalize", )
            //binding details to my textview
            binding.tvdName.text = capitalizeName
            binding.tvdHeigth.text = it.height.toString()
            Log.d("TAG", "getPokemonDetails: after capitalize", )


//            Fetch image using glide
            Glide.with(this)
                .load("$POKEMON_IMAGE_BASE_URL$endpoint.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivdImage)
        }else{
            Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
        }
    })

    connectivityLiveData.observe(this,{isAvailable ->
        when(isAvailable){
            true -> {
                //handles ui view
                viewModel.getPokemonDetail(endpoint)
            }
            false -> {
                Toast.makeText(this, "no network",Toast.LENGTH_SHORT).show()
            }
        }
    })

}

}


//
//    //Get url including endpoint from previous activity
//    apiUrl = intent.getStringExtra("pokemonDetails").toString()
//    //Get endpoint from url
//    val splitUrl: List<String> = apiUrl.split("/")
//    endPoint = splitUrl[splitUrl.size - 2]
//    Log.d("GKB", "onCreate: $apiUrl")
//    connectivityLiveData = ConnectivityLiveData(application)
//    getPokemonDetails(endPoint)
//}
//
//fun getPokemonDetails(endPoint: String) {
//    val viewModel = ViewModelProvider(this)[PokemonDetailViewModel::class.java]
//    viewModel.pokemonDetailLiveData.observe(this, {
//        //Update UI visibility
//        if (it != null) {
//            val capitalizeName = it.name[0].uppercase() + it.name.substring(1)
//
//            //Set fetched texts to textviews
//            binding.tvdGameIndex.text = it.id.toString()
//            binding.tvdAbilities.text = it.base_experience.toString()
//            binding.tvdHeigth.text = it.height.toString()
//            binding.tvdName.text = it.weight.toString()
////                binding.detailPageName.text = capitalizeName
//            binding.tvdHeigth.text = it.stats[0].base_stat.toString()
////                binding.attackValue.text = it.stats[1].base_stat.toString()
////                binding.tvdSpecies.text = it.stats[2].base_stat.toString()
////                binding.specialAttackValue.text = it.stats[3].base_stat.toString()
////                binding.specialDefenceValue.text = it.stats[4].base_stat.toString()
////                binding.speedValue.text = it.stats[5].base_stat.toString()
//
//
////Fetch image using glide
//            Glide.with(this)
//                .load("$POKEMON_IMAGE_BASE_URL${this.endPoint}.png")
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(binding.ivdImage)
//
//        } else {
//            Log.d("TAG", "makeApiCallFromViewModel: Error fetching data")
//            Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
//        }
//    })
//    connectivityLiveData.observe(this, { isAvailable ->
//        when(isAvailable) {
//            true -> {
//                viewModel.getPokemonDetail(endPoint)
//                //Handle UI views
//            }
//            false -> {
//                //Handle UI
//                Toast.makeText(this, "Network is unavailable", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    })
//}