package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var name: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var editText = findViewById<EditText>(R.id.editText);

        //set name as empty
        name = "";

        //try to laod
        load();

        //set loaded or null name
        editText.setText(name)

        button.setOnClickListener()
        {

            //get new name
            name = editText.text.toString()

            if(name != "") {
                //save
                save();

                //to start new activity
                val intent = Intent(this, chat::class.java)
                startActivity(intent)
            }
        }
    }

    fun load()
    {
        var shar = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        name = shar.getString("NAME", "").toString()
    }

    fun save()
    {
        var shar = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        val editor = shar.edit();
        editor.putString("NAME", name.trim());
        editor.apply()
    }
}
