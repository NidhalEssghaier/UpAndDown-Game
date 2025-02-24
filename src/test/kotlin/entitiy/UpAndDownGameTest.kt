package entitiy

import entity.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * UpAndDown game Test
 */
class UpAndDownGameTest {

    private val player1=Player("Test1")
    private val player2=Player("Test2")
    private val upAndDownGame = UpAndDownGame(player1,player2)
    private var examplePassCounter = upAndDownGame.passCounter


    /**
     * Tests whether  stacks wich contains at least one card are not empty
     */
    @Test
    fun checkIfPlayStacksAreEmpty(){

        upAndDownGame.playStack1.add(Card(CardSuit.HEARTS , CardValue.ACE))
        upAndDownGame.playStack2.add(Card(CardSuit.HEARTS , CardValue.TWO))

        assert(upAndDownGame.playStack1.isNotEmpty() || upAndDownGame.playStack2.isNotEmpty())
    }

    /**
     * Tests if the pass counter starts at 0.
     */
    @Test
    fun checkPassCounter() {
        assertEquals(0, examplePassCounter, "Passcounter should be 0 as declared")
    }
}

