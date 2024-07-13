package com.gfpf.github_api.domain.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GHSearchUser(
    @SerialName("total_count")
    var totalCount: Int? = null,

    @SerialName("incomplete_results")
    var isIncompleteResult: Boolean = false,

    @SerialName("items")
    var users: List<GHUser>? = null
) {
    companion object {
        const val REQUESTED_USER_KEY = "REQUESTED_USER_KEY"
    }
}