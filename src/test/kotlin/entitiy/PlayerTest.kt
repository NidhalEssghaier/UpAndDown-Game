package entitiy

import entity.Player
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *  tests for the `Player` class.
 *
 * This test class verifies:
 * - The correct initialization of a player.
 * - The correct representation of the player's state.
 */
class PlayerTest {

    private val player = Player("player1")


    /** test richtige name Zuweisung **/
    @Test
    fun `player tester` () {

        assertEquals(
            expected = "player1",
            actual = player.name,
            "should return the exact player´s name, drawStack and hand that are declared  "
        )
    }

}


