package com.gfpf.github_api.domain.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

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
data class GHTag(
    @Json(name = "node_id")
    var nodeId: String? = null,

    @Json(name = "name")
    var name: String? = null,

    @Json(name = "zipball_url")
    var zipballUrl: String? = null,

    @Json(name = "tarball_url")
    var tarballUrl: String? = null,

    @Json(name = "commit")
    var commit: GHCommit? = null

) : Serializable {
    companion object {}
}

@JsonClass(generateAdapter = true)
data class GHCommit(
    @Json(name = "sha")
    var nodeId: String? = null,

    @Json(name = "url")
    var url: String? = null

) : Serializable {
    companion object {}
}