package com.example.edvoratask

data class RideList(
    var id:Int,
    var origin_station_code:Int,
    var station_path:List<Int>?,
    var destination_station_code:Int,
    var date:String,
    var map_url:String,
    var state:String,
    var city:String


)
