package io.getstream.openapi.models
import com.squareup.moshi.JsonClass
import java.util.Date


@JsonClass(generateAdapter = true)
internal data class StreamChatChannelCreatedEvent(

    public val created_at: Date,

    public val type: String,

) : StreamChatWSEvent()
