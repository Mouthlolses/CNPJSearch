package com.cnpj.cnpjsearch.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnpj.cnpjsearch.model.CNPJ
import com.cnpj.cnpjsearch.network.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun searchData(cnpj: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            val data = apiRepository.getData(cnpj)
            _uiState.value = UiState.Success(data)
        } catch (e: Exception) {
            _uiState.value = UiState.Failure(e.localizedMessage ?: "Erro Desconhecido")
        }
    }

    fun clearState() {
        _uiState.value = UiState.Idle
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val data: CNPJ) : UiState()
    data class Failure(val message: String) : UiState()
}