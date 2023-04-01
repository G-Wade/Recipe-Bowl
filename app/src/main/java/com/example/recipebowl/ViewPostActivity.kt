package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.koushikdutta.ion.Ion
import org.json.JSONObject

class ViewPostActivity : AppCompatActivity() {

    private lateinit var title : TextView
    private lateinit var image : ImageView
    private lateinit var description : TextView
    private lateinit var instructions : TextView
    private lateinit var ingredientLayout : LinearLayoutCompat

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

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

        db = FirebaseFirestore.getInstance()

        val info = intent.getStringExtra("info")
        if (info != null) {
            db.collection("Recipe").document(info).get().addOnCompleteListener {
                populatePost(it)
            }
        }
    }

    private fun populatePost(document : Task<DocumentSnapshot>) {
        val docRef = document.result

        title = findViewById(R.id.title)
        title.text = docRef.get("title").toString()
        description = findViewById(R.id.description)
        description.text = docRef.get("description").toString()

        ingredientLayout = findViewById(R.id.ingredient_layout)
        val ingredients = docRef.get("ingredients")

        for (ingredient in ingredients as ArrayList<String>) {
            var value = TextView(this)
            value.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80)
            value.setTextSize(14F)

            Ion.with(this).load("https://world.openfoodfacts.org/api/v0/product/" + ingredient + ".json")
                .setHeader("User-Agent", "Gareth Wade").setHeader("Accept", "application/JSON").asString()
                .setCallback { _, result ->
                    val jsonObj = JSONObject(result)
                    val status = jsonObj.getString("status")

                    if (status.equals("1")) {
                        val product = jsonObj.getJSONObject("product")
                        val id = product.getString("product_name")
                        value.setText(id)
                    } else {
                        value.setText(ingredient)
                    }
                }

            ingredientLayout.addView(value)
        }

        instructions = findViewById(R.id.instructions)
        instructions.text = docRef.get("instructions").toString()
    }
}