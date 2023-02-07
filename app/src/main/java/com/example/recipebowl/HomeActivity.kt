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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser

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

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        val modelArrayList = populateHome()
        val recyclerView = findViewById<RecyclerView>(R.id.homeRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = HomeAdapter(modelArrayList)
        recyclerView.adapter = adapter


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

    private fun populateHome() : ArrayList<HomeModel> {
        val list = ArrayList<HomeModel>()

        val nameList = arrayOf(R.string.login, R.string.logout, R.string.e_mail_hint, R.string.password_hint, R.string.app_name)

        for (i in 0 .. nameList.size-1) {
            val model = HomeModel()
            model.setName(getString(nameList[i]))
            list.add(model)
        }

        list.sortBy { list -> list.modelName}
        return list
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