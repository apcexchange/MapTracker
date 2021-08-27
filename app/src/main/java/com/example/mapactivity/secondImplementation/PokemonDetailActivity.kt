package com.example.mapactivity.secondImplementation

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mapactivity.R
import com.example.mapactivity.databinding.ActivityPokemonDetailBinding
import com.example.mapactivity.secondImplementation.adapters.PokemonTypeAdapter
import com.example.mapactivity.secondImplementation.constants.Constants.POKEMON_IMAGE_BASE_URL
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxAttack
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxDefense
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxExp
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxHp
import com.example.mapactivity.secondImplementation.data.Pokemon.Companion.maxSpeed
import com.example.mapactivity.secondImplementation.network.ConnectivityLiveData
import com.example.mapactivity.secondImplementation.network.PokemonDetailViewModel
import com.example.mapactivity.secondImplementation.util.PokemonTypeColor

private lateinit var binding: ActivityPokemonDetailBinding
private lateinit var connectivityLiveData: ConnectivityLiveData
private lateinit var apiUrl: String
private lateinit var endPoint: String
lateinit var typeRecycler: RecyclerView



class PokemonDetailActivity : AppCompatActivity() {
    lateinit var pokemonTypeAdapter: PokemonTypeAdapter
    private lateinit var bitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {

        pokemonTypeAdapter = PokemonTypeAdapter(this)


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

        typeRecycler= findViewById(R.id.pokeTypeRecyclerView)

        // type recyclerview
        typeRecycler.adapter=pokemonTypeAdapter
        typeRecycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

//   val detailImage = findViewById<ImageView>(R.id.detailImage)

    }
fun getPokemonDetails(endpoint : String){

  val viewModel = ViewModelProvider(this)[PokemonDetailViewModel::class.java]
    viewModel.pokemonDetailLiveData.observe(this,{
        //update ui
        if (it != null){
            binding.progressBar2.visibility = View.GONE

            val capitalizeName = it.name[0].uppercase() + it.name.substring(1)
            Log.d("TAG", "$it: log It")
            Log.d("TAG", "getPokemonDetails: capitalize", )
            //binding details to my textview
            binding.detailPokeName.text = capitalizeName
            binding.height.text = (it.height.toString() + " M")
            binding.weight.text = (it.weight.toString() + " KG")


        //setting the type adaapter
            pokemonTypeAdapter.setDataList(it.types)

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

            Log.d("TAG", "getPokemonDetails: after capitalize", )


//            Fetch image using glide and setting background color
            Glide.with(this)
                .load("$POKEMON_IMAGE_BASE_URL$endpoint.png")
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("TAG", "onLoadFailed: Couldn't fetch image")
                        return false
                    }


                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            Palette.from(resource.toBitmap()).generate(){
                                    palette ->
                                palette?.let {
                                    val intColor = it.dominantSwatch?.rgb ?:1
                                    binding.detailPage.setBackgroundColor(intColor)
                                }

                            }
                        }
                        return false
                    }

                })
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
