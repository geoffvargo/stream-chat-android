/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.chat.android.client.api2

import io.getstream.chat.android.client.api.ChatApi
import io.getstream.chat.android.client.api.models.PinnedMessagesPagination
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.api.models.SearchMessagesRequest
import io.getstream.chat.android.client.api2.endpoint.ChannelApi
import io.getstream.chat.android.client.api2.endpoint.DeviceApi
import io.getstream.chat.android.client.api2.endpoint.FileDownloadApi
import io.getstream.chat.android.client.api2.endpoint.GeneralApi
import io.getstream.chat.android.client.api2.endpoint.GuestApi
import io.getstream.chat.android.client.api2.endpoint.MessageApi
import io.getstream.chat.android.client.api2.endpoint.ModerationApi
import io.getstream.chat.android.client.api2.endpoint.UserApi
import io.getstream.chat.android.client.api2.endpoint.VideoCallApi
import io.getstream.chat.android.client.api2.mapping.toDomain
import io.getstream.chat.android.client.api2.mapping.toDto
import io.getstream.chat.android.client.api2.model.dto.ChatEventDto
import io.getstream.chat.android.client.api2.model.dto.DeviceDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamMemberDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamMessageDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamReactionDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamUserDto
import io.getstream.chat.android.client.api2.model.dto.PartialUpdateUserDto
import io.getstream.chat.android.client.api2.model.dto.UpstreamUserDto
import io.getstream.chat.android.client.api2.model.requests.AcceptInviteRequest
import io.getstream.chat.android.client.api2.model.requests.AddDeviceRequest
import io.getstream.chat.android.client.api2.model.requests.AddMembersRequest
import io.getstream.chat.android.client.api2.model.requests.BanUserRequest
import io.getstream.chat.android.client.api2.model.requests.GuestUserRequest
import io.getstream.chat.android.client.api2.model.requests.HideChannelRequest
import io.getstream.chat.android.client.api2.model.requests.InviteMembersRequest
import io.getstream.chat.android.client.api2.model.requests.MarkReadRequest
import io.getstream.chat.android.client.api2.model.requests.MarkUnreadRequest
import io.getstream.chat.android.client.api2.model.requests.MuteChannelRequest
import io.getstream.chat.android.client.api2.model.requests.MuteUserRequest
import io.getstream.chat.android.client.api2.model.requests.PartialUpdateMessageRequest
import io.getstream.chat.android.client.api2.model.requests.PartialUpdateUsersRequest
import io.getstream.chat.android.client.api2.model.requests.PinnedMessagesRequest
import io.getstream.chat.android.client.api2.model.requests.QueryBannedUsersRequest
import io.getstream.chat.android.client.api2.model.requests.ReactionRequest
import io.getstream.chat.android.client.api2.model.requests.RejectInviteRequest
import io.getstream.chat.android.client.api2.model.requests.RemoveMembersRequest
import io.getstream.chat.android.client.api2.model.requests.SendActionRequest
import io.getstream.chat.android.client.api2.model.requests.SendEventRequest
import io.getstream.chat.android.client.api2.model.requests.SyncHistoryRequest
import io.getstream.chat.android.client.api2.model.requests.TruncateChannelRequest
import io.getstream.chat.android.client.api2.model.requests.UpdateChannelPartialRequest
import io.getstream.chat.android.client.api2.model.requests.UpdateChannelRequest
import io.getstream.chat.android.client.api2.model.requests.UpdateCooldownRequest
import io.getstream.chat.android.client.api2.model.requests.UpdateMessageRequest
import io.getstream.chat.android.client.api2.model.requests.UpdateUsersRequest
import io.getstream.chat.android.client.api2.model.requests.VideoCallCreateRequest
import io.getstream.chat.android.client.api2.model.requests.VideoCallTokenRequest
import io.getstream.chat.android.client.api2.model.response.BannedUserResponse
import io.getstream.chat.android.client.api2.model.response.ChannelResponse
import io.getstream.chat.android.client.api2.model.response.CreateVideoCallResponse
import io.getstream.chat.android.client.api2.model.response.TranslateMessageRequest
import io.getstream.chat.android.client.api2.model.response.VideoCallTokenResponse
import io.getstream.chat.android.client.events.ChatEvent
import io.getstream.chat.android.client.extensions.enrichWithCid
import io.getstream.chat.android.client.extensions.syncUnreadCountWithReads
import io.getstream.chat.android.client.helpers.CallPostponeHelper
import io.getstream.chat.android.client.parser.toMap
import io.getstream.chat.android.client.parser2.adapters.RawJson
import io.getstream.chat.android.client.scope.UserScope
import io.getstream.chat.android.client.uploader.FileUploader
import io.getstream.chat.android.client.utils.ProgressCallback
import io.getstream.chat.android.models.AppSettings
import io.getstream.chat.android.models.BannedUser
import io.getstream.chat.android.models.BannedUsersSort
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Device
import io.getstream.chat.android.models.FilterObject
import io.getstream.chat.android.models.Flag
import io.getstream.chat.android.models.GuestUser
import io.getstream.chat.android.models.Member
import io.getstream.chat.android.models.Message
import io.getstream.chat.android.models.Mute
import io.getstream.chat.android.models.Reaction
import io.getstream.chat.android.models.SearchMessagesResult
import io.getstream.chat.android.models.UploadedFile
import io.getstream.chat.android.models.UploadedImage
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.VideoCallInfo
import io.getstream.chat.android.models.VideoCallToken
import io.getstream.chat.android.models.querysort.QuerySorter
import io.getstream.log.taggedLogger
import io.getstream.openapi.models.DefaultApi
import io.getstream.openapi.models.StreamChatBanRequest
import io.getstream.openapi.models.StreamChatChannelMember
import io.getstream.openapi.models.StreamChatChannelMemberRequest
import io.getstream.openapi.models.StreamChatChannelRequest
import io.getstream.openapi.models.StreamChatChannelStopWatchingRequest
import io.getstream.openapi.models.StreamChatCreateDeviceRequest
import io.getstream.openapi.models.StreamChatDevice
import io.getstream.openapi.models.StreamChatFlagRequest
import io.getstream.openapi.models.StreamChatGetApplicationResponse
import io.getstream.openapi.models.StreamChatGuestRequest
import io.getstream.openapi.models.StreamChatHideChannelRequest
import io.getstream.openapi.models.StreamChatMarkChannelsReadRequest
import io.getstream.openapi.models.StreamChatMarkReadRequest
import io.getstream.openapi.models.StreamChatMarkUnreadRequest
import io.getstream.openapi.models.StreamChatMessage
import io.getstream.openapi.models.StreamChatMessageActionRequest
import io.getstream.openapi.models.StreamChatMessageRequest
import io.getstream.openapi.models.StreamChatMuteChannelRequest
import io.getstream.openapi.models.StreamChatMuteUserRequest
import io.getstream.openapi.models.StreamChatQueryBannedUsersRequest
import io.getstream.openapi.models.StreamChatQueryChannelsRequest
import io.getstream.openapi.models.StreamChatQueryMembersRequest
import io.getstream.openapi.models.StreamChatQueryUsersRequest
import io.getstream.openapi.models.StreamChatReaction
import io.getstream.openapi.models.StreamChatSearchRequest
import io.getstream.openapi.models.StreamChatSendMessageRequest
import io.getstream.openapi.models.StreamChatSendReactionRequest
import io.getstream.openapi.models.StreamChatShowChannelRequest
import io.getstream.openapi.models.StreamChatSortParam
import io.getstream.openapi.models.StreamChatSortParamRequest
import io.getstream.openapi.models.StreamChatTranslateMessageRequest
import io.getstream.openapi.models.StreamChatTruncateChannelRequest
import io.getstream.openapi.models.StreamChatUnmuteChannelRequest
import io.getstream.openapi.models.StreamChatUnmuteUserRequest
import io.getstream.openapi.models.StreamChatUpdateChannelPartialRequest
import io.getstream.openapi.models.StreamChatUpdateChannelPartialResponse
import io.getstream.openapi.models.StreamChatUpdateChannelRequest
import io.getstream.openapi.models.StreamChatUpdateChannelResponse
import io.getstream.openapi.models.StreamChatUpdateMessagePartialRequest
import io.getstream.openapi.models.StreamChatUpdateMessageRequest
import io.getstream.openapi.models.StreamChatUpdateUserPartialRequest
import io.getstream.openapi.models.StreamChatUpdateUsersRequest
import io.getstream.openapi.models.StreamChatUserObject
import io.getstream.openapi.models.StreamChatUserObjectRequest
import io.getstream.openapi.models.StreamChatUserResponse
import io.getstream.result.Result
import io.getstream.result.call.Call
import io.getstream.result.call.CoroutineCall
import io.getstream.result.call.map
import io.getstream.result.call.toUnitCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import java.io.File
import java.util.Date
import io.getstream.chat.android.client.api.models.SendActionRequest as DomainSendActionRequest

@Suppress("TooManyFunctions", "LargeClass")
internal class MoshiChatApi
@Suppress("LongParameterList")
constructor(
    private val fileUploader: FileUploader,
    private val defaultApi: DefaultApi,
    private val userApi: UserApi,
    private val guestApi: GuestApi,
    private val messageApi: MessageApi,
    private val channelApi: ChannelApi,
    private val deviceApi: DeviceApi,
    private val moderationApi: ModerationApi,
    private val generalApi: GeneralApi,
    private val callApi: VideoCallApi,
    private val fileDownloadApi: FileDownloadApi,
    private val coroutineScope: CoroutineScope,
    private val userScope: UserScope,
) : ChatApi {

    private val logger by taggedLogger("Chat:MoshiChatApi")

    private val callPostponeHelper: CallPostponeHelper by lazy {
        CallPostponeHelper(
            awaitConnection = {
                _connectionId.first { id -> id.isNotEmpty() }
            },
            userScope = userScope,
        )
    }

    @Volatile
    private var userId: String = ""
        get() {
            if (field == "") {
                logger.e { "userId accessed before being set. Did you forget to call ChatClient.connectUser()?" }
            }
            return field
        }

    private val _connectionId: MutableStateFlow<String> = MutableStateFlow("")

    private val connectionId: String
        get() {
            if (_connectionId.value == "") {
                logger.e { "connectionId accessed before being set. Did you forget to call ChatClient.connectUser()?" }
            }
            return _connectionId.value
        }

    override fun setConnection(userId: String, connectionId: String) {
        logger.d { "[setConnection] userId: '$userId', connectionId: '$connectionId'" }
        this.userId = userId
        this._connectionId.value = connectionId
    }

    override fun releseConnection() {
        this._connectionId.value = ""
    }

    override fun appSettings(): Call<AppSettings> {
        //TODO: The `StreamChatApp` model has a lot of fields that are marked as required but we don't get them
        return defaultApi.getApp().map(StreamChatGetApplicationResponse::toDomain)
    }

    override fun sendMessage(
        channelType: String,
        channelId: String,
        message: Message,
    ): Call<Message> {
        return defaultApi.sendMessage(
            type = channelType,
            id = channelId,
            request = StreamChatSendMessageRequest(
                message = message.toDto(),
                skip_push = message.skipPushNotification,
                skip_enrich_url = message.skipEnrichUrl,
            ),
        ).map { response -> response.message.toDomain() }
    }

    override fun updateMessage(
        message: Message,
    ): Call<Message> {
        return defaultApi.updateMessage(
            id = message.id,
            request = StreamChatUpdateMessageRequest(
                message = message.toDto(),
                skip_enrich_url = message.skipEnrichUrl,
            ),
        ).map { response -> response.message.toDomain() }
    }

    override fun partialUpdateMessage(
        messageId: String,
        set: Map<String, Any>,
        unset: List<String>,
        skipEnrichUrl: Boolean,
    ): Call<Message> {
        return defaultApi.updateMessagePartial(
            id = messageId,
            request = StreamChatUpdateMessagePartialRequest(
                set = RawJson(set),
                unset = unset,
                skip_enrich_url = skipEnrichUrl,
            ),
        ).map { response -> response.message.toDomain() }
    }

    override fun getMessage(messageId: String): Call<Message> {
        return defaultApi.getMessage(
            id = messageId,
            //TODO: message shoudln't be nullable?
        ).map { response -> response.message!!.toDomain() }
    }

    override fun deleteMessage(messageId: String, hard: Boolean): Call<Message> {
        return defaultApi.deleteMessage(
            id = messageId,
            hard = if (hard) true else null,
            //TODO: this deleted_by was never set so far)
            deletedBy = null
            //TODO: response message should not be null
        ).map { response -> response.message!!.toDomain() }
    }

    override fun getReactions(
        messageId: String,
        offset: Int,
        limit: Int,
    ): Call<List<Reaction>> {
        return defaultApi.getReactions(
            id = messageId,
            offset = offset,
            limit = limit,
            //TODO: list of reactions shouldn't contain nullable items
        ).map { response -> response.reactions.map{ it -> it!!.toDomain()} }
    }

    override fun sendReaction(reaction: Reaction, enforceUnique: Boolean): Call<Reaction> {
        return defaultApi.sendReaction(
            id = reaction.messageId,
            request = StreamChatSendReactionRequest(
                reaction = reaction.toDto(),
                enforce_unique = enforceUnique,
            ),
            //TODO: reaction shouldn't be nullable
        ).map { response -> response.reaction!!.toDomain() }
    }

    override fun deleteReaction(
        messageId: String,
        reactionType: String,
    ): Call<Message> {
        return defaultApi.deleteReaction(
            id = messageId,
            type = reactionType,
            userId = null
            //TODO: message shouldn't be nullable
        ).map { response -> response.message!!.toDomain() }
    }

    override fun addDevice(device: Device): Call<Unit> {
        return defaultApi.createDevice(
            request = StreamChatCreateDeviceRequest(
                device.token,
                device.pushProvider.key,
                device.providerName,
            ),
        ).toUnitCall()
    }

    override fun deleteDevice(device: Device): Call<Unit> {
        return defaultApi.deleteDevice(id = device.token, userId = null).toUnitCall()
    }

    override fun getDevices(): Call<List<Device>> {
        return defaultApi.listDevices(userId = null).map { response -> response.devices.map(StreamChatDevice::toDomain) }
    }

    override fun muteCurrentUser(): Call<Mute> {
        return muteUser(
            userId = userId,
            timeout = null,
        )
    }

    override fun unmuteCurrentUser(): Call<Unit> {
        return unmuteUser(userId)
    }

    override fun muteUser(
        userId: String,
        timeout: Int?,
    ): Call<Mute> {
        return defaultApi.muteUser(
            request = StreamChatMuteUserRequest(
                target_ids = listOf(userId),
                user_id = this.userId,
                timeout = timeout,
            ),
            //TODO: mute shouldn't be nullable
        ).map { response -> response.mute!!.toDomain() }
    }

    override fun unmuteUser(userId: String): Call<Unit> {
        return defaultApi.unmuteUser(
            request = StreamChatUnmuteUserRequest(
                target_id = userId,
                //TODO: this is a new field from spec
                target_ids = emptyList(),
                user_id = this.userId,
                timeout = null,
            ),
        ).toUnitCall()
    }

    override fun muteChannel(
        channelType: String,
        channelId: String,
        expiration: Int?,
    ): Call<Unit> {
        return defaultApi.muteChannel(
            request = StreamChatMuteChannelRequest(
                channel_cids = listOf("$channelType:$channelId"),
                expiration = expiration,
            ),
        ).toUnitCall()
    }

    override fun unmuteChannel(
        channelType: String,
        channelId: String,
    ): Call<Unit> {
        return defaultApi.unmuteChannel(
            //TODO: why is there also a channel_cids?
            request = StreamChatUnmuteChannelRequest(
                channel_cid = "$channelType:$channelId",
                channel_cids = emptyList(),
                expiration = null,
            ),
        ).toUnitCall()
    }

    override fun sendFile(
        channelType: String,
        channelId: String,
        file: File,
        callback: ProgressCallback?,
    ): Call<UploadedFile> {
        return CoroutineCall(coroutineScope) {
            if (callback != null) {
                fileUploader.sendFile(
                    channelType = channelType,
                    channelId = channelId,
                    userId = userId,
                    file = file,
                    callback,
                )
            } else {
                fileUploader.sendFile(
                    channelType = channelType,
                    channelId = channelId,
                    userId = userId,
                    file = file,
                )
            }
        }
    }

    override fun sendImage(
        channelType: String,
        channelId: String,
        file: File,
        callback: ProgressCallback?,
    ): Call<UploadedImage> {
        return CoroutineCall(coroutineScope) {
            if (callback != null) {
                fileUploader.sendImage(
                    channelType = channelType,
                    channelId = channelId,
                    userId = userId,
                    file = file,
                    callback,
                )
            } else {
                fileUploader.sendImage(
                    channelType = channelType,
                    channelId = channelId,
                    userId = userId,
                    file = file,
                )
            }
        }
    }

    override fun deleteFile(channelType: String, channelId: String, url: String): Call<Unit> {
        return CoroutineCall(coroutineScope) {
            fileUploader.deleteFile(
                channelType = channelType,
                channelId = channelId,
                userId = userId,
                url = url,
            )
            Result.Success(Unit)
        }
    }

    override fun deleteImage(channelType: String, channelId: String, url: String): Call<Unit> {
        return CoroutineCall(coroutineScope) {
            fileUploader.deleteImage(
                channelType = channelType,
                channelId = channelId,
                userId = userId,
                url = url,
            )
            Result.Success(Unit)
        }
    }

    override fun flagUser(userId: String): Call<Flag> =
        flag(mutableMapOf("target_user_id" to userId))

    override fun unflagUser(userId: String): Call<Flag> =
        //TODO: unflag missing!
        unflag(mutableMapOf("target_user_id" to userId))

    override fun flagMessage(messageId: String): Call<Flag> =
        defaultApi.flag(request = StreamChatFlagRequest(target_message_id = messageId)).map { response -> response.flag!!.toDomain() }

    override fun unflagMessage(messageId: String): Call<Flag> =
        unflag(mutableMapOf("target_message_id" to messageId))

    private fun flag(body: MutableMap<String, String>): Call<Flag> {
        return moderationApi.flag(body = body).map { response -> response.flag.toDomain() }
    }

    private fun unflag(body: MutableMap<String, String>): Call<Flag> {
        return moderationApi.unflag(body = body).map { response -> response.flag.toDomain() }
    }

    override fun banUser(
        targetId: String,
        timeout: Int?,
        reason: String?,
        channelType: String,
        channelId: String,
        shadow: Boolean,
    ): Call<Unit> {
        return defaultApi.ban(
            request = StreamChatBanRequest(
                target_user_id = targetId,
                timeout = timeout,
                reason = reason,
                type = channelType,
                id = channelId,
                shadow = shadow,
            ),
        ).toUnitCall()
    }

    override fun unbanUser(
        targetId: String,
        channelType: String,
        channelId: String,
        shadow: Boolean,
    ): Call<Unit> {
        return defaultApi.unban(
            targetUserId = targetId,
            id = channelId,
            type = channelType,
            //TODO: new parameter?
            createdBy = null
            //TODO: missing
            //shadow = shadow,
        ).toUnitCall()
    }

    override fun queryBannedUsers(
        filter: FilterObject,
        sort: QuerySorter<BannedUsersSort>,
        offset: Int?,
        limit: Int?,
        createdAtAfter: Date?,
        createdAtAfterOrEqual: Date?,
        createdAtBefore: Date?,
        createdAtBeforeOrEqual: Date?,
    ): Call<List<BannedUser>> {
        return defaultApi.queryBannedUsers(
            payload = StreamChatQueryBannedUsersRequest(
                filter_conditions = RawJson(filter.toMap()),
                sort = sort.toDto().map { StreamChatSortParam(direction = it.direction, field = it.field) },
                offset = offset,
                limit = limit,
                created_at_after = createdAtAfter,
                created_at_after_or_equal = createdAtAfterOrEqual,
                created_at_before = createdAtBefore,
                created_at_before_or_equal = createdAtBeforeOrEqual,
            ),
        ).map { response -> response.bans.map{ it!!.toDomain()} }
    }

    override fun enableSlowMode(
        channelType: String,
        channelId: String,
        cooldownTimeInSeconds: Int,
    ): Call<Unit> = updateCooldown(
        channelType = channelType,
        channelId = channelId,
        cooldownTimeInSeconds = cooldownTimeInSeconds,
    )

    override fun disableSlowMode(
        channelType: String,
        channelId: String,
    ): Call<Unit> = updateCooldown(
        channelType = channelType,
        channelId = channelId,
        cooldownTimeInSeconds = 0,
    )

    private fun updateCooldown(
        channelType: String,
        channelId: String,
        cooldownTimeInSeconds: Int,
    ): Call<Unit> {
        return defaultApi.updateChannelPartial(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelPartialRequest(set = RawJson(mapOf("cooldown" to cooldownTimeInSeconds)) , unset = emptyList()),
        ).toUnitCall()
    }

    override fun stopWatching(channelType: String, channelId: String): Call<Unit> = postponeCall {
        defaultApi.stopWatchingChannel(
            type = channelType,
            id = channelId,
            connectionId = connectionId,
            request = StreamChatChannelStopWatchingRequest(),
        ).toUnitCall()
    }

    override fun getPinnedMessages(
        channelType: String,
        channelId: String,
        limit: Int,
        sort: QuerySorter<Message>,
        pagination: PinnedMessagesPagination,
    ): Call<List<Message>> {
        //TODO: missing API
        return channelApi.getPinnedMessages(
            channelType = channelType,
            channelId = channelId,
            payload = PinnedMessagesRequest.create(
                limit = limit,
                sort = sort,
                pagination = pagination,
            ),
        ).map { response -> response.messages.map(DownstreamMessageDto::toDomain) }
    }

    override fun updateChannel(
        channelType: String,
        channelId: String,
        extraData: Map<String, Any>,
        updateMessage: Message?,
    ): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = emptyList(),
                data = StreamChatChannelRequest(Custom = RawJson(extraData)),
                message = updateMessage?.toDto()
            ),
        ).map{ it.channel!!.toDomain() }
    }

    override fun updateChannelPartial(
        channelType: String,
        channelId: String,
        set: Map<String, Any>,
        unset: List<String>,
    ): Call<Channel> {
        return defaultApi.updateChannelPartial(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelPartialRequest(set = RawJson(set), unset = unset),
        ).map{ it.channel!!.toDomain() }
    }

    override fun showChannel(
        channelType: String,
        channelId: String,
    ): Call<Unit> {
        return defaultApi.showChannel(
            type = channelType,
            id = channelId,
            request = StreamChatShowChannelRequest(),
        ).toUnitCall()
    }

    override fun hideChannel(
        channelType: String,
        channelId: String,
        clearHistory: Boolean,
    ): Call<Unit> {
        return defaultApi.hideChannel(
            type = channelType,
            id = channelId,
            request = StreamChatHideChannelRequest(clear_history = clearHistory),
        ).toUnitCall()
    }

    override fun truncateChannel(
        channelType: String,
        channelId: String,
        systemMessage: Message?,
    ): Call<Channel> {
        return defaultApi.truncateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatTruncateChannelRequest(message = systemMessage?.toDto()),
        ).map{ it.channel!!.toDomain() }
    }

    override fun rejectInvite(channelType: String, channelId: String): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                reject_invite = true,
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = emptyList()
            ),
        ).map{ it.channel!!.toDomain() }
    }

    override fun acceptInvite(
        channelType: String,
        channelId: String,
        message: String?,
    ): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                accept_invite = true,
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = emptyList(),
                message = StreamChatMessageRequest(text = message, attachments = emptyList())
            ),
        ).map{ it.channel!!.toDomain() }
    }

    override fun deleteChannel(channelType: String, channelId: String): Call<Channel> {
        return defaultApi.deleteChannel(
            type = channelType,
            id = channelId,
            hardDelete = false
        ).map{ it.channel!!.toDomain() }
    }

    override fun markRead(channelType: String, channelId: String, messageId: String): Call<Unit> {
        return defaultApi.markRead(
            type = channelType,
            id = channelId,
            request = StreamChatMarkReadRequest(message_id = messageId),
        ).toUnitCall()
    }

    override fun markUnread(channelType: String, channelId: String, messageId: String): Call<Unit> {
        return defaultApi.markUnread(
            type = channelType,
            id = channelId,
            request = StreamChatMarkUnreadRequest(message_id = messageId),
        ).toUnitCall()
    }

    override fun markAllRead(): Call<Unit> {
        return defaultApi.markChannelsRead(StreamChatMarkChannelsReadRequest()).toUnitCall()
    }

    override fun addMembers(
        channelType: String,
        channelId: String,
        members: List<String>,
        systemMessage: Message?,
        hideHistory: Boolean?,
        skipPush: Boolean?,
    ): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = emptyList(),
                add_members = members.map { StreamChatChannelMemberRequest(user_id = it) },
                hide_history = hideHistory,
                message = systemMessage?.toDto(),
                skip_push = skipPush),
        ).map{ it.channel!!.toDomain() }
    }

    override fun removeMembers(
        channelType: String,
        channelId: String,
        members: List<String>,
        systemMessage: Message?,
        skipPush: Boolean?,
    ): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = members,
                message = systemMessage?.toDto(),
                skip_push = skipPush),
        ).map{ it.channel!!.toDomain() }
    }

    override fun inviteMembers(
        channelType: String,
        channelId: String,
        members: List<String>,
        systemMessage: Message?,
        skipPush: Boolean?,
    ): Call<Channel> {
        return defaultApi.updateChannel(
            type = channelType,
            id = channelId,
            request = StreamChatUpdateChannelRequest(
                add_moderators = emptyList(),
                demote_moderators = emptyList(),
                remove_members = emptyList(),
                invites = members.map { StreamChatChannelMemberRequest(user_id = it) },
                message = systemMessage?.toDto(),
                skip_push = skipPush),
        ).map{ it.channel!!.toDomain() }
    }

    private fun flattenChannel(response: ChannelResponse): Channel {
        return response.channel.toDomain().let { channel ->
            channel.copy(
                watcherCount = response.watcher_count,
                read = response.read.map { it.toDomain(channel.lastMessageAt ?: it.last_read) },
                members = response.members.map(DownstreamMemberDto::toDomain),
                membership = response.membership?.toDomain(),
                messages = response.messages.map { it.toDomain().enrichWithCid(channel.cid) },
                watchers = response.watchers.map(DownstreamUserDto::toDomain),
                hidden = response.hidden,
                hiddenMessagesBefore = response.hide_messages_before,
            ).syncUnreadCountWithReads()
        }
    }

    override fun getReplies(messageId: String, limit: Int): Call<List<Message>> {
        return defaultApi.getReplies(
            parentId = messageId,
            //TODO: missing limit
            //limit = limit,
            createdAtAfter = null,
            idLt = null,
            idLte = null,
            idAround = null,
            idGt = null,
            idGte = null,
            createdAtAfterOrEqual = null,
            createdAtAround = null,
            createdAtBefore = null,
            createdAtBeforeOrEqual = null
        ).map { response -> response.messages.map(StreamChatMessage::toDomain) }
    }

    override fun getRepliesMore(messageId: String, firstId: String, limit: Int): Call<List<Message>> {
        return defaultApi.getReplies(
            parentId = messageId,
            idLt = firstId,
            //TODO: missing limit
            //limit = limit,
            createdAtAfter = null,
            idLte = null,
            idAround = null,
            idGt = null,
            idGte = null,
            createdAtAfterOrEqual = null,
            createdAtAround = null,
            createdAtBefore = null,
            createdAtBeforeOrEqual = null
        ).map { response -> response.messages.map(StreamChatMessage::toDomain) }
    }

    override fun sendAction(request: DomainSendActionRequest): Call<Message> {
        return defaultApi.runMessageAction(
            id = request.messageId,
            request = StreamChatMessageActionRequest(
                form_data = request.formData,
            ),
        ).map { response -> response.message!!.toDomain() }
    }

    override fun updateUsers(users: List<User>): Call<List<User>> {
        val map: Map<String, StreamChatUserObjectRequest> = users.associateBy({ it.id }, User::toDto)
        return defaultApi.updateUsers(
            request = StreamChatUpdateUsersRequest(map),
        ).map { response ->
            response.users.values.map(StreamChatUserObject::toDomain)
        }
    }

    override fun partialUpdateUser(id: String, set: Map<String, Any>, unset: List<String>): Call<User> {
        return defaultApi.updateUsersPartial(
            request = StreamChatUpdateUserPartialRequest(
                id = userId,
                set = RawJson(set),
                unset = unset,
            ),
        ).map { response ->
            response.users[id]!!.toDomain()
        }
    }

    override fun getGuestUser(userId: String, userName: String): Call<GuestUser> {
        return defaultApi.createGuest(
            request = StreamChatGuestRequest(user = StreamChatUserObjectRequest(id = userId,
                custom = RawJson(mapOf("name" to userName)))),
        ).map { response -> GuestUser(response.user!!.toDomain(), response.access_token) }
    }

    override fun translate(messageId: String, language: String): Call<Message> {
        return defaultApi.translateMessage(
            id = messageId,
            request = StreamChatTranslateMessageRequest(language = language),
        ).map { response -> response.message!!.toDomain() }
    }

    override fun searchMessages(request: SearchMessagesRequest): Call<List<Message>> {
        val newRequest = StreamChatSearchRequest(
            filter_conditions = RawJson(request.channelFilter.toMap()),
            message_filter_conditions = RawJson(request.messageFilter.toMap()),
            offset = request.offset,
            limit = request.limit,
            next = request.next,
            sort = request.sort.orEmpty().map { StreamChatSortParam(direction = it.direction, field = it.field) },
        )
        return defaultApi.search(newRequest)
            .map { response ->
                response.results.map { resp ->
                    //TODO: not nullable
                    resp!!.message!!.toDomain()
                        .let { message ->
                            (message.cid.takeUnless(CharSequence::isBlank) ?: message.channelInfo?.cid)
                                ?.let(message::enrichWithCid)
                                ?: message
                        }
                }
            }
    }

    override fun searchMessages(
        channelFilter: FilterObject,
        messageFilter: FilterObject,
        offset: Int?,
        limit: Int?,
        next: String?,
        sort: QuerySorter<Message>?,
    ): Call<SearchMessagesResult> {
        val newRequest = StreamChatSearchRequest(
            filter_conditions = RawJson(channelFilter.toMap()),
            message_filter_conditions = RawJson(messageFilter.toMap()),
            offset = offset,
            limit = limit,
            next = next,
            sort = sort?.toDto()?.map { StreamChatSortParam(direction = it.direction, field = it.field) },
        )
        return defaultApi.search(newRequest)
            .map { response ->
                val results = response.results

                val messages = results.map { resp ->
                    //TODO: shouldn't be null
                    resp!!.message!!.toDomain().let { message ->
                        (message.cid.takeUnless(CharSequence::isBlank) ?: message.channelInfo?.cid)
                            ?.let(message::enrichWithCid)
                            ?: message
                    }
                }
                SearchMessagesResult(
                    messages = messages,
                    next = response.next,
                    previous = response.previous,
                    resultsWarning = response.results_warning?.toDomain(),
                )
            }
    }

    override fun queryChannels(query: QueryChannelsRequest): Call<List<Channel>> {
        val request = StreamChatQueryChannelsRequest(
            filter_conditions = RawJson(query.filter.toMap()),
            offset = query.offset,
            limit = query.limit,
            sort = query.sort.map { StreamChatSortParamRequest(direction = it.direction, field = it.field) },
            message_limit = query.messageLimit,
            member_limit = query.memberLimit,
            state = query.state,
            watch = query.watch,
            presence = query.presence,
        )

        val lazyQueryChannelsCall = {
            defaultApi.queryChannels(
                connectionId = connectionId,
                request = request,
            ).map { response -> response.channels.map{ it.channel!!.toDomain() } }
        }

        val isConnectionRequired = query.watch || query.presence
        return if (connectionId.isBlank() && isConnectionRequired) {
            logger.i { "[queryChannels] postponing because an active connection is required" }
            postponeCall(lazyQueryChannelsCall)
        } else {
            lazyQueryChannelsCall()
        }
    }

    override fun queryChannel(channelType: String, channelId: String, query: QueryChannelRequest): Call<Channel> {
        val request = io.getstream.chat.android.client.api2.model.requests.QueryChannelRequest(
            state = query.state,
            watch = query.watch,
            presence = query.presence,
            messages = query.messages,
            watchers = query.watchers,
            members = query.members,
            data = query.data,
        )

        val lazyQueryChannelCall = {
            if (channelId.isEmpty()) {
                channelApi.queryChannel(
                    channelType = channelType,
                    connectionId = connectionId,
                    request = request,
                )
            } else {
                channelApi.queryChannel(
                    channelType = channelType,
                    channelId = channelId,
                    connectionId = connectionId,
                    request = request,
                )
            }.map(::flattenChannel)
        }

        val isConnectionRequired = query.watch || query.presence
        return if (connectionId.isBlank() && isConnectionRequired) {
            logger.i { "[queryChannel] postponing because an active connection is required" }
            postponeCall(lazyQueryChannelCall)
        } else {
            lazyQueryChannelCall()
        }
    }

    override fun queryUsers(queryUsers: QueryUsersRequest): Call<List<User>> {
        val request = StreamChatQueryUsersRequest(
            filter_conditions = RawJson(queryUsers.filter.toMap()),
            offset = queryUsers.offset,
            limit = queryUsers.limit,
            sort = queryUsers.sort.map { StreamChatSortParam(direction = it.direction, field = it.field) },
            presence = queryUsers.presence,
        )
        val lazyQueryUsersCall = {
            defaultApi.queryUsers(
                request,
            ).map { response -> response.users.map {it!!.toDomain()} }
        }

        return if (connectionId.isBlank() && queryUsers.presence) {
            postponeCall(lazyQueryUsersCall)
        } else {
            lazyQueryUsersCall()
        }
    }

    override fun queryMembers(
        channelType: String,
        channelId: String,
        offset: Int,
        limit: Int,
        filter: FilterObject,
        sort: QuerySorter<Member>,
        members: List<Member>,
    ): Call<List<Member>> {
        val request = StreamChatQueryMembersRequest(
            type = channelType,
            id = channelId,
            filter_conditions = RawJson(filter.toMap()),
            offset = offset,
            limit = limit,
            sort = sort.toDto().map { StreamChatSortParam(direction = it.direction, field = it.field) },
            //TODO: this is probably not needed? iOS was never sending this
            // members = members.map { it.toDto() },
        )

        return defaultApi.queryMembers(request)
            .map { response -> response.members.map{ it!!.toDomain()} }
    }

    override fun createVideoCall(
        channelId: String,
        channelType: String,
        callId: String,
        callType: String,
    ): Call<VideoCallInfo> {
        return callApi.createCall(
            channelId = channelId,
            channelType = channelType,
            request = VideoCallCreateRequest(id = callId, type = callType),
        ).map(CreateVideoCallResponse::toDomain)
    }

    override fun getVideoCallToken(callId: String): Call<VideoCallToken> {
        return callApi.getCallToken(callId, VideoCallTokenRequest(callId)).map(VideoCallTokenResponse::toDomain)
    }

    override fun sendEvent(
        eventType: String,
        channelType: String,
        channelId: String,
        extraData: Map<Any, Any>,
    ): Call<ChatEvent> {
        val map = mutableMapOf<Any, Any>("type" to eventType)
        map.putAll(extraData)

        return channelApi.sendEvent(
            channelType = channelType,
            channelId = channelId,
            request = SendEventRequest(map),
        ).map { response -> response.event.toDomain() }
    }

    override fun getSyncHistory(channelIds: List<String>, lastSyncAt: String): Call<List<ChatEvent>> {
        return generalApi.getSyncHistory(
            body = SyncHistoryRequest(channelIds, lastSyncAt),
            connectionId = connectionId,
        ).map { response -> response.events.map(ChatEventDto::toDomain) }
    }

    override fun downloadFile(fileUrl: String): Call<ResponseBody> {
        return fileDownloadApi.downloadFile(fileUrl)
    }

    override fun warmUp() {
        generalApi.warmUp().enqueue()
    }

    private fun <T : Any> postponeCall(call: () -> Call<T>): Call<T> {
        return callPostponeHelper.postponeCall(call)
    }
}
