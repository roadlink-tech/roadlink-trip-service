package com.roadlink.tripservice.dev_tools.infrastructure.network

interface Component {
    fun dispatch(request: Request): Response
}
