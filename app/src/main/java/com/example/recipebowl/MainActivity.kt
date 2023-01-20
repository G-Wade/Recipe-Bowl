package com.example.recipebowl

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //authentication variables
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser

    //UI elements
    private lateinit var emailText : EditText
    private lateinit var passwordText : EditText
    private lateinit var loginBtn : Button
    private lateinit var regBtn : Button
    private lateinit var logoutBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        loginBtn = findViewById(R.id.loginBtn)
        regBtn = findViewById(R.id.registerBtn)
        logoutBtn = findViewById(R.id.logoutBtn)

        loginBtn.setOnClickListener{v -> loginClick(v)}
        regBtn.setOnClickListener{v -> registerClick(v)}
        logoutBtn.setOnClickListener{v -> logoutClick(v)}

        update()
    }

    //registration action
    private fun registerClick(view: View) {
        try {
            if (mAuth.currentUser != null) {
                displayMessage(view, getString(R.string.register_while_logged_in))
            } else {
                mAuth.createUserWithEmailAndPassword(emailText.text.toString(),
                    passwordText.text.toString()).addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        closeKeyBoard()
                        try {
                            val newIntent = Intent(this, HomeActivity::class.java)
                            startActivity(newIntent)
                        } catch (e : java.lang.Exception) {
                            Log.i("Activities", "Null")
                        }
                    } else {
                        closeKeyBoard()
                        displayMessage(view, getString(R.string.register_failure))
                    }
                }
            }
        } catch (e : java.lang.IllegalArgumentException) {
            displayMessage(view, getString(R.string.empty_field))
            closeKeyBoard()
        }

    }

    //login the user
    private fun loginClick(view: View) {
        try {
            mAuth.signInWithEmailAndPassword(emailText.text.toString(),
                passwordText.text.toString()).addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    closeKeyBoard()
                    displayMessage(view, getString(R.string.login_success))
                    try {
                        val newIntent = Intent(this, HomeActivity::class.java)
                        startActivity(newIntent)
                    } catch (e : java.lang.Exception) {
                        Log.i("Activities", "Null")
                    }
                } else {
                    closeKeyBoard()
                    displayMessage(view, getString(R.string.login_failure))
                }
            }
        } catch (e : java.lang.IllegalArgumentException) {
            displayMessage(view, getString(R.string.empty_field))
            closeKeyBoard()
        }
    }


    //logout the current user
    private fun logoutClick(view: View) {
        currentUser = mAuth.currentUser
        mAuth.signOut()
        closeKeyBoard()
        displayMessage(view, getString(R.string.logout_success))
    }

    //sign out on application stopping
    override fun onStop() {
        super.onStop()
        currentUser = mAuth.currentUser
        mAuth.signOut()
    }

    //update the current user
    private fun update() {
        currentUser = mAuth.currentUser

        //null safe check
        val currentEmail = currentUser?.email
        val greetingSpace = findViewById<TextView>(R.id.greetingView)

        if (currentEmail == null) {
            greetingSpace.text = getString(R.string.not_logged_in)
        } else {
            greetingSpace.text = getString(R.string.logged_in)
        }
    }

    //snack-bar helper
    private fun displayMessage(view : View, msgTxt : String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    //close keyboard helper
    private fun closeKeyBoard() {
        val view = this.currentFocus

        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}