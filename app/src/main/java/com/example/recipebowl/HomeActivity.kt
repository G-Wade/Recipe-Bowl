package com.example.recipebowl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
    }
}