package com.gfpf.github_api.data

import androidx.collection.ArrayMap
import com.gfpf.github_api.data.service.GHServiceApi
import com.gfpf.github_api.domain.user.GHRepository
import com.gfpf.github_api.domain.user.GHSearchUser
import com.gfpf.github_api.domain.user.GHTag
import com.gfpf.github_api.domain.user.GHUser
import java.util.Locale

class FakeServiceApiImpl : GHServiceApi {
    override suspend fun createGHUser(vararg users: GHUser) {
        for (user in users) {
            GH_USER_SERVICE_DATA[user.id] = user
        }
    }

    override suspend fun loadAllGHUsers(): List<GHUser> {
        return ArrayList(GH_USER_SERVICE_DATA.values)
    }

    override suspend fun loadGHUserById(id: Int): GHUser? {
        return GH_USER_SERVICE_DATA[id]
    }

    override suspend fun searchGHUserByName(
        username: String,
        sort: String,
        order: String
    ): GHSearchUser {
        val results = GH_USER_SERVICE_DATA.values.filter { user ->
            val itemName = user.login?.trim()?.lowercase(Locale.getDefault())
            val searchTerm = username.trim().lowercase(Locale.getDefault())
            itemName == searchTerm || itemName?.contains(searchTerm) == true
        }

        return GHSearchUser(results.size, false, results)
    }

    override suspend fun loadGHUserRepos(id: String): List<GHRepository> {
        val userId = id.toIntOrNull()
        return GH_REPOS_SERVICE_DATA[userId] ?: emptyList()
    }

    override suspend fun loadGHRepositoryTags(owner: String, repo: String): List<GHTag> {
        val repoKey = "$owner/$repo"
        return GH_TAGS_SERVICE_DATA[repoKey] ?: emptyList()
    }

    override suspend fun updateGHUser(id: Int, ghUser: String) {
    }

    override suspend fun deleteGHUser(id: Int) {
        GH_USER_SERVICE_DATA.remove(id)
    }

    companion object {
        private val GH_USER_SERVICE_DATA: ArrayMap<Int?, GHUser> =
            FakeServiceApiClient.loadAllGHUsers()

        private val GH_REPOS_SERVICE_DATA: ArrayMap<Int?, List<GHRepository>> =
            FakeServiceApiClient.loadAllGHRepositories()

        private val GH_TAGS_SERVICE_DATA: Map<String, MutableList<GHTag>> =
            FakeServiceApiClient.loadAllGHTags().mapKeys { (userId, _) -> userId.toString() }

        /*private val GH_TAGS_SERVICE_DATA: ArrayMap<Int?, List<GHTag>> =
            FakeServiceApiClient.loadAllGHTags()*/
    }
}