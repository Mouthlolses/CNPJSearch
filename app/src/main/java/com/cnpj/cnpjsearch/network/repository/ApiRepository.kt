package com.cnpj.cnpjsearch.network.repository

import com.cnpj.cnpjsearch.model.CNPJ
import com.cnpj.cnpjsearch.network.ApiService

class ApiRepository(
    private val api: ApiService
) {
    suspend fun getData(cnpj: String): CNPJ {
        return api.getData(cnpj)
    }
}