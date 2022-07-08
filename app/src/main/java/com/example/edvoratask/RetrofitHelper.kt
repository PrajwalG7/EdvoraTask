package com.example.edvoratask

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    const val baseUrl = "https://assessment.api.vweb.app"

  fun getInstance(): Retrofit {
          return Retrofit.Builder().baseUrl(baseUrl)
              .addConverterFactory(GsonConverterFactory.create())
              .build()

    }
}