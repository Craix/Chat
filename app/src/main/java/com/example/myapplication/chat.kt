package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.myapplication.dto.Massage
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_chat.*
import okhttp3.ResponseBody
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class chat : AppCompatActivity() {

    var res: MutableList<Massage> = mutableListOf<Massage>();
    lateinit var  recyclerView: RecyclerView;

    public fun getHTPP() {

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


        val call: Call<List<Massage>> = jsonPlaceholderAPI.getPost()
        call.enqueue(object : Callback<List<Massage>> {
            override fun onResponse(
                call: Call<List<Massage>>,
                response: Response<List<Massage>>
            ) {
                if (!response.isSuccessful) {
                    println("######################### Response problem: " + response.code())
                    return
                }

                //get massage bopdy
                res.clear();
                res.addAll(response.body()!!)

                println("######################### Response: " + response.code())

                recyclerView.adapter = CustomAdapter(res);

            }

            override fun onFailure(
                call: Call<List<Massage>>,
                t: Throwable
            ) {
                println("######################### Error :" + t.message)
            }
        })
    }

    public fun postHTTP(massage: Massage) {

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

        val call: Call<ResponseBody> = jsonPlaceholderAPI.sendMsg(massage);

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
        setContentView(R.layout.activity_chat)

        //recycler viewv
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))


        //restore text
        var shar = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        var nameGet = shar.getString("NAME", "")

        //login
        var text = findViewById<TextView>(R.id.loginText);
        text.setText("Login: " + nameGet.toString())


        // navigation drawer
        var nav_view = findViewById<NavigationView>(R.id.main)
        nav_view.setNavigationItemSelectedListener {
                menuItem->
            if(menuItem.itemId == R.id.przycisk1) {
                println("Wyloguj");
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            if(menuItem.itemId == R.id.przycisk2) {
                println("Refresh");
                getHTPP();
            }
            true
        }

        getHTPP();

        //swip to refresh
        var swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.refresh)
        swipeRefresh.setOnRefreshListener(OnRefreshListener {
            getHTPP();
            System.out.println("######################### update");
            swipeRefresh.setRefreshing(false)
        })

        //refresh by time
        val t: Thread = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(1000 * 30.toLong())
                        runOnUiThread {
                            //update
                            getHTPP();
                            System.out.println("######################### update");

                        }
                    }
                } catch (e: InterruptedException) {
                }
            }
        }
        t.start()

        //get masage
        var massageEdit = findViewById<EditText>(R.id.mas);

        //add button to send masage
        bts.setOnClickListener()
        {
            var msg = Massage(nameGet.toString(), massageEdit.text.toString())
            massageEdit.setText("")
            postHTTP(msg);
        }

        //swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as CustomAdapter
                val msg = adapter.remove(viewHolder.adapterPosition) as Massage;

                if(nameGet == msg.login)
                {
                    Toast.makeText(applicationContext, "Delete massage", Toast.LENGTH_SHORT).show()
                    deleteHTTP(msg.id.toString());
                }
                else
                {
                    Toast.makeText(applicationContext, "You can't delete this massage", Toast.LENGTH_SHORT).show()
                }

                getHTPP();

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
