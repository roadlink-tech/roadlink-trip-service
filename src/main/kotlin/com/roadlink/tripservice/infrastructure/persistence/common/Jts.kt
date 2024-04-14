package com.roadlink.tripservice.infrastructure.persistence.common

import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel

object Jts {
    /*
    SRID (Spatial Reference System Identifier) 4326 refers to a specific coordinate reference system used to represent
     points on the surface of the Earth in latitude and longitude coordinates.
    More specifically, SRID 4326 corresponds to the WGS 84 coordinate system, which is commonly used as a standard
     for GPS and global mapping systems. In this system, coordinates are represented in decimal degrees, with latitude
      values ranging from -90 to 90 and longitude values ranging from -180 to 180.
     */
    val SRID = 4326

    val geometryFactory = GeometryFactory(PrecisionModel(), SRID)
}