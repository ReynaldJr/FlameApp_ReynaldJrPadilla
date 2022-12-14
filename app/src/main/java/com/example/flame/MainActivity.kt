package com.example.flame

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import render.animations.*
import render.animations.Attention.Shake

class MainActivity : AppCompatActivity() {
    private lateinit var name1List: CharArray
    private lateinit var name2List: CharArray
    private var bothNames: MutableList<Char> = ArrayList()
    private val render = Render(this)
    private var name1Input: TextInputEditText? = null
    private var name2Input: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val resetButton = findViewById<Button>(R.id.resetButton)
        name1Input = findViewById(R.id.name1)
        name2Input = findViewById(R.id.name2)

        resetButton.isClickable = false

        // Display entry pop up
        Handler().postDelayed({
            displayPopup()
        }, 800)

        // Display pop up on info button
        info_button.setOnClickListener {
            info_button.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_click))
            displayPopup()
        }
    }

    // Function to display pop up
    private fun displayPopup() {
        var popupScreen = Dialog(this)
        popupScreen.setCancelable(false)

        popupScreen.setContentView(R.layout.popup)
        popupScreen.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var okButton = popupScreen.findViewById<Button>(R.id.ok_button)
        okButton.setOnClickListener {
            popupScreen.dismiss()
        }

        popupScreen.show()
    }


    // Calculating
    fun calculateRelationship(view: View?) {
        bothNames.clear()

        val name1 = name1Input!!.text.toString()
        val name2 = name2Input!!.text.toString()

        // Checks if either of the inputs are empty
        if(TextUtils.isEmpty(name1.replace(" ", "")) || TextUtils.isEmpty(name2.replace(" ", ""))) {
            Toast.makeText(this, "You need to have an input in all fields", Toast.LENGTH_SHORT).show()
            render.setAnimation(Shake(name1Input))
            render.start()
            render.setAnimation(Shake(name1Layout))
            render.start()
            render.setAnimation(Shake(name2Input))
            render.start()
            render.setAnimation(Shake(name2Layout))
            render.start()
            return
        }

        // Checks if both inputs are the same
        if (name1.equals(name2, ignoreCase = true)) {
            Toast.makeText(this, "Both fields have the same value, please change one", Toast.LENGTH_SHORT).show()
            render.setAnimation(Shake(name1Input))
            render.start()
            render.setAnimation(Shake(name1Layout))
            render.start()
            render.setAnimation(Shake(name2Input))
            render.start()
            render.setAnimation(Shake(name2Layout))
            render.start()
            return
        }

        // Disabling calculate button
        calculateButton.isClickable = false
        calculateButton.alpha = 0.5f

        // Enabling reset button
        resetButton.isClickable = true
        resetButton.alpha = 1f

        // Disabling Inputs
        disableInput(name1Input!!)
        disableInput(name2Input!!)

        // Putting both names into lower case and a list of characters
        name1List = name1.lowercase().toCharArray()
        name2List = name2.lowercase().toCharArray()


        // If the characters are the same then they will be replaced with a space
        for(item in name1List.indices) {
            for(item2 in name2List.indices) {
                if (name1List[item] == name2List[item2]) {
                    name1List[item] = ' '
                    name2List[item2] = ' '
                    break
                }
            }
        }


        // Adds both names to the list without spaces
        for (item in name1List) {
            if (item != ' ') {
                bothNames.add(item)
            }
        }

        for (item in name2List) {
            if (item != ' ') {
                bothNames.add(item)
            }
        }

        Log.i("LIST OUTPUT", bothNames.toString())

        val bothNamesLength = bothNames.size
        var flameString = "FLAME"
        var splitFlame: String
        var finalFlameRelationship = 0.toChar()

        // Calculating the FLAME relationship
        if(bothNamesLength > 0) {
            while (flameString.length != 1) {
                Log.i("FLAME OUTPUT", flameString.toString())
                val splitIndex = bothNamesLength % flameString.length
                splitFlame = if(splitIndex != 0 ) {
                     flameString.substring(0, splitIndex - 1) + flameString.substring(splitIndex)
                } else {
                    flameString.substring(0, flameString.length - 1)
                }

                flameString = splitFlame

            }

            finalFlameRelationship = flameString[0]
            Log.i("FINAL FLAME", finalFlameRelationship.toString())
        }

        relationshipResult(finalFlameRelationship)
    }

    // Resets the result of inputs on reset button
    fun resetResults(view: View?) {
        val name1Input = findViewById<TextInputEditText>(R.id.name1)
        val name2Input = findViewById<TextInputEditText>(R.id.name2)

        // Disabling reset button
        resetButton.isClickable = false
        resetButton.alpha = 0.5f

        // Enabling calc button
        calculateButton.isClickable = true
        calculateButton.alpha = 1f

        // Enabling inputs
        enableInput(name1Input!!)
        enableInput(name2Input!!)

        // Clearing input
        name1Input.text?.clear()
        name2Input.text?.clear()

        // Setting background
        backgroundChange.setImageResource(R.drawable.main_bg_final)
        emoji1.setImageResource(R.drawable.natural_quiet_silent_emoji)
        emoji2.setImageResource(R.drawable.natural_quiet_silent_emoji)

        // Reset Circles
        circle1.setImageResource(R.drawable.circle_main)
        circle2.setImageResource(R.drawable.circle_main)

        // Reset Title
        titleFlame.text = Html.fromHtml("<font color = \"BLACK\">FLAME</font>")

        render.setAnimation(Zoom.Out(namesText))
        render.start()
        render.setAnimation(Zoom.Out(resultText))
        render.start()
        render.setAnimation(Fade.In(backgroundChange))
        render.start()
        render.setAnimation(Bounce.In(emoji1))
        render.start()
        render.setAnimation(Bounce.In(emoji2))
        render.start()

    }

    // Function to display the result of relationships
    private fun relationshipResult(relationshipResult: Char) {

        when(relationshipResult) {
            'F' -> {
                titleFlame.text = Html.fromHtml("<font color = \"#379237\">F</font>LAME")

                resultText.text = "FRIENDS"
                resultText.setTextColor(Color.parseColor("#379237"))

                backgroundChange.setImageResource(R.drawable.friends_bg)

                circle1.setImageResource(R.drawable.circle_friends)
                circle2.setImageResource(R.drawable.circle_friends)

                emoji1.setImageResource(R.drawable.smile_happy_cool_emoji)
                emoji2.setImageResource(R.drawable.surprise_wonderful_excited_emoji)
            }

            'L' -> {
                titleFlame.text = Html.fromHtml("F<font color = \"#8B1A32\">L</font>AME")

                resultText.text = "LOVERS"
                resultText.setTextColor(Color.parseColor("#8B1A32"))

                backgroundChange.setImageResource(R.drawable.love_bg)

                circle1.setImageResource(R.drawable.circle_love)
                circle2.setImageResource(R.drawable.circle_love)

                emoji1.setImageResource(R.drawable.love_heart_greatful_emoji)
                emoji2.setImageResource(R.drawable.love_heart_greatful_emoji)
            }

            'A' -> {
                titleFlame.text = Html.fromHtml("FL<font color = \"#47497E\">A</font>ME")

                resultText.text = "AFFECTIONATE"
                resultText.setTextColor(Color.parseColor("#47497E"))

                backgroundChange.setImageResource(R.drawable.affection_bg)

                circle1.setImageResource(R.drawable.circle_affection)
                circle2.setImageResource(R.drawable.circle_affection)

                emoji1.setImageResource(R.drawable.eat_full_satisfy_emoji)
                emoji2.setImageResource(R.drawable.smile_happy_cool_emoji)
            }

            'M' -> {
                titleFlame.text = Html.fromHtml("FLA<font color = \"#7E6258\">M</font>E")

                resultText.text = "MARRIED"
                resultText.setTextColor(Color.parseColor("#7E6258"))

                backgroundChange.setImageResource(R.drawable.marriage_bg)

                circle1.setImageResource(R.drawable.circle_marriage)
                circle2.setImageResource(R.drawable.circle_marriage)

                emoji1.setImageResource(R.drawable.kiss_love_heart_emoji)
                emoji2.setImageResource(R.drawable.kiss_love_heart_emoji)

            }

            'E' -> {
                titleFlame.text = Html.fromHtml("FLAM<font color = \"#40434D\">E</font>")

                resultText.text = "ENEMIES"
                resultText.setTextColor(Color.parseColor("#40434D"))

                backgroundChange.setImageResource(R.drawable.enemies_bg_final)

                circle1.setImageResource(R.drawable.circle_enemy)
                circle2.setImageResource(R.drawable.circle_enemy)

                emoji1.setImageResource(R.drawable.angry_bad_emotional_emoji)
                emoji2.setImageResource(R.drawable.devil_bad_evil_emoji)

            }
        }

        namesText.text = "${name1Input?.text.toString()} and ${name2Input?.text.toString()} will be"

        // Animation
        render.setAnimation(Fade.In(backgroundChange))
        render.start()
        render.setAnimation(Fade.InUp(emoji1))
        render.start()
        render.setAnimation(Fade.InUp(emoji2))
        render.start()
        render.setAnimation(Zoom.In(namesText))
        render.start()
        render.setAnimation(Zoom.In(resultText))
        render.start()
    }

    // Function to disable both inputs
    private fun disableInput(editText: EditText) {
        editText!!.isFocusable = false
        editText!!.isEnabled = false
        editText!!.isCursorVisible = false
        editText!!.setBackgroundResource(R.drawable.rounded_edit_text_disabled)
        editText!!.setTextColor(Color.parseColor("#b0b0b0"))

    }

    // Function to enable both inputs
    private fun enableInput(editText: EditText) {
        editText!!.isFocusableInTouchMode = true
        editText!!.isEnabled = true
        editText!!.isCursorVisible = true
        editText!!.setBackgroundResource(R.drawable.rounded_edit_text)
        editText!!.setTextColor(Color.BLACK)
    }
}


