package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val newIntent = Intent(this, LoginActivity::class.java)
            startActivity(newIntent)
            finish()
        }

        db = FirebaseFirestore.getInstance()

        db.collection("Recipe").get().addOnCompleteListener {
            if (it.isSuccessful) {
                populateHome(it.result)
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.browseNav
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

        /*
        Ion.with(this).load(Nutella).setHeader("User-Agent", "Gareth Wade")
            .setHeader("Accept", "application/JSON").asString()
            .setCallback { _, result ->
                val jsonObj = JSONObject(result)
                val value = jsonObj.getString("code")
                Button.text = value
            }
         */
    }

    private fun populateHome(collection : QuerySnapshot) {
        val list = ArrayList<HomeModel>()

        for (document in collection) {
            val model = HomeModel()
            model.setName(document.get("title").toString())
            model.setID(document.id)
            list.add(model)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.homeRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = HomeAdapter(list)
        recyclerView.adapter = adapter

        adapter.onItemClick = {
            val newIntent = Intent(this, ViewPostActivity::class.java)
            newIntent.putExtra("info", it.getID())
            startActivity(newIntent)
            finish()
        }
    }
}