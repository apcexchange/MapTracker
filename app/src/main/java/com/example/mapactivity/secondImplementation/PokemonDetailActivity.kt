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
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxAttack
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxDefense
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxExp
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxHp
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxSpeed
import com.example.mapactivity.secondImplementation.network.ConnectivityLiveData
import com.example.mapactivity.secondImplementation.network.PokemonDetailViewModel

private lateinit var binding: ActivityPokemonDetailBinding
private lateinit var connectivityLiveData: ConnectivityLiveData
private lateinit var apiUrl: String
private lateinit var endPoint: String

class PokemonDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

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
            binding.detailPokeName.text = capitalizeName
            binding.height.text = (it.height.toString() + " M")
            binding.weight.text = (it.weight.toString() + " KG")



            /*Set-up Progress View*/

            binding.progressHp.apply {
                val hp: Int = (30..300).random()
                progress = hp.toFloat()
                max = maxHp.toFloat()
                labelText = "$hp/$maxHp"
            }

            binding.progressAtk.apply {
                val attack: Int = (30..300).random()
                progress = attack.toFloat()
                max = maxAttack.toFloat()
                labelText = "$attack/$maxAttack"
            }
            binding.progressDef.apply {
                val defense: Int = (30..300).random()
                progress = defense.toFloat()
                max = maxDefense.toFloat()
                labelText = "$defense/$maxDefense"
            }

            binding.progressSpd.apply {
                val speed: Int = (30..300).random()
                progress = speed.toFloat()
                max = maxSpeed.toFloat()
                labelText = "$speed/$maxSpeed"
            }
            binding.progressExp.apply {
                val exp: Int = (30..300).random()
                progress = exp.toFloat()
                max = maxExp.toFloat()
                labelText = "$exp/$maxExp"
            }



//            Fetch image using glide
            Glide.with(this)
                .load("$POKEMON_IMAGE_BASE_URL$endpoint.png")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.detailImage)
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
