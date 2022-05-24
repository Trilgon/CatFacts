package ru.trilgon.retrofitapp

import retrofit2.Call
import retrofit2.http.GET
import ru.trilgon.retrofitapp.api.Cat

interface ApiRequests {
    @GET("/facts/random")
    fun getCatFact(): Call<Cat>
}