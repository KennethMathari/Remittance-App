package org.tawakal.composemphelloworld.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class TimeManager{
    fun getCurrentTimestamp(): String {
        val currentTime: Instant = Clock.System.now()
        return currentTime.toString() // Format the timestamp as ISO 8601
    }
}