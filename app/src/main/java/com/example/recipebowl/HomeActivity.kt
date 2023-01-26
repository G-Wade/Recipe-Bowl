package com.example.recipebowl

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.koushikdutta.ion.Ion
import org.json.JSONObject

private val Nutella = "https://world.openfoodfacts.org/api/v0/product/3017620422003.json"

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val Button = findViewById<Button>(R.id.Button)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        Ion.with(this).load(Nutella).setHeader("User-Agent", "Gareth Wade")
            .setHeader("Accept", "application/JSON").asString()
            .setCallback { _, result ->
                val jsonObj = JSONObject(result)
                val value = jsonObj.getString("code")
                Button.text = value
            }
    }
}