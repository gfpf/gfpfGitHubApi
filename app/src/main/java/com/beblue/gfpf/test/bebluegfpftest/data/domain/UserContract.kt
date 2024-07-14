package com.beblue.gfpf.test.bebluegfpftest.data.domain

import androidx.lifecycle.LiveData
import com.beblue.gfpf.test.bebluegfpftest.util.SingleEvent

interface UserContract {

    interface View {
        fun setProgressIndicator(active: Boolean)
        fun showToastMessage(message: String)
        fun showGHUserListUI(users: List<GHUser>, isAppend: Boolean)
        fun showGHUserDetailUI(requestedUser: GHUser)
    }

    interface UserActionsListener {
        fun searchUserByName(searchTerm: String, forceUpdate: Boolean): LiveData<GHSearchUser?>
        fun loadAllUsers(): LiveData<List<GHUser>>
        fun loadUserById(id: Int): LiveData<SingleEvent<GHUser>>
    }
}
