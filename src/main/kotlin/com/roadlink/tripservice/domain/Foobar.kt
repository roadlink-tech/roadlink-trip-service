package com.roadlink.tripservice.domain

import javax.persistence.Entity
import javax.persistence.Id

// TODO: temporary entity. delete when database adapter is in progress or finished
@Entity
class Foobar(
    @Id
    val foobar: String,
)
