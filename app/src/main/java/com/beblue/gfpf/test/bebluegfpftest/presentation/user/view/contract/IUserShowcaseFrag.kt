package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract

import androidx.lifecycle.LiveData
import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHUser
import com.gfpf.github_api.util.SingleEvent

interface IUserShowcaseFrag {

    interface View {
        fun setProgressIndicator(isActive: Boolean)
        fun showToastMessage(message: String)
        fun showUserListUI(users: List<GHUser>?, isAppend: Boolean)
        fun showUserDetailUI(requestedUser: GHUser)
    }

    interface ActionListener {
        fun searchUserByName(searchTerm: String, forceUpdate: Boolean): LiveData<GHSearchUser?>
        fun loadAllUsers(): LiveData<List<GHUser>>
        fun loadUserById(id: Int): LiveData<SingleEvent<GHUser?>>
        //fun loadUserRepos(username: String): LiveData<List<GHRepository>>
        //fun loadRepoTags(owner: String, repo: String): LiveData<List<GHTag>>
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
