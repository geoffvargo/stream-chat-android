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

package io.getstream.chat.android.models

/**
 * Device's information needed to register push notifications.
 *
 * @property token Device's token generated by push notification provider.
 * @property pushProvider Push notifications provider type.
 * @property providerName Push notifications provider name.
 * @see [PushProvider]
 */
public data class Device(
    val token: String,
    val pushProvider: PushProvider,
    val providerName: String?,
)

/**
 * Push notifications provider type.
 */
public enum class PushProvider(public val key: String) {
    /** Firebase push notification provider */
    FIREBASE("firebase"),

    /** Huawei push notification provider */
    HUAWEI("huawei"),

    /** Xiaomi push notification provider */
    XIAOMI("xiaomi"),

    /** Unknown push notification provider */
    UNKNOWN("unknown"),
    ;

    public companion object {
        public fun fromKey(key: String): PushProvider =
            values().firstOrNull { it.key == key } ?: UNKNOWN
    }
}