package io.getstream.openapi.models
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StreamChatOnlyUserIDRequest(

    public val id: String,

) 
