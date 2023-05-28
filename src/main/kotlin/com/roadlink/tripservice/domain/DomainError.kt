package com.roadlink.tripservice.domain

import java.lang.RuntimeException

open class DomainError(override val message: String) : RuntimeException(message)