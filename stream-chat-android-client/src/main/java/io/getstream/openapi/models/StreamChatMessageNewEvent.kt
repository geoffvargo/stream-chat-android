package io.getstream.openapi.models
import com.squareup.moshi.JsonClass
import java.util.Date


@JsonClass(generateAdapter = true)
internal data class StreamChatMessageNewEvent(

    public val channel_id: String,

    public val channel_type: String,

    public val cid: String,

    public val created_at: Date,

    public val type: String,

    public val watcher_count: Int,

    public val team: String? = null,

    public val thread_participants: List<StreamChatUserObject>? = null,

    public val message: StreamChatMessage? = null,

    public val user: StreamChatUserObject? = null,

) : StreamChatWSEvent()
