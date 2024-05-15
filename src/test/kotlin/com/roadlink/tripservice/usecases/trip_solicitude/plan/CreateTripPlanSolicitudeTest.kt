package com.roadlink.tripservice.usecases.trip_solicitude.plan

import com.roadlink.tripservice.domain.trip.TripRepository
import com.roadlink.tripservice.domain.trip.constraint.Visibility
import com.roadlink.tripservice.domain.trip.section.SectionRepository
import com.roadlink.tripservice.domain.trip_solicitude.TripPlanSolicitudeRepository
import com.roadlink.tripservice.domain.user.User
import com.roadlink.tripservice.domain.user.UserRepository
import com.roadlink.tripservice.usecases.trip.SectionFactory
import com.roadlink.tripservice.usecases.trip.TripFactory
import com.roadlink.tripservice.usecases.trip_solicitude.TripLegSolicitudeFactory
import com.roadlink.tripservice.usecases.user.UserFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class CreateTripPlanSolicitudeTest {

    @MockK
    lateinit var sectionRepository: SectionRepository

    @MockK
    lateinit var tripPlanSolicitudeRepository: TripPlanSolicitudeRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var tripRepository: TripRepository

    private lateinit var createTripPlanSolicitude: CreateTripPlanSolicitude

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        createTripPlanSolicitude =
            CreateTripPlanSolicitude(
                sectionRepository,
                tripRepository,
                userRepository,
                tripPlanSolicitudeRepository
            )
    }

    @Test
    fun `when all the requested sections can receive the passenger, then the plan must be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800()
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId)
        val oneTripId = UUID.randomUUID()
        val tripPlanSolicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = oneTripId.toString(),
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = oneTripId.toString(),
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        val trip = TripFactory.common()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        every { tripRepository.find(any()) } returns listOf(trip)
        every { userRepository.findByUserId(id = george.id) } returns george
        every { tripPlanSolicitudeRepository.find(any()) } returns emptyList()

        // WHEN
        val output = createTripPlanSolicitude(tripPlanSolicitude)

        // THEN
        thenTheTripPlanWasCreated(output)
        thenTheTripPlanWasSaved()
    }

    @Test
    fun `when all the requested sections can receive the passenger but the passenger has already sent a solicitude, then an error must be retrieved`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800()
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId)
        val tripId = UUID.randomUUID()
        val tripPlanSolicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = tripId.toString(),
                    sectionsIds = setOf(sectionOne.id)
                )
            )
        )

        val trip = TripFactory.common()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        every { tripRepository.find(any()) } returns listOf(trip)
        every { userRepository.findByUserId(id = george.id) } returns george
        every {
            tripPlanSolicitudeRepository.find(
                commandQuery = TripPlanSolicitudeRepository.CommandQuery(
                    passengerId = UUID.fromString(george.id),
                    tripIds = listOf(tripId)
                )
            )
        } returns listOf(
            TripPlanSolicitudeFactory.common(
                tripLegSolicitudes = listOf(
                    TripLegSolicitudeFactory.common(
                        sections = listOf(
                            SectionFactory.withDriver(
                                driverId = UUID.randomUUID(),
                                tripId = tripId
                            )
                        )
                    )
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(tripPlanSolicitude)

        // THEN
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.TripPlanSolicitudeAlreadySent::class.java,
            output
        )
        thenTheTripPlanWasNotSaved()
    }

    @Test
    fun `when all the requested sections can receive the passenger and the passenger has sent a solicitude but to a different plan, then the plan must be created`() {
        // GIVEN
        val tripId = UUID.randomUUID()
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800(tripId = tripId)
        val otherTripID = UUID.randomUUID()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800(tripId = otherTripID)

        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId)
        val tripPlanSolicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = sectionOne.tripId.toString(),
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = sectionTwo.tripId.toString(),
                    sectionsIds = setOf(sectionTwo.id)
                )
            )
        )

        val trip = TripFactory.common()
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        every { tripRepository.find(any()) } returns listOf(trip)
        every { userRepository.findByUserId(id = george.id) } returns george
        every {
            tripPlanSolicitudeRepository.find(
                commandQuery = TripPlanSolicitudeRepository.CommandQuery(
                    passengerId = UUID.fromString(george.id),
                    tripIds = listOf(sectionOne.tripId, sectionTwo.tripId)
                )
            )
        } returns listOf(
            TripPlanSolicitudeFactory.common(
                tripLegSolicitudes = listOf(
                    TripLegSolicitudeFactory.common(
                        sections = listOf(
                            SectionFactory.withDriver(
                                driverId = UUID.randomUUID(),
                                tripId = sectionOne.tripId
                            ),
                            SectionFactory.withDriver(
                                driverId = UUID.randomUUID(),
                                tripId = sectionTwo.tripId
                            )
                        )
                    )
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(tripPlanSolicitude)

        // THEN
        thenTheTripPlanWasCreated(output)
        thenTheTripPlanWasSaved()
    }

    @Test
    fun `when there is a section which can not receive the passenger, then the plan must not be created`() {
        // GIVEN
        val sectionOne = SectionFactory.avCabildo4853_virreyDelPino1800()
        val sectionTwo = SectionFactory.avCabildo1621_virreyDelPino1800_completed()
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId)
        val trip = TripFactory.common()

        every { tripRepository.find(any()) } returns listOf(trip)
        every { userRepository.findByUserId(id = george.id) } returns george
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne, sectionTwo)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        every { tripPlanSolicitudeRepository.find(any()) } returns emptyList()

        val application = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
                    sectionsIds = setOf(sectionOne.id)
                ),
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
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

    @Test
    fun `when the trip only admits friends but driver and passenger are not friends, then an error must be retrieved`() {
        // GIVEN
        val trip = TripFactory.common(restrictions = listOf(Visibility.OnlyFriends))
        val sectionOne =
            SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(trip.id))
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId)

        every {
            tripRepository.find(
                TripRepository.CommandQuery(
                    ids = listOf(
                        sectionOne.tripId
                    )
                )
            )
        } returns listOf(trip)

        every { userRepository.findByUserId(id = george.id) } returns george
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs

        val solicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
                    sectionsIds = setOf(sectionOne.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(solicitude)

        // THEN
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.UserIsNotCompliantForJoiningTrip::class.java,
            output
        )
        thenTheTripPlanWasNotSaved()
    }

    @Test
    fun `when the trip only admits women but driver is not a women, then an error must be retrieved`() {
        // GIVEN
        val trip = TripFactory.common(restrictions = listOf(Visibility.OnlyWomen))
        val sectionOne =
            SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(trip.id))
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId, gender = User.Gender.X)

        every {
            tripRepository.find(
                TripRepository.CommandQuery(
                    ids = listOf(
                        sectionOne.tripId
                    )
                )
            )
        } returns listOf(trip)

        every { userRepository.findByUserId(id = george.id) } returns george
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs

        val solicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
                    sectionsIds = setOf(sectionOne.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(solicitude)

        // THEN
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.UserIsNotCompliantForJoiningTrip::class.java,
            output
        )
        thenTheTripPlanWasNotSaved()
    }

    @Test
    fun `when the trip only admits friends women but driver is not a driver friend, then an error must be retrieved`() {
        // GIVEN
        val trip =
            TripFactory.common(restrictions = listOf(Visibility.OnlyWomen, Visibility.OnlyFriends))
        val sectionOne =
            SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(trip.id))
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(id = passengerId, gender = User.Gender.Female)

        every {
            tripRepository.find(
                TripRepository.CommandQuery(
                    ids = listOf(
                        sectionOne.tripId
                    )
                )
            )
        } returns listOf(trip)

        every { userRepository.findByUserId(id = george.id) } returns george
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs

        val solicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
                    sectionsIds = setOf(sectionOne.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(solicitude)

        // THEN
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.UserIsNotCompliantForJoiningTrip::class.java,
            output
        )
        thenTheTripPlanWasNotSaved()
    }

    @Test
    fun `when the trip only admits friends women and driver and passenger are friends, then the plan must be created`() {
        // GIVEN
        val trip =
            TripFactory.common(restrictions = listOf(Visibility.OnlyWomen, Visibility.OnlyFriends))
        val sectionOne =
            SectionFactory.avCabildo4853_virreyDelPino1800(tripId = UUID.fromString(trip.id))
        val passengerId = UUID.randomUUID()
        val george = UserFactory.common(
            id = passengerId,
            gender = User.Gender.Female,
            friendsIds = setOf(UUID.fromString(trip.driverId))
        )

        every {
            tripRepository.find(
                TripRepository.CommandQuery(
                    ids = listOf(
                        sectionOne.tripId
                    )
                )
            )
        } returns listOf(trip)
        every { userRepository.findByUserId(id = george.id) } returns george
        every { sectionRepository.findAllById(any()) } returns listOf(sectionOne)
        every { tripPlanSolicitudeRepository.insert(any()) } just runs
        every { tripPlanSolicitudeRepository.find(any()) } returns emptyList()

        val solicitude = CreateTripPlanSolicitude.Input(
            passengerId = george.id,
            tripSections = listOf(
                CreateTripPlanSolicitude.Input.TripSections(
                    tripId = trip.id,
                    sectionsIds = setOf(sectionOne.id)
                )
            )
        )

        // WHEN
        val output = createTripPlanSolicitude(solicitude)

        // THEN
        thenTheTripPlanWasCreated(output)
        thenTheTripPlanWasSaved()
    }

    private fun thenTheTripPlanWasSaved() {
        verify { tripPlanSolicitudeRepository.insert(any()) }
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
        assertInstanceOf(
            CreateTripPlanSolicitude.Output.TripPlanSolicitudeCreated::class.java,
            output
        )
    }
}