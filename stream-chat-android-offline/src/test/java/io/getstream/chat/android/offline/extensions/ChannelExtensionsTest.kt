package io.getstream.chat.android.offline.extensions

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.parser2.adapters.DateAdapter
import io.getstream.chat.android.offline.randomChannel
import io.getstream.chat.android.offline.request.QueryChannelsPaginationRequest
import io.getstream.chat.android.offline.request.toAnyChannelPaginationRequest
import okio.buffer
import okio.source
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import java.io.File
import java.util.Date

internal class ChannelExtensionsTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `When apply pagination Should not throw any exception`() {
        val channelsFile = File(this.javaClass.classLoader!!.getResource("channels.json").toURI())
        val moshi = Moshi.Builder()
            .add(DateAdapter())
            .add(
                File::class.java,
                object : JsonAdapter<File>() {
                    // Dummy adapter because reflective serialization can't deal with platform types
                    override fun fromJson(reader: JsonReader): File? = null
                    override fun toJson(writer: JsonWriter, value: File?) = TODO("Not implemented")
                }
            )
            .add(
                Attachment.UploadState::class.java,
                object : JsonAdapter<Attachment.UploadState>() {
                    // Dummy adapter because reflective serialization can't deal with sealed classes
                    override fun fromJson(reader: JsonReader): Attachment.UploadState? = null
                    override fun toJson(writer: JsonWriter, value: Attachment.UploadState?) = TODO("Not implemented")
                }
            )
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter<List<Channel>>()
        val channels = requireNotNull(adapter.fromJson(JsonReader.of(channelsFile.source().buffer())))
        val sort = QuerySort<Channel>().desc(Channel::lastMessageAt)
        val queryPaginationRequest = QueryChannelsPaginationRequest(
            sort = sort,
            channelOffset = 0,
            channelLimit = 30,
            messageLimit = 10,
            memberLimit = 30,
        )

        channels.applyPagination(queryPaginationRequest.toAnyChannelPaginationRequest())
    }

    @Test
    fun `When apply pagination Should sort correctly descending`() {
        val firstChannel = randomChannel(lastMessageAt = Date(1000))
        val secondChannel = randomChannel(lastMessageAt = Date(3000))
        val thirdChannel = randomChannel(lastMessageAt = Date(2000))
        val sort = QuerySort<Channel>().desc(Channel::lastMessageAt)
        val queryPaginationRequest = QueryChannelsPaginationRequest(
            sort = sort,
            channelOffset = 0,
            channelLimit = 30,
            messageLimit = 10,
            memberLimit = 30,
        )

        val result1 = listOf(
            firstChannel,
            secondChannel,
            thirdChannel
        ).applyPagination(queryPaginationRequest.toAnyChannelPaginationRequest())

        result1.first() shouldBeEqualTo secondChannel
        result1.last() shouldBeEqualTo firstChannel
    }

    @Test
    fun `When apply pagination Should sort correctly ascending`() {
        val firstChannel = randomChannel(lastMessageAt = Date(1000))
        val secondChannel = randomChannel(lastMessageAt = Date(3000))
        val thirdChannel = randomChannel(lastMessageAt = Date(2000))
        val sort = QuerySort<Channel>().asc(Channel::lastMessageAt)
        val queryPaginationRequest = QueryChannelsPaginationRequest(
            sort = sort,
            channelOffset = 0,
            channelLimit = 30,
            messageLimit = 10,
            memberLimit = 30,
        )

        val result1 = listOf(
            firstChannel,
            secondChannel,
            thirdChannel
        ).applyPagination(queryPaginationRequest.toAnyChannelPaginationRequest())

        result1.first() shouldBeEqualTo firstChannel
        result1.last() shouldBeEqualTo secondChannel
    }
}