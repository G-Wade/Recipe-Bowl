package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.koushikdutta.ion.Ion
import org.json.JSONObject
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var db : FirebaseFirestore

    //private val Nutella = "https://world.openfoodfacts.org/api/v0/product/3017620422003.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val profileFragment = ProfileFragment()

        setCurrentFragment(profileFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.profileNav -> setCurrentFragment(profileFragment)
            }
            true
        }

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val newIntent = Intent(this, LoginActivity::class.java)
            startActivity(newIntent)
            finish()
        }

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        db = FirebaseFirestore.getInstance()

        db.collection("Recipe").get().addOnCompleteListener {
            if (it.isSuccessful) {
                 populateHome(it.result)
            }
        }

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

    private fun setCurrentFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            Log.d("Success", "Success adding document")
            commit()
        }
    }

    private fun populateHome(collection : QuerySnapshot) {
        val list = ArrayList<HomeModel>()
        val nameList = ArrayList<String>()

        for (document in collection) {
            nameList.add(document.get("name").toString())
        }

        for (i in 0 .. nameList.size-1) {
            val model = HomeModel()
            model.setName(nameList[i])
            list.add(model)
        }

        list.sortBy { list -> list.modelName}

        val recyclerView = findViewById<RecyclerView>(R.id.homeRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = HomeAdapter(list)
        recyclerView.adapter = adapter
    }

    /*
    private fun dataTest() {
        val dataText1 = findViewById<EditText>(R.id.edittext1)
        val dataText2 = findViewById<EditText>(R.id.edittext2)
        val value1 = dataText1.text.toString()
        val value2 = dataText2.text.toString()

        if (value1.isEmpty() || value2.isEmpty()) { return }

        val map = hashMapOf("first" to value1, "second" to value2)

        val docRef = FirebaseFirestore.getInstance().collection("Users")

        docRef.add(map)
            .addOnSuccessListener { documentReference ->
                Log.d("Success", "Success adding document")
            }
            .addOnFailureListener { e ->
                Log.w("Failure", "Error adding document", e)
            }
    }
     */
}