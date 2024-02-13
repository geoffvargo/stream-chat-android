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

package io.getstream.chat.android.client.api2.mapping

import io.getstream.chat.android.client.api2.model.dto.DeviceDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamChannelMuteDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamMuteDto
import io.getstream.chat.android.client.api2.model.dto.DownstreamUserDto
import io.getstream.chat.android.client.api2.model.dto.UpstreamUserDto
import io.getstream.chat.android.client.parser2.adapters.RawJson
import io.getstream.chat.android.models.Device
import io.getstream.chat.android.models.User
import io.getstream.openapi.models.StreamChatUserObject
import io.getstream.openapi.models.StreamChatUserObjectRequest

internal fun User.toDtoOld(): UpstreamUserDto =
    UpstreamUserDto(
        banned = isBanned,
        id = id,
        name = name,
        image = image,
        invisible = isInvisible,
        language = language,
        role = role,
        devices = devices.map(Device::toDto),
        teams = teams,
        extraData = extraData,
    )

internal fun User.toDto(): StreamChatUserObjectRequest =
    StreamChatUserObjectRequest(
        //TODO: missing in spec
        //banned = isBanned,
        id = id,
        //TODO: missing in spec
        //name = name,
        //TODO: missing in spec
        //image = image,
        invisible = isInvisible,
        language = language,
        role = role,
        //TODO: missing in spec
        //devices = devices.map(Device::toDto),
        teams = teams,
        custom = RawJson(extraData),
    )


internal fun DownstreamUserDto.toDomain(): User =
    User(
        id = id,
        name = name ?: "",
        image = image ?: "",
        role = role,
        invisible = invisible,
        language = language ?: "",
        banned = banned,
        devices = devices.orEmpty().map(DeviceDto::toDomain),
        online = online,
        createdAt = created_at,
        deactivatedAt = deactivated_at,
        updatedAt = updated_at,
        lastActive = last_active,
        totalUnreadCount = total_unread_count,
        unreadChannels = unread_channels,
        mutes = mutes.orEmpty().map(DownstreamMuteDto::toDomain),
        teams = teams,
        channelMutes = channel_mutes.orEmpty().map(DownstreamChannelMuteDto::toDomain),
        extraData = extraData.toMutableMap(),
    )

internal fun StreamChatUserObject.toDomain(): User =
    User(
        id = id,
        //TODO missing name
        //name = name ?: "",
        name = "",
        //TODO missing image
        //image = image ?: "",
        image = "",
        //TODO role should not be nullable?
        role = role ?: "",
        invisible = invisible,
        language = language ?: "",
        banned = banned,
        // TODO missing devices
        //devices = devices.orEmpty().map(DeviceDto::toDomain),
        devices = emptyList(),
        //TODO:
        online = online ?: false,
        createdAt = created_at,
        deactivatedAt = deactivated_at,
        updatedAt = updated_at,
        lastActive = last_active,
        //TODO: missing total_unread_count
        totalUnreadCount = 0,
        //TODO: missing unread_channels
        unreadChannels = 0,
        //TODO: missing mutes
        mutes = emptyList(),
        //TODO: teams should not be nullable
        teams = teams.orEmpty(),
        //TODO: missing channel_mutes
        channelMutes = emptyList(),
        extraData = custom.orEmpty().toMutableMap(),
    )