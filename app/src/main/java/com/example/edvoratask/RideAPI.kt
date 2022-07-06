package com.example.edvoratask

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
interface RideAPI {

    @GET("/rides")
   fun getRides() : Call<List<RideList>>

   @GET("/user")
   fun getUser(): Call<UserList>
}