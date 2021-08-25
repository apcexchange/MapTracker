package com.example.mapactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mapactivity.secondImplementation.PokemonActivity

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val mapButton = findViewById<Button>(R.id.btn_map)
        val pokemonButton = findViewById<Button>(R.id.btn_pokemon)

        mapButton.setOnClickListener {
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }


        pokemonButton.setOnClickListener {
            val intent = Intent(this,PokemonActivity::class.java)
            startActivity(intent)
        }
    }
}