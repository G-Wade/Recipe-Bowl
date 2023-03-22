package com.example.recipebowl

import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.core.view.updateMargins

class AddPostActivity : AppCompatActivity() {

    private lateinit var titleText : EditText
    private lateinit var titleImage : ImageView
    private lateinit var descriptionText : EditText
    private lateinit var ingredientText : EditText
    private lateinit var addIngredientBtn : Button
    private lateinit var instructionText : EditText
    private lateinit var finishBtn : Button
    private lateinit var removeBtn : Button
    private lateinit var ingredientLayout : LinearLayoutCompat
    private lateinit var removeIngredientLayout : LinearLayoutCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        val mainToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mainToolbar)

        ingredientLayout = findViewById<LinearLayoutCompat>(R.id.ingredient_layout)
        removeIngredientLayout = findViewById<LinearLayoutCompat>(R.id.remove_btn_layout)

        var ingredients = ArrayDeque<EditText>()

        addIngredientBtn = findViewById(R.id.add_ingredient_btn)
        addIngredientBtn.setOnClickListener{addIngredientRow(ingredients)}
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