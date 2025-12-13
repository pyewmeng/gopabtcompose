package com.colvecs.gopabtcompose

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value

class settingsActivity: ComponentActivity() {
    private lateinit var stopvalue: EditText
    private lateinit var forwardvalue: EditText
    private lateinit var backwardvalue: EditText
    private lateinit var leftvalue: EditText
    private lateinit var rightvalue: EditText
    private lateinit var ultrasoundvalue: EditText
    private lateinit var infraredvalue: EditText
    private lateinit var musicvalue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        stopvalue = findViewById(R.id.stopValue)
        forwardvalue = findViewById(R.id.forwardValue)
        backwardvalue = findViewById(R.id.backwardValue)
        leftvalue = findViewById(R.id.leftValue)
        rightvalue = findViewById(R.id.rightValue)
        ultrasoundvalue = findViewById(R.id.ultrasoundValue)
        infraredvalue = findViewById(R.id.infraredValue)
        musicvalue = findViewById(R.id.musicvalue)

        var mControlLedSaveSettings = findViewById<Button>(R.id.saveSettings)
        var mControlLedCancelSettings = findViewById<Button>(R.id.cancelSettings)


        mControlLedSaveSettings.setOnClickListener { savesettings() }
        mControlLedCancelSettings.setOnClickListener { cancelsettings() }

    }

    override fun onResume() {
        super.onResume()
        // Fetching the stored data from the SharedPreference
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val s1 = sh.getInt("stopvalue", 0)
        val s2 = sh.getInt("forwardvalue", 1)
        val s3 = sh.getInt("backwardvalue", 2)
        val s4 = sh.getInt("leftvalue", 3)
        val s5 = sh.getInt("rightvalue", 4)
        val s6 = sh.getInt("ultrasoundvalue", 7)
        val s7 = sh.getInt("infraredvalue", 8)
        val s8 = sh.getInt("musicvalue", 9)

        // Setting the fetched data in the EditTexts
        stopvalue.setText(s1.toString())
        forwardvalue.setText(s2.toString())
        backwardvalue.setText(s3.toString())
        leftvalue.setText(s4.toString())
        rightvalue.setText(s5.toString())
        ultrasoundvalue.setText(s6.toString())
        infraredvalue.setText(s7.toString())
        musicvalue.setText(s8.toString())

    }

    override fun onPause() {
        super.onPause()
        // Storing data into SharedPreferences
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        // Creating an Editor object to edit(write to the file)
        val myEdit = sharedPreferences.edit()

        // Storing the key and its value as the data fetched from edittext
        myEdit.putInt("stopvalue", stopvalue.text.toString().toInt())
        myEdit.putInt("forwardvalue", forwardvalue.text.toString().toInt())
        myEdit.putInt("backwardvalue", backwardvalue.text.toString().toInt())
        myEdit.putInt("leftvalue", leftvalue.text.toString().toInt())
        myEdit.putInt("rightvalue", rightvalue.text.toString().toInt())
        myEdit.putInt("ultrasoundvalue", ultrasoundvalue.text.toString().toInt())
        myEdit.putInt("infraredvalue", infraredvalue.text.toString().toInt())
        myEdit.putInt("musicvalue", musicvalue.text.toString().toInt())


    }

    private fun savesettings(){
        // Storing data into SharedPreferences
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        // Creating an Editor object to edit(write to the file)
        val myEdit = sharedPreferences.edit()

        // Storing the key and its value as the data fetched from edittext
        myEdit.putInt("stopvalue", stopvalue.text.toString().toInt())
        myEdit.putInt("forwardvalue", forwardvalue.text.toString().toInt())
        myEdit.putInt("backwardvalue", backwardvalue.text.toString().toInt())
        myEdit.putInt("leftvalue", leftvalue.text.toString().toInt())
        myEdit.putInt("rightvalue", rightvalue.text.toString().toInt())
        myEdit.putInt("ultrasoundvalue", ultrasoundvalue.text.toString().toInt())
        myEdit.putInt("infraredvalue", infraredvalue.text.toString().toInt())
        myEdit.putInt("musicvalue", musicvalue.text.toString().toInt())

        // Once the changes have been made, we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
        finish()
    }

    private fun cancelsettings(){

        finish()
    }

}