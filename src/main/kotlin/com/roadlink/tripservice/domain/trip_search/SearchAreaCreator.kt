package com.roadlink.tripservice.domain.trip_search

import com.roadlink.tripservice.domain.common.Location
import com.roadlink.tripservice.infrastructure.persistence.common.Jts
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.PrecisionModel
import org.locationtech.jts.util.GeometricShapeFactory

interface SearchFigure

interface SearchAreaCreator<T: SearchFigure> {
    fun from(location: Location, radiusInMeters: Double): T
}

class JtsCircle(val value: Polygon) : SearchFigure

class JtsSearchCircleCreator : SearchAreaCreator<JtsCircle> {

    private val geometryFactory = GeometryFactory(PrecisionModel(), Jts.SRID)
    private val geometricShapeFactory = GeometricShapeFactory(geometryFactory)
    private val nearSearchCircleNumPoints = 32

    override fun from(location: Location, radiusInMeters: Double): JtsCircle {
        val coordinate = Coordinate(location.longitude, location.latitude)
        geometricShapeFactory.setCentre(coordinate)
        geometricShapeFactory.setSize(2 * toDegrees(radiusInMeters))
        geometricShapeFactory.setNumPoints(nearSearchCircleNumPoints)
        return JtsCircle(geometricShapeFactory.createCircle())
    }

    private fun toDegrees(meters: Double): Double =
        meters / 110000.0

}