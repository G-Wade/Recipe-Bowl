package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.TextViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.profileNav
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

        val username = findViewById<TextView>(R.id.username)
        val docRef = db.collection("User").document(auth.uid.toString())
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                username.text = document.getString("username")
            }
        }
    }
}