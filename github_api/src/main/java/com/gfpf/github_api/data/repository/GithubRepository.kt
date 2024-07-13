package com.gfpf.github_api.data.repository

import com.gfpf.github_api.data.api.GithubService
import com.gfpf.github_api.domain.user.GHUser

class GithubRepository(private val githubService: GithubService) {
    /*suspend fun getUser(username: String): GHUser {
        return githubService.loadGHUserById(username)
    }*/
}
