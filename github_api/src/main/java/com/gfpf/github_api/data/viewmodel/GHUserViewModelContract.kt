package com.gfpf.github_api.data.viewmodel

import androidx.lifecycle.LiveData
import com.gfpf.github_api.domain.user.GHUser
import com.gfpf.github_api.presentation.UIState

interface GHUserViewModelContract {
    fun getAllUsers()
    fun searchUserByUsername(username: String)
    fun getUserById(id: Int)
    fun updateUser(user: GHUser)
    fun deleteUser(id: Int)

    fun getUsersLiveData(): LiveData<List<GHUser>>
    fun getUserLiveData(): LiveData<GHUser?>
    fun getUIStateLiveData(): LiveData<UIState>
}
