package com.example.edvoratask

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var r: List<RideList>
    val re: MutableList<RideList> = ArrayList()
    private lateinit var result: Call<List<RideList>>
    private lateinit var resultUser: Call<UserList>
    lateinit var  userName:TextView
    private lateinit var RideAPIs:RideAPI
    private lateinit var nearestRides:TextView
    private lateinit var upcomingRides:TextView
    private lateinit var pastRides:TextView
    private lateinit var userLoggedProfile:ImageView
    var userLoggedStationCode:Int=0
    var distance:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        userName=findViewById(R.id.IdUserLoggedName)
        nearestRides=findViewById(R.id.nearestRides)
        upcomingRides=findViewById(R.id.UpcomingRides)
        pastRides=findViewById(R.id.pastRides)
        userLoggedProfile=findViewById(R.id.IdUserLoggedImg)
        nearestRides.setTextColor(Color.parseColor("#FFFFFF"))

        RideAPIs = RetrofitHelper.getInstance()
                .create(RideAPI::class.java)

        userProfile()
        forNearestRide()
    }
    fun nearestRide(view: View) {
        upcomingRides.setTextColor(Color.parseColor("#c2bebe"))
        pastRides.setTextColor(Color.parseColor("#c2bebe"))
        nearestRides.setTextColor(Color.parseColor("#FFFFFF"))
        forNearestRide()
    }

    fun upcomingRide(view: View){
        nearestRides.setTextColor(Color.parseColor("#c2bebe"))
        pastRides.setTextColor(Color.parseColor("#c2bebe"))
        upcomingRides.setTextColor(Color.parseColor("#FFFFFF"))
        forNearestRide()
    }

    fun pastRide(view: View){
        nearestRides.setTextColor(Color.parseColor("#c2bebe"))
        upcomingRides.setTextColor(Color.parseColor("#c2bebe"))
        pastRides.setTextColor(Color.parseColor("#FFFFFF"))

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
                userLoggedStationCode= response.body()?.station_code!!
                Glide.with(applicationContext)
                    .load(response.body()!!.url)
                    .into(userLoggedProfile)

            }

        })
    }

    fun forNearestRide(){
        //results
        result=RideAPIs.getRides()
        result.enqueue(object : Callback<List<RideList>> {

            override fun onResponse(
                call: Call<List<RideList>>,
                response: Response<List<RideList>>

            ) {
                r = response.body()!!

                //when getting response r as List<RideList> do check if userLoggedStationCode is present in each station path and if present
                // add that index of list r to re as List<RideList> and send it to MyAdapter

                val listToCheckInCodes: MutableList<Int> = ArrayList()

                for (i in r.indices) {
                   for(j in r[i].station_path!! ){
                       listToCheckInCodes.add(j)
                   }
                    for(iterate in listToCheckInCodes){
                        if(userLoggedStationCode==iterate){
                            re.add(r[i])
                        }
                    }
                    listToCheckInCodes.clear()
                }
              recyclerView.adapter = MyAdapter(re,distance)
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


