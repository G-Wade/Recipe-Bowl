package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class MyPostsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posts)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val uid = auth.uid

        db.collection("Recipe").whereEqualTo("userUID", uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                populateMyPosts(it.result)
            }
        }

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

        val addPostBtn = findViewById<Button>(R.id.add_post_btn)
        addPostBtn.setOnClickListener{v -> addPostClick()}
    }

    private fun addPostClick() {
        val newIntent = Intent(this, AddPostActivity::class.java)
        startActivity(newIntent)
        finish()
    }

    private fun populateMyPosts(collection : QuerySnapshot) {
        val list = ArrayList<HomeModel>()

        for (document in collection) {
            val model = HomeModel()
            model.setName(document.get("title").toString())
            model.setID(document.id)
            list.add(model)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.myPostsRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = HomeAdapter(list)
        recyclerView.adapter = adapter
    }
}