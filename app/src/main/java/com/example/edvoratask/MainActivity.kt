package com.example.edvoratask

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
     lateinit var result: Call<List<RideList>>
     lateinit var resultUser: Call<UserList>
     lateinit var  userName:TextView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            userName=findViewById(R.id.IdUserLoggedName)
    }

     fun newestRide(view: View) {

        val RideAPI = RetrofitHelper.getInstance()
            .create(RideAPI::class.java)

         //results
         result=RideAPI.getRides()
                result.enqueue(object : Callback<List<RideList>> {

                    override fun onResponse(
                        call: Call<List<RideList>>,
                        response: Response<List<RideList>>

                    ) {
                       Log.d("onResponse", response.body().toString())
                        r = response.body()!!

                    }

                    override fun onFailure(
                        call: Call<List<RideList>>,
                        t: Throwable
                    ) {
                        Log.d("onFailure", t.toString())
                    }

                })

         //userloggedin
         resultUser=RideAPI.getUser()
         resultUser.enqueue(object : Callback<UserList> {


             override fun onFailure(call: Call<UserList>, t: Throwable) {
                 Log.d("onFailureUser",t.toString())
             }

             override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                 userName.text=response.body()?.name
             }

         })

         recyclerView=findViewById (R.id.idRV)
         recyclerView.layoutManager = LinearLayoutManager(this)

         if(::r.isInitialized) {
          recyclerView.adapter = MyAdapter(r)
      }

    }
}