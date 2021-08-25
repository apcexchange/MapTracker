package com.example.mapactivity.secondImplementation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mapactivity.R
import com.example.mapactivity.secondImplementation.constants.Constants.POKEMON_IMAGE_BASE_URL
import com.example.mapactivity.secondImplementation.data.Pokemon
import com.example.mapactivity.secondImplementation.data.PokemonList
import com.example.mapactivity.secondImplementation.data.Result
import com.example.mapactivity.secondImplementation.network.OnClickPokemon

class PokemonListAdapter(val context: Context, private var onClickPokemon: OnClickPokemon): RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {
    private val listOfPokemon: ArrayList<Result> = ArrayList()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var img_pokemon: ImageView = itemView.findViewById(R.id.ivPokeListImage) as ImageView
        internal var txt_pokemon: TextView = itemView.findViewById(R.id.tvPokeListName) as TextView
        internal var txt_pokemonNuber: TextView = itemView.findViewById(R.id.tvPokeListNumber) as TextView
//        internal  var layout: CardView = itemView.findViewById(R.id.list_item)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonListAdapter.MyViewHolder {
        val itemView : View = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item,parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: PokemonListAdapter.MyViewHolder, position: Int) {
        //binding views
        val pokemonPosition = listOfPokemon[position]
        val url = pokemonPosition.url

        //get numbers from url dynamically
        val splitUrl:List<String> = url.split("/")
        val pokemonNumber = splitUrl[splitUrl.size-2]

        //capitalize first name of pokemon
        val capitalizeName = pokemonPosition.name[0].uppercase() + pokemonPosition.name.substring(1
        )

        //binding data to views
        holder.txt_pokemon.text = capitalizeName
        holder.txt_pokemonNuber.text = "#$pokemonNumber"


        //Set background colour based on position
//        if (pokemonNumber.toInt()%2 == 0){
//            holder.layout.setBackgroundResource(R.drawable.bright_vault_gradient)
//        }else if (pokemonNumber.toInt()%3 == 0){
//            holder.layout.setBackgroundResource(R.drawable.gradient)
//        }else if (pokemonNumber.toInt()%5 == 0){
//            holder.layout.setBackgroundResource(R.drawable.haikus_gradient)
//        }else{
//            holder.layout.setBackgroundResource(R.drawable.miami_dolphins_gradient)
//        }
        //Fetch image using glide
        Glide.with(context)
            .load("$POKEMON_IMAGE_BASE_URL$pokemonNumber.png")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.img_pokemon)
        //Set onclick listener for view holder
        holder.itemView.setOnClickListener {
            onClickPokemon.accessPokemonDetails(position, pokemonPosition.name, url)
        }

    }

    override fun getItemCount(): Int {
    return listOfPokemon.size
    }
    //add pokemon data to list

    fun addPokemons(pokemons:List<Result>?){
        if (pokemons != null){
            listOfPokemon.clear()
            listOfPokemon.addAll(pokemons)
            notifyDataSetChanged()
        }
    }

}