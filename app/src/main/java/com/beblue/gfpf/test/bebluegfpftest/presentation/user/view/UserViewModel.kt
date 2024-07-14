package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHSearchUser
import com.beblue.gfpf.test.bebluegfpftest.data.domain.GHUser
import com.beblue.gfpf.test.bebluegfpftest.data.domain.UserContract
import com.beblue.gfpf.test.bebluegfpftest.data.repository.UserRepository
import com.beblue.gfpf.test.bebluegfpftest.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(), UserContract.UserActionsListener {

    private val mGHUserRepository: UserRepository = userRepository

    private val _allUsers = MutableLiveData<List<GHUser>>()
    val allUsers: LiveData<List<GHUser>>
        get() = _allUsers

    private val _searchResult = MutableLiveData<GHSearchUser?>()
    val searchResult: LiveData<GHSearchUser?>
        get() = _searchResult

    private val _userDetail = MutableLiveData<SingleEvent<GHUser>>()
    val userDetail: LiveData<SingleEvent<GHUser>>
        get() = _userDetail

    override fun loadAllUsers(): LiveData<List<GHUser>> {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mGHUserRepository.loadAllUsers()
            _allUsers.postValue(result)
        }
        return allUsers
    }

    override fun searchUserByName(
        searchTerm: String,
        forceUpdate: Boolean
    ): LiveData<GHSearchUser?> {
        viewModelScope.launch(Dispatchers.IO) {
            if (forceUpdate) {
                mGHUserRepository.refreshData()
            }
            val result = mGHUserRepository.searchUserByName(searchTerm)
            _searchResult.postValue(result)
        }
        return searchResult
    }

    override fun loadUserById(id: Int): LiveData<SingleEvent<GHUser>> {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mGHUserRepository.loadUserById(id)
            _userDetail.postValue(SingleEvent(result))
        }
        return userDetail
    }

    fun clearAllResult() {
        _allUsers.postValue(emptyList())
    }

    fun clearSearchResult() {
        _searchResult.postValue(null)
    }
}