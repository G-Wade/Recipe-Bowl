package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.settingsNav
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.browseNav -> {
                    val newIntent = Intent(this, HomeActivity::class.java)
                    startActivity(newIntent)
                    finish()
                }
                R.id.profileNav -> {
                    val newIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(newIntent)
                    finish()
                }
                R.id.settingsNav -> {
                    val newIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(newIntent)
                    finish()
                }
                R.id.logoutNav -> {
                    auth.signOut()
                    val newIntent = Intent(this, LoginActivity::class.java)
                    startActivity(newIntent)
                    finish()
                }
            }
            true
        }

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)
    }
}