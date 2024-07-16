package com.gfpf.github_api.domain.user

import androidx.lifecycle.LiveData
import com.gfpf.github_api.util.SingleEvent

interface GHUserContract {

    //TODO GFPF - MOVE WHOLE FILE TO APP MODULE - WRONG PLACE
    interface View {
        fun setProgressIndicator(isActive: Boolean)
        fun showToastMessage(message: String)
        fun showGHUserListUI(users: List<GHUser>?, isAppend: Boolean)
        fun showGHUserDetailUI(requestedUser: GHUser)
    }

    interface UserActionsListener {
        fun searchUserByName(searchTerm: String, forceUpdate: Boolean): LiveData<GHSearchUser?>
        fun loadAllUsers(): LiveData<List<GHUser>>
        fun loadUserById(id: Int): LiveData<SingleEvent<GHUser?>>
        fun loadUserRepos(username: String): LiveData<List<GHRepository>>
        fun loadRepoTags(owner: String, repo: String): LiveData<List<GHTag>>
    }

    //TODO GFPF - Should I make these methods suspend to avoid spare code?
    // The line below does not need/uses the method return LiveData<GHSearchUser?>
    // mGHUserViewModel.searchUserByName(searchTerm, false)
    /*interface UserActionsListener {
        suspend fun searchUserByName(searchTerm: String, forceUpdate: Boolean): GHSearchUser?
        suspend fun loadAllUsers(): List<GHUser>
        suspend fun loadUserById(id: Int): SingleEvent<GHUser>
    }*/
}
