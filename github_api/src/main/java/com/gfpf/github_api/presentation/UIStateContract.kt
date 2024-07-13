package com.gfpf.github_api.presentation

sealed class UIState {
    object Loading : UIState()
    data class Success(val data: Any) : UIState()
    data class Error(val message: String) : UIState()
}
