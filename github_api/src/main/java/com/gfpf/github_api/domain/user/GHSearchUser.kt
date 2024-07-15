package com.gfpf.github_api.domain.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a GitHub Full User.
 *
 * Using Moshi with code generation (via @JsonClass(generateAdapter = true)) is preferred over reflection
 * because:
 *
 * - **Performance**: Code generation avoids the overhead of runtime reflection,
 *      leading to faster serialization and deserialization.
 * - **ProGuard/R8 Safety**: Generated adapters are less likely to be affected by ProGuard/R8 obfuscation,
 *      ensuring that the serialization logic remains intact.
 * - **Reduced APK Size**: Reflection-based libraries often require additional metadata,
 *      which can increase the size of your APK. Code-generated adapters do not have this overhead.
 */
@JsonClass(generateAdapter = true)
data class GHSearchUser(
    //@SerialName("total_count")
    @Json(name = "total_count")
    var totalCount: Int? = null,

    //@SerialName("incomplete_results")
    @Json(name = "incomplete_results")
    var isIncompleteResult: Boolean = false,

    //@SerialName("items")
    @Json(name = "items")
    var users: List<GHUser>? = null
) {
    companion object {
        const val REQUESTED_USER_KEY = "REQUESTED_USER_KEY"
    }
}