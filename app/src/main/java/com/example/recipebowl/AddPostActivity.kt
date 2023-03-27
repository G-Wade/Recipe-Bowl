package com.example.recipebowl

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.core.widget.TextViewCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddPostActivity : AppCompatActivity() {

    private lateinit var titleImage : ImageView
    private lateinit var addIngredientBtn : Button
    private lateinit var finishBtn : Button
    private lateinit var removeBtn : Button
    private lateinit var ingredientLayout : LinearLayoutCompat
    private lateinit var removeIngredientLayout : LinearLayoutCompat
    private lateinit var linkText : TextView

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ingredientLayout = findViewById<LinearLayoutCompat>(R.id.ingredient_layout)
        removeIngredientLayout = findViewById<LinearLayoutCompat>(R.id.remove_btn_layout)

        var ingredients = ArrayDeque<EditText>()

        addIngredientBtn = findViewById<Button>(R.id.add_ingredient_btn)
        addIngredientBtn.setOnClickListener{addIngredientRow(ingredients)}

        finishBtn = findViewById<Button>(R.id.finish_post_btn)
        finishBtn.setOnClickListener{postRecipe(ingredients)}

        linkText = findViewById<TextView>(R.id.OFF_text)
        linkText.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun postRecipe(ingredients: ArrayDeque<EditText>) {

        var complete = true
        val titleText = findViewById<EditText>(R.id.edit_title)
        val descriptionText = findViewById<EditText>(R.id.edit_description)
        val instructionText = findViewById<EditText>(R.id.edit_instructions)

        if (titleText.text.isEmpty() || descriptionText.text.isEmpty() || instructionText.text.isEmpty() || ingredients.isEmpty()) {
            complete = false
        }

        for (ingredient in ingredients) {
            if (ingredient.text.isEmpty()) {
                complete = false
            }
        }

        if (complete) {
            val uid = auth.uid
            val title = titleText.text.toString()
            val description = descriptionText.text.toString()
            val instructions = instructionText.text.toString()
            val ingredientArray = Array(ingredients.size) {
                i -> ingredients[i].text.toString()
            }

            val docRef = FirebaseFirestore.getInstance().collection("Recipe")
            val map = hashMapOf("userUID" to uid, "title" to title, "description" to description,
                "instructions" to instructions, "ingredients" to ingredientArray.asList())

            docRef.add(map)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Post Successful", Toast.LENGTH_SHORT).show()
                    val newIntent = Intent(this, MyPostsActivity::class.java)
                    startActivity(newIntent)
                    finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show()
                val newIntent = Intent(this, MyPostsActivity::class.java)
                startActivity(newIntent)
                finish()
            }
        } else {
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addIngredientRow(ingredients : ArrayDeque<EditText>) {

        var ingredient = EditText(this)
        ingredient.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100)
        ingredient.setTextSize(14F)
        ingredient.setHint("Ingredient")

        ingredientLayout.addView(ingredient)

        if (ingredients.isEmpty()) {
            removeBtn = Button(this)
            removeBtn.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100)
            removeBtn.setText("Remove")
            removeBtn.setTextSize(12F)
            removeBtn.setBackgroundColor(0)

            removeBtn.setOnClickListener{removeIngredientRow(ingredients)}

            removeIngredientLayout.addView(removeBtn)
        } else {
            val layoutParams = removeBtn.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(0, 100*ingredients.size, 0, 0)
            removeBtn.layoutParams = layoutParams
        }

        ingredients.add(ingredient)
    }

    private fun removeIngredientRow(ingredients : ArrayDeque<EditText>) {

        val remove = ingredients.last()
        ingredientLayout.removeView(remove)
        ingredients.removeLast()

        if (ingredients.isEmpty()) {
            removeIngredientLayout.removeView(removeBtn)
        } else {
            val layoutParams = removeBtn.layoutParams as LinearLayout.LayoutParams
            layoutParams.setMargins(0, 100*(ingredients.size-1), 0, 0)
            removeBtn.layoutParams = layoutParams
        }

    }
}