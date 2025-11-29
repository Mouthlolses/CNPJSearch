package com.cnpj.cnpjsearch.model

data class Qsa(
    val cnpj_cpf_do_socio: String,
    val codigo_faixa_etaria: Int,
    val codigo_pais: Any,
    val codigo_qualificacao_representante_legal: Int,
    val codigo_qualificacao_socio: Int,
    val cpf_representante_legal: String,
    val data_entrada_sociedade: String,
    val faixa_etaria: String,
    val identificador_de_socio: Int,
    val nome_representante_legal: String,
    val nome_socio: String,
    val pais: Any,
    val qualificacao_representante_legal: String,
    val qualificacao_socio: String
)