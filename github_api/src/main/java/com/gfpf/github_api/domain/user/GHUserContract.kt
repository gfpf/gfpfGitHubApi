package com.gfpf.github_api.domain.user

import androidx.lifecycle.LiveData
import com.gfpf.github_api.util.SingleEvent

interface GHUserContract {

    interface View {
        fun setProgressIndicator(isActive: Boolean)
        fun showToastMessage(message: String)
        fun showGHUserListUI(users: List<GHUser>?, isAppend: Boolean)
        fun showGHUserDetailUI(requestedUser: GHUser)
    }

    interface UserActionsListener {
        fun searchUserByName(searchTerm: String, forceUpdate: Boolean): LiveData<GHSearchUser?>
        fun loadAllUsers(): LiveData<List<GHUser>>
        fun loadUserById(id: Int): LiveData<SingleEvent<GHUser>>
    }
}
