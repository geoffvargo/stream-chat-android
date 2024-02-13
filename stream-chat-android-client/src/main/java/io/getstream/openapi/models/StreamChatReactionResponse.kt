package io.getstream.openapi.models
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StreamChatReactionResponse(

    public val duration: String,

    public val message: StreamChatMessage? = null,

    public val reaction: StreamChatReaction? = null,

) 
