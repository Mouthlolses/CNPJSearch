package com.cnpj.cnpjsearch.network

import com.cnpj.cnpjsearch.model.CNPJ
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("cnpj/v1/{cnpj}")
    suspend fun getData(@Path ("cnpj") cnpj: String): CNPJ
}