package entitiy

import entity.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class UpAndDownGameTest {

    val player1=Player("Test1")
    val player2=Player("Test2")
    val upAndDownGame = UpAndDownGame(player1,player2)
    var examplePassCounter = upAndDownGame.passCounter


    /**
     * Tests whether  stacks wich contains at least one card are not empty
     */
    @Test
    fun CheckIfPlaySatckSAreEmpty(){

        upAndDownGame.playStack1.add(Card(CardSuit.HEARTS , CardValue.ACE))
        upAndDownGame.playStack2.add(Card(CardSuit.HEARTS , CardValue.TWO))

        assert(!upAndDownGame.playStack1.isEmpty() || !upAndDownGame.playStack2.isEmpty())
    }

    /**
     * Tests if the pass counter starts at 0.
     */
    @Test
    fun CheckPassCounter() {
        assertEquals(0, examplePassCounter, "Passcounter should be 0 as declared")
    }
}