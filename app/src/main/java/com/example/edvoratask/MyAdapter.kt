package com.example.edvoratask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val mList: List<RideList>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.rideId.setText("Ride Id: "+mList[position].id.toString())
        holder.Origin_Station.setText("Origin Station: "+mList[position].origin_station_code.toString())
        holder.station_path.setText("station_path: "+mList[position].station_path.toString())
        holder.Date.setText("Date: "+mList[position].date)
        holder.CityName.setText("City Name: "+"\n"+mList[position].city)
        holder.StateName.setText("State Name: "+"\n"+mList[position].state)

    }


    override fun getItemCount(): Int {
        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val rideId= itemView.findViewById<TextView>(R.id.idRideId)
        var Origin_Station= itemView.findViewById<TextView>(R.id.idOriginStation)
        val station_path=itemView.findViewById<TextView>(R.id.idStationPath)
        val Date=itemView.findViewById<TextView>(R.id.idDate)
        val Distance=itemView.findViewById<TextView>(R.id.idDistance)
        val CityName=itemView.findViewById<TextView>(R.id.idStateName)
        val StateName=itemView.findViewById<TextView>(R.id.idCityName)

        //Distance = nearest or equal value of station_code in station_path list
    }
}
