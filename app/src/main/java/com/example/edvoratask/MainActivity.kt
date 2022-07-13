package com.example.edvoratask

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
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
    lateinit var  filter:TextView
    private lateinit var RideAPIs:RideAPI
    private lateinit var nearestRides:TextView
    private lateinit var upcomingRides:TextView
    private lateinit var pastRides:TextView
    private lateinit var userLoggedProfile:ImageView
    private  lateinit var relativeLayout:RelativeLayout
    var userLoggedStationCode:Int=0
    var distance:Int=0
    lateinit var  spinner:Spinner
    val filteredList: MutableList<RideList> = ArrayList()
    var clicked:Int=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        spinner= Spinner(applicationContext)
        userName=findViewById(R.id.IdUserLoggedName)
        nearestRides=findViewById(R.id.nearestRides)
        upcomingRides=findViewById(R.id.UpcomingRides)
        filter=findViewById(R.id.filter)
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
        filter.setTextColor(Color.parseColor("#c2bebe"))
        nearestRides.setTextColor(Color.parseColor("#FFFFFF"))
        forNearestRide()
    }

    fun upcomingRide(view: View){
        nearestRides.setTextColor(Color.parseColor("#c2bebe"))
        pastRides.setTextColor(Color.parseColor("#c2bebe"))
        filter.setTextColor(Color.parseColor("#c2bebe"))
        upcomingRides.setTextColor(Color.parseColor("#FFFFFF"))
        forNearestRide()
    }

    fun pastRide(view: View){
        nearestRides.setTextColor(Color.parseColor("#c2bebe"))
        upcomingRides.setTextColor(Color.parseColor("#c2bebe"))
        pastRides.setTextColor(Color.parseColor("#FFFFFF"))
        filter.setTextColor(Color.parseColor("#c2bebe"))

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

    fun filter(view: View) {

            relativeLayout=findViewById(R.id.relLayout)

            if(clicked%2==0){
                relativeLayout.visibility=View.GONE
            }
           else{
            filter.setTextColor(Color.parseColor("#FFFFFF"))
            nearestRides.setTextColor(Color.parseColor("#c2bebe"))
            upcomingRides.setTextColor(Color.parseColor("#c2bebe"))
            pastRides.setTextColor(Color.parseColor("#c2bebe"))


            val relativeLayout = findViewById<RelativeLayout>(R.id.relLayout)
            relativeLayout.visibility = View.VISIBLE

            val spinnerState = findViewById<Spinner>(R.id.spin_state)
            val spinnerCity = findViewById<Spinner>(R.id.spin_city)


            val listForStates: MutableList<String> = ArrayList()
            val listForCitiesOfSelectedState: MutableList<String> = ArrayList()

            for (i in re.indices) {
                listForStates.add(re[i].state)
            }

            val adapterState = ArrayAdapter(
                applicationContext,
                android.R.layout.simple_spinner_item,
                listForStates
            )
            spinnerState.adapter = adapterState


            spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    //       Toast.makeText(applicationContext, parentView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()
                    val selectedState = parentView.getItemAtPosition(position)

                    for (i in re.indices) {
                        if (re[i].state == selectedState) {
                            listForCitiesOfSelectedState.add(re[i].city)
                        }
                    }

                    val adapterCity = ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_spinner_item,
                        listForCitiesOfSelectedState
                    )
                    spinnerCity.adapter = adapterCity

                    for (i in listForCitiesOfSelectedState.indices) {
                        for (j in re.indices) {
                            if (re[j].city == listForCitiesOfSelectedState[i]) {
                                filteredList.add(re[j])
                            }

                        }
                    }

                    recyclerView.adapter = MyAdapter(filteredList, distance)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }


        }
        clicked += 1
    }
}
 

