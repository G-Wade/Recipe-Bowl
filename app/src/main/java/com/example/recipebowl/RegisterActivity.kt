package com.example.recipebowl

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    //UI elements
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var usernameText : EditText
    private lateinit var regBtn : Button
    private lateinit var logoutBtn : Button
    private lateinit var loginText : TextView

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val newIntent = Intent(this, HomeActivity::class.java)
            startActivity(newIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        usernameText = findViewById(R.id.usernameText)
        regBtn = findViewById(R.id.registerBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        loginText = findViewById(R.id.loginLink)

        regBtn.setOnClickListener{v -> registerClick()}
        loginText.setOnClickListener{v -> loginLink()}
    }

    private fun registerClick() {
        val email = emailText.text.toString()
        val password = passwordText.text.toString()
        val username = usernameText.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT).show()

                val map = hashMapOf("e-mail" to email, "username" to username)
                val uid = auth.uid.toString()
                val docRef = db.collection("User").document(uid)

                docRef.set(map)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Success", "Success adding document")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Failure", "Error adding document", e)
                    }

                val newIntent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(newIntent)
                finish()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginLink() {
        val newIntent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(newIntent)
        finish()
    }
}