package service

import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test

/**
 * Class for testing multiple different methods of the [CardService].
 */
class RootServiceTest {
    /** Test adding a single Refreshable to the RootService. */
    @Test
    fun testAddRefreshable() {
        val rootService = RootService()
        val testRefreshable = TestRefreshable()

        // Test: The Refreshable is added without an error
        assertDoesNotThrow { rootService.addRefreshable(testRefreshable) }
    }

    /** Test adding multiple Refreshables to the RootService. */
    @Test
    fun testAddMultipleRefreshables() {
        val rootService = RootService()
        val testRefreshable1 = TestRefreshable()
        val testRefreshable2 = TestRefreshable()

        // Test: The Refreshables are added without an error
        assertDoesNotThrow { rootService.addRefreshables(testRefreshable1, testRefreshable2) }
    }
}


