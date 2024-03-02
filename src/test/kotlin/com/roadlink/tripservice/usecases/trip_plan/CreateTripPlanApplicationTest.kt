package com.roadlink.tripservice.usecases.trip_plan

import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_application.TripPlanApplicationRepository
import com.roadlink.tripservice.usecases.trip.domain.SectionFactory
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplication
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationInput
import com.roadlink.tripservice.usecases.trip_application.plan.CreateTripPlanApplicationOutput
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateTripPlanApplicationTest {

    @MockK
    lateinit var sectionRepository: SectionRepository

    @MockK
    lateinit var tripPlanApplicationRepository: TripPlanApplicationRepository

    private lateinit var createTripPlanApplication: CreateTripPlanApplication

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createTripPlanApplication = CreateTripPlanApplication(sectionRepository, tripPlanApplicationRepository)
    }

    @Test
    fun `when all the request sections can receive the passenger, then the plan must be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanApplicationRepository.insert(any()) } just runs
        val application = CreateTripPlanApplicationInput(
            passengerId = "chorch",
            trips = listOf(
                CreateTripPlanApplicationInput.TripSections(
                    tripId = "1",
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanApplicationInput.TripSections(
                    tripId = "2",
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanApplication(application)

        // THEN
        thenTheTripPlanWasCreated(output)
        thenTheTripPlanWasSaved()
    }

    private fun thenTheTripPlanWasSaved() {
        verify { tripPlanApplicationRepository.insert(any()) }
    }

    @Test
    fun `when there is a section which can not receive the passenger, then the plan must not be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800_completed()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanApplicationRepository.insert(any()) } just runs
        val application = CreateTripPlanApplicationInput(
            passengerId = "chorch",
            trips = listOf(
                CreateTripPlanApplicationInput.TripSections(
                    tripId = "1",
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanApplicationInput.TripSections(
                    tripId = "2",
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanApplication(application)

        // THEN
        thenTheTripPlanCouldNotBeCreated(output)
        thenTheTripPlanWasNotSaved()
    }

    private fun thenTheTripPlanWasNotSaved() {
        verify(exactly = 0) { tripPlanApplicationRepository.insert(any()) }
    }

    private fun thenTheTripPlanCouldNotBeCreated(output: CreateTripPlanApplicationOutput) {
        assertInstanceOf(
            CreateTripPlanApplicationOutput.OneOfTheSectionCanNotReceivePassenger::class.java,
            output
        )
    }

    private fun thenTheTripPlanWasCreated(output: CreateTripPlanApplicationOutput) {
        extracted(output)
    }

    private fun extracted(output: CreateTripPlanApplicationOutput) {
        assertInstanceOf(CreateTripPlanApplicationOutput.TripPlanApplicationCreated::class.java, output)
    }
}