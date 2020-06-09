package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.dto.Massage
import kotlinx.android.synthetic.main.activity_edit.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class edit : AppCompatActivity() {

    public  fun sendHTTP(id: String, massage: Massage) {

        if(!isNetworkAvailable())
        {
            Toast.makeText(applicationContext, "No internet conection", Toast.LENGTH_SHORT).show()
            return;
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tgryl.pl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceholderAPI: JsonPlaceholderAPI = retrofit.create<JsonPlaceholderAPI>(
            JsonPlaceholderAPI::class.java
        )

        val call: Call<ResponseBody> = jsonPlaceholderAPI.sendPost(id, massage);

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    println("######################### Response code" + response.code())
                    return
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("######################### Error :" + t.message)
            }
        })
    }

    public fun deleteHTTP(id: String) {

        if(!isNetworkAvailable())
        {
            Toast.makeText(applicationContext, "No internet conection", Toast.LENGTH_SHORT).show()
            return;
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tgryl.pl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val jsonPlaceholderAPI: JsonPlaceholderAPI = retrofit.create<JsonPlaceholderAPI>(
            JsonPlaceholderAPI::class.java
        )

        val call: Call<ResponseBody> = jsonPlaceholderAPI.sendMsg(id);

        call.enqueue(object: Callback<ResponseBody>
        {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("######################### Response code" + response.code())
                return
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("######################### Error :" + t.message)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var shar = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        var text = shar.getString("TEXT", "test 5g")
        var login = shar.getString("LOGIN", "test 5g")
        var id = shar.getString("ID", "test 5g")
        var status = shar.getBoolean("STATUS", false)

        System.out.println("######################### Shared prefenrce to edit: ")
        System.out.println(id + " " + login + " " + text);


        var editable = findViewById<EditText>(R.id.e);
        editable.setText(text.toString())

        if(!status)
        {
            t.setText("View massage")
            b1.visibility = View.GONE;
            b3.visibility = View.GONE;
        }
        else
        {
            t.setText("Edit massage")
            b1.visibility = View.VISIBLE;
            b3.visibility = View.VISIBLE;
        }

        b1.setOnClickListener()
        {
            System.out.println("######################### send");

            var msg = Massage(login.toString(), editable.text.toString());
            sendHTTP(id.toString(), msg)

            val intent = Intent(this, chat::class.java)
            startActivity(intent)
        }

        b2.setOnClickListener()
        {
            System.out.println("######################### back");
            val intent = Intent(this, chat::class.java)
            startActivity(intent)
        }

        b3.setOnClickListener()
        {
            System.out.println("######################### delete");

            deleteHTTP(id.toString())

            val intent = Intent(this, chat::class.java)
            startActivity(intent)
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
