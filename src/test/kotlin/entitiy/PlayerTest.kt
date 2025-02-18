package entitiy

import entity.Card
import entity.CardSuit
import entity.CardValue
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

class PlayerTest() {

    val player = Player("Nidhal")
    val drawSatck = player.drawStack.add(Card(CardSuit.HEARTS, CardValue.ACE))  // ♥A
    val hand = player.hand.add(Card(CardSuit.DIAMONDS, CardValue.TWO))          // ♦2


    @Test
    fun `player tester` () {

        assertEquals(
            expected = "Nidhal: D[♥A] H[♦2]",
            actual = player.toString(),
            "should return the exact player´s name, drawStack and hand that are declared  "
        )
    }

}


