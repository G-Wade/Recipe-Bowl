package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    //private val Nutella = "https://world.openfoodfacts.org/api/v0/product/3017620422003.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            val newIntent = Intent(this, LoginActivity::class.java)
            startActivity(newIntent)
            finish()
        }

        val browseFragment = BrowseFragment()
        val profileFragment = ProfileFragment()

        //setCurrentFragment(browseFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                //R.id.browseNav -> setCurrentFragment(browseFragment)
                R.id.profileNav -> setCurrentFragment(profileFragment)
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