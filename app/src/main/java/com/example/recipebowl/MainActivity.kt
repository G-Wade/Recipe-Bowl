package com.example.recipebowl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //authentication variables
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser

    //UI elements
    lateinit var emailText : EditText
    lateinit var passwordText : EditText
    lateinit var loginBtn : Button
    lateinit var regBtn : Button
    lateinit var logoutBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailText = findViewById(R.id.emailText)
        passwordText = findViewById(R.id.passwordText)
        loginBtn = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener({v -> loginClick(v)})

        update()
    }

    //registration action
    private fun registerClick(view: View) {
        if (mAuth.currentUser != null) {
            displayMessage(view, getString(R.string.register_while_logged_in))
        } else {
            mAuth.createUserWithEmailAndPassword(emailText.text.toString(),
                passwordText.text.toString()).addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    closeKeyBoard()
                    update()
                } else {
                    closeKeyBoard()
                    displayMessage(loginBtn, getString(R.string.register_failure))
                }
            }
        }
    }

    //login the user
    fun loginClick(view: View) {
        mAuth.signInWithEmailAndPassword(emailText.text.toString(),
        passwordText.text.toString()).addOnCompleteListener(this) {task ->
            if (task.isSuccessful) {
                closeKeyBoard()
                update()
            } else {
                closeKeyBoard()
                displayMessage(loginBtn, getString(R.string.login_failure))
            }
        }

    }


    //logout the current user
    private fun logoutClick(view: View) {
        currentUser = mAuth.currentUser
        mAuth.signOut()
        update()
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