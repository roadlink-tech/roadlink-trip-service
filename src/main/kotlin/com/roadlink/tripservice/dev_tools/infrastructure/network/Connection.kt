package com.roadlink.tripservice.dev_tools.infrastructure.network

interface Component<R : Request> {
    fun dispatch(request: R): Response
}
