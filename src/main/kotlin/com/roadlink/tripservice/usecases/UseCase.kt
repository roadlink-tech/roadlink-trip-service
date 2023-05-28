package com.roadlink.tripservice.usecases

interface UseCase<I, O> {
    operator fun invoke(input: I): O
}