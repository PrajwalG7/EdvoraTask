package com.example.edvoratask

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var r: List<RideList>
    private lateinit var result: Call<List<RideList>>
    private lateinit var resultUser: Call<UserList>
    lateinit var  userName:TextView
    private lateinit var RideAPIs:RideAPI
    private lateinit var nearestRides:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userName=findViewById(R.id.IdUserLoggedName)
        nearestRides=findViewById(R.id.nearestRides)

        RideAPIs = RetrofitHelper.getInstance()
                .create(RideAPI::class.java)

        userProfile()
        forNearestRide()
    }
    fun nearestRide(view: View) {
        forNearestRide()
    }

    fun userProfile(){
        //userloggedin
        resultUser=RideAPIs.getUser()
        resultUser.enqueue(object : Callback<UserList> {


            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.d("onFailureUser",t.toString())
            }

            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                userName.text=response.body()?.name
            }

        })
    }

    fun forNearestRide(){
        nearestRides.setPaintFlags(nearestRides.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        //results
        result=RideAPIs.getRides()
        result.enqueue(object : Callback<List<RideList>> {

            override fun onResponse(
                call: Call<List<RideList>>,
                response: Response<List<RideList>>

            ) {
                r = response.body()!!
                Log.d("r", r.toString())
                recyclerView.adapter = MyAdapter(r)
            }

            override fun onFailure(
                call: Call<List<RideList>>,
                t: Throwable
            ) {
                Log.d("onFailure", t.toString())
            }

        })

        recyclerView=findViewById (R.id.idRV)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}