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
    private lateinit var nutritions : TextView
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

        nutritions = findViewById(R.id.nutritions)
        var allergenText = "Allergens:\n"

        val mutableList = mutableListOf<String>()

        for (ingredient in ingredients as ArrayList<String>) {
            var value = TextView(this)
            value.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50)
            value.setTextSize(14F)

            var nutrients = TextView(this)
            nutrients.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            nutrients.setTextSize(12F)

            Ion.with(this).load("https://world.openfoodfacts.org/api/v0/product/" + ingredient + ".json")
                .setHeader("User-Agent", "Gareth Wade").setHeader("Accept", "application/JSON").asString()
                .setCallback { _, result ->
                    val jsonObj = JSONObject(result)
                    val status = jsonObj.getString("status")

                    if (status.equals("1")) {
                        val product = jsonObj.getJSONObject("product")
                        val id = product.getString("product_name")
                        value.setText(id)

                        val nutriments = product.getJSONObject("nutriments")
                        var nValues = ""

                        val carbohydrates = nutriments.optString("carbohydrates")
                        if (carbohydrates != "") {
                            nValues += "carbohydrates in g: " + carbohydrates + "    "
                        }

                        val calories = nutriments.optString("energy-kcal")
                        if (calories != "") {
                            nValues += "calories in kcal: " + calories + "    "
                        }

                        val fat = nutriments.optString("fat")
                        if (fat != "") {
                            nValues += "fat in g: " + fat + "    "
                        }

                        val saturatedFat = nutriments.optString("saturated-fat")
                        if (saturatedFat != "") {
                            nValues += "saturated fat in g: " + saturatedFat + "    "
                        }

                        val fiber = nutriments.optString("fiber")
                        if (fiber != "") {
                            nValues += "fiber in g: " + fiber + "    "
                        }

                        val proteins = nutriments.optString("proteins")
                        if (proteins != "") {
                            nValues += "proteins in g: " + proteins + "    "
                        }

                        val salt = nutriments.optString("salt")
                        if (salt != "") {
                            nValues += "salt in g: " + salt + "    "
                        }

                        val sodium = nutriments.optString("sodium")
                        if (sodium != "") {
                            nValues += "sodium in g: " + sodium + "    "
                        }

                        val sugars = nutriments.optString("sugars")
                        if (sugars != "") {
                            nValues += "sugars in g: " + sugars + "    "
                        }

                        nutrients.setText(nValues)

                        val allergenList = product.getJSONArray("allergens_tags")
                        for (i in 0 .. allergenList.length()-1) {
                            allergenText += allergenList[i].toString() + "\n"
                            nutritions.text = allergenText
                        }

                    } else {
                        value.setText(ingredient)
                        nutrients.setText("No nutritional information found.")
                    }
                }

            ingredientLayout.addView(value)
            ingredientLayout.addView(nutrients)
        }

        /*
        for (i in 0 .. mutableList.size) {
            for (j in i+1 .. mutableList.size) {
                if (mutableList.get(i) == mutableList.get(j)) {
                    mutableList.removeAt(j)
                }
            }

            allergenText += mutableList.get(i) + "\n"
        }
         */

        var notice = TextView(this)
        notice.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        notice.setTextSize(12F)
        notice.setText("\n" + "All values given a per 100g and any missing values are due to a lack of data on the product")

        ingredientLayout.addView(notice)

        instructions = findViewById(R.id.instructions)
        instructions.text = docRef.get("instructions").toString()

    }
}