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

class RegisterActivity : AppCompatActivity() {

    //UI elements
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var regBtn : Button
    private lateinit var logoutBtn : Button
    private lateinit var loginText : TextView

    private lateinit var auth : FirebaseAuth

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

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        regBtn = findViewById(R.id.registerBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        loginText = findViewById(R.id.loginLink)

        regBtn.setOnClickListener{v -> registerClick()}
        loginText.setOnClickListener{v -> loginLink()}
    }

    private fun registerClick() {
        val email = emailText.text.toString()
        val password = passwordText.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT).show()
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