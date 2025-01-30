package com.example.apprepaso.Api

import retrofit2.http.GET

interface CatApiService {
    @GET("breeds")
    suspend fun getListadoRazas():List<RazaGato>
}