package com.example.mapactivity.secondImplementation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mapactivity.R
import com.example.mapactivity.secondImplementation.data.Result
import com.example.mapactivity.secondImplementation.data.Type
import com.example.mapactivity.secondImplementation.data.TypeX
import com.example.mapactivity.secondImplementation.util.PokemonTypeColor.getTypeColor

class PokemonTypeAdapter (internal var context: Context):
    RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>(){
    private var listOfType: List<Type> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(pokType: List<Type>){
        this.listOfType = pokType
        notifyDataSetChanged()
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.poke_type_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val type = listOfType[position]
        holder.bind(listOfType[position])
        //holder.pokeType.text = type.name
    }

    override fun getItemCount(): Int {
        return listOfType.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val pokeType = itemView.findViewById<TextView>(R.id.tvPokeType)
            val pokeTypeCard = itemView.findViewById<CardView>(R.id.pokeType)

        fun bind(pok: Type){
            pokeType.text = pok.type.name

            //set background color for type
            val color = getTypeColor(pok.type.name)
            pokeTypeCard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,color))
        }
    }
}