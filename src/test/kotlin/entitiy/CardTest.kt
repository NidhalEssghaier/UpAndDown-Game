package entitiy

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * tests for the `Card` class.
 *
 * This tests class verifies:
 * - The comparison of different card values.
 * - The correct representation of a card as a string.
 */

class CardTest {
    val exampleCard1 = Card(CardSuit.HEARTS, CardValue.TEN) //♥10
    val exampleCard2 = Card(CardSuit.HEARTS, CardValue.JACK) // ♥J


    /**
     * Tests that two cards with different values are not considered as equal.
     */

    @Test
    fun comparecards() {

        assertNotEquals(
            exampleCard1.value.ordinal, exampleCard2.value.ordinal,
            "exampleCards values should not be equal"
        )
    }


    /**
     * Tests the `toString()` method of the `Card` class.
     *
     * Ensures that the correct suit and value are returned as a string.
     */
    @Test
    fun CardSuitAndValueTest() {

        assertEquals(
            expected = "♥10",
            actual = exampleCard1.toString(),
            "should return the correct suit and Value of the Card  "
        )

        assertEquals(
            expected = "♥J",
            actual = exampleCard2.toString(),
            "should return the correct suit and Value of the Card  "
        )
    }


}
