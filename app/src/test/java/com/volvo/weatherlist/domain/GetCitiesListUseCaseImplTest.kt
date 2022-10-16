package com.volvo.weatherlist.domain

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class GetCitiesListUseCaseImplTest {

    @InjectMocks
    private lateinit var useCase: GetCitiesListUseCaseImpl

    @Test
    fun `execute - return list of cities - always`() {
        val actual = useCase.execute()

        assertEquals(Cities.values().map { it.cityName }, actual)
    }
}