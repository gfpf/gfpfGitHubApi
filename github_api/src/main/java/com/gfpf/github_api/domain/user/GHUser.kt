package com.gfpf.github_api.domain.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable as KotlinSerializable
import java.io.Serializable as JavaSerializable

@KotlinSerializable
data class GHUser(
    @SerialName("id")
    var id: Int? = null,

    @SerialName("login")
    var login: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("avatar_url")
    var avatarUrl: String? = null,

    @SerialName("html_url")
    var ghUrl: String? = null
) : JavaSerializable {
    companion object {
        const val REQUESTED_USER_KEY = "REQUESTED_USER_KEY"
    }
}
