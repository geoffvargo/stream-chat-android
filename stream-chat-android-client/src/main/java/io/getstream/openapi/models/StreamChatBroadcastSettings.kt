package io.getstream.openapi.models
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class StreamChatBroadcastSettings(

    public val enabled: Boolean,

    public val hls: StreamChatHLSSettings,

) 
