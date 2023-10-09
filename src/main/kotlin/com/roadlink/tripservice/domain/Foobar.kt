package com.roadlink.tripservice.domain

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import jakarta.persistence.Entity
import jakarta.persistence.Id

// TODO: temporary entity. delete when database adapter is in progress or finished
@Entity
class Foobar(
    @Id
    val id: String,
)

@Repository
interface FoobarRepository : CrudRepository<Foobar, String> {
    fun save(foobar: Foobar): Foobar
}
