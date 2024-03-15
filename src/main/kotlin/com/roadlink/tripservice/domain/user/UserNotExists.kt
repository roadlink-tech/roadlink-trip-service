package com.roadlink.tripservice.domain.user

import com.roadlink.tripservice.domain.common.DomainError

sealed class UserError(message: String) : DomainError(message) {
    class NotExists(userId: String) : RuntimeException("User '$userId' not exists")
}
