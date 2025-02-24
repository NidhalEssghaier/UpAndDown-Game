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



    @Test
    fun `player tester` () {

        assertEquals(
            expected = "player1: D[♥A] H[♦2]",
            actual = player.toString(),
            "should return the exact player´s name, drawStack and hand that are declared  "
        )
    }

}


