package io.getstream.openapi.models
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StreamChatQueryBannedUsersResponse(

    public val duration: String,

    public val bans: List<StreamChatBanResponse?>,

) 
