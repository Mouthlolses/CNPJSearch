package com.cnpj.cnpjsearch.model

data class RegimeTributario(
    val ano: Int,
    val cnpj_da_scp: Any,
    val forma_de_tributacao: String,
    val quantidade_de_escrituracoes: Int
)