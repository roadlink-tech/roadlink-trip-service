package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.usecases.trip.SectionFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateTripPlanSolicitudeTest {

    @MockK
    lateinit var sectionRepository: SectionRepository

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    private lateinit var createTripPlanSolicitude: CreateTripPlanSolicitude

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createTripPlanSolicitude = CreateTripPlanSolicitude(sectionRepository, tripPlanSolicitudeRepository)
    }

    @Test
    fun `when all the request sections can receive the passenger, then the plan must be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        val application = CreateTripPlanSolicitude.Input(
            passengerId = "chorch",
            trips = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = "1",
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = "2",
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(application)

        // THEN
        thenTheTripPlanWasCreated(output)
        thenTheTripPlanWasSaved()
    }

    private fun thenTheTripPlanWasSaved() {
        verify { tripPlanSolicitudeRepository.insert(any()) }
    }

    @Test
    fun `when there is a section which can not receive the passenger, then the plan must not be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800_completed()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        val application = CreateTripPlanSolicitude.Input(
            passengerId = "chorch",
            trips = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = "1",
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = "2",
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(application)

        // THEN
        thenTheTripPlanCouldNotBeCreated(output)
        thenTheTripPlanWasNotSaved()
    }

    private fun thenTheTripPlanWasNotSaved() {
        verify(exactly = 0) { tripPlanSolicitudeRepository.insert(any()) }
    }

    private fun thenTheTripPlanCouldNotBeCreated(output: CreateTripPlanSolicitude.Output) {
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.OneOfTheSectionCanNotReceivePassenger::class.java,
            output
        )
    }

    private fun thenTheTripPlanWasCreated(output: CreateTripPlanSolicitude.Output) {
        extracted(output)
    }

    private fun extracted(output: CreateTripPlanSolicitude.Output) {
        assertInstanceOf(CreateTripPlanSolicitude.Output.TripPlanSolicitudeCreated::class.java, output)
    }
}