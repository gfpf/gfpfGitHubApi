package com.gfpf.github_api.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.gfpf.github_api.data.repository.GithubRepository
import kotlinx.coroutines.Dispatchers

class GHUserViewModel(private val repository: GithubRepository) : ViewModel() {
    /*fun getUser(username: String) = liveData(Dispatchers.IO) {
        val retrievedUser = repository.getUser(username)
        emit(retrievedUser)
    }*/
}

class GHUserViewModelFactory(private val repository: GithubRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GHUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GHUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}