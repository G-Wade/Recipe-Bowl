package com.example.recipebowl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar

class LoginActivity : AppCompatActivity() {

    //UI elements
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var loginBtn : Button
    private lateinit var logoutBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        loginBtn = findViewById(R.id.loginBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        loginBtn.setOnClickListener{v -> loginClick(v)}
    }

    private fun loginClick(view : View) {

    }
}