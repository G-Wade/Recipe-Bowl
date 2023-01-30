package com.example.recipebowl

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.koushikdutta.ion.Ion
import org.json.JSONObject
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser : FirebaseUser

    private val db = Firebase.firestore
    private val docRef = FirebaseFirestore.getInstance().document("Users/Recipe")

    private val Nutella = "https://world.openfoodfacts.org/api/v0/product/3017620422003.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        currentUser = mAuth.currentUser

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

        val databut = findViewById<Button>(R.id.editbutton)
        databut.setOnClickListener{v -> dataTest(v)}

        val test = findViewById<TextView>(R.id.texttest)
        test.text = currentUser?.uid
    }

    private fun dataTest(view : View) {
        val dataText1 = findViewById<EditText>(R.id.edittext1)
        val dataText2 = findViewById<EditText>(R.id.edittext2)
        val value1 = dataText1.text.toString()
        val value2 = dataText2.text.toString()

        if (value1.isEmpty() || value2.isEmpty()) { return }

        val map = hashMapOf("first" to value1, "second" to value2)

        docRef.set(map)
            .addOnSuccessListener { documentReference ->
                Log.d("Success", "Success adding document")
            }
            .addOnFailureListener { e ->
                Log.w("Failure", "Error adding document", e)
            }
    }
}