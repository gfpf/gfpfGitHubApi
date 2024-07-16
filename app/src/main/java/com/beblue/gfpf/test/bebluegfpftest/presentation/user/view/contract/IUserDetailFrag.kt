package com.beblue.gfpf.test.bebluegfpftest.presentation.user.view.contract

import androidx.lifecycle.LiveData
import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHTag

interface IUserDetailFrag {

    interface View {
        fun setReposProgressIndicator(isActive: Boolean)
        fun setTagsProgressIndicator(isActive: Boolean)
        fun showToastMessage(message: String)
        fun showUserRepoListUI(ghRepos: List<GHRepository>?, isAppend: Boolean)
        fun showUserTagListUI(ghTags: List<GHTag>?)
    }

    interface ActionListener {
        fun loadUserRepos(username: String): LiveData<List<GHRepository>>
        fun loadRepoTags(owner: String, repo: String): LiveData<List<GHTag>>
    }
}