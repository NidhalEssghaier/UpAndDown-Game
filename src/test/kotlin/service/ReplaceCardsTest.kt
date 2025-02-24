package service

import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

/**
 * Test class for testing replace Card functionality
 */
class ReplaceCardsTest {

    private lateinit var rootService: RootService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var game: UpAndDownGame
    private lateinit var refreshable: TestRefreshable

    /**
     * Initializes game and services before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        playerActionService = rootService.playerActionService
        game = UpAndDownGame(Player("player1"), Player("player2"))
        rootService.currentGame = game
        game.currentPlayer = 1
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)
    }

    /**
     * Tests successful card replacement when there are 8 cards in hand and cards in the draw stack.
     */
    @Test
    fun testReplaceCardsSuccessfully() {
        val card :Card = (Card(CardSuit.SPADES, CardValue.SEVEN))
        repeat(8){game.player1.hand.add(card)}
        game.player1.drawStack.add(0,card)
        assertDoesNotThrow {
            playerActionService.replaceCards()
        }
        assertEquals(5, game.player1.hand.size)
        assertEquals(4 , game.player1.drawStack.size)
        assertEquals(0 , rootService.currentGame!!.passCounter)
        assertTrue(refreshable.refreshAfterReplaceCardsCalled)
        refreshable.reset()
    }

    /**
     * Tests card replacement when the player has less than 8 cards in hand.
     */
    @Test
    fun testReplaceCardsWhenHandSizeLessThan8() {
        val card :Card = (Card(CardSuit.SPADES, CardValue.SEVEN))
        repeat(5){game.player1.hand.add(card)}
        game.player1.drawStack.add(0,card)
        val exception = assertThrows<IllegalStateException> {
            playerActionService.replaceCards()
        }
        assertEquals("you have less than 8 cards , you cant replace you cards", exception.message)
        assertFalse(refreshable.refreshAfterReplaceCardsCalled)
        refreshable.reset()
    }

    /**
     * Tests card replacement when the draw stack is empty.
     */
    @Test
    fun testReplaceCardsWhenDrawStackIsEmpty() {
        val card :Card = (Card(CardSuit.SPADES, CardValue.SEVEN))
        repeat(8){game.player1.hand.add(card)}
        val exception = assertThrows<IllegalStateException> {
            playerActionService.replaceCards()
        }
        assertEquals("you draw Deck is Empty , you cant replace you cards", exception.message)
        assertFalse(refreshable.refreshAfterReplaceCardsCalled)
        refreshable.reset()
    }

    /**
     * Tests card replacement when no game is active.
     */
    @Test
    fun testReplaceCardsWhenNoGameIsActive() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> { playerActionService.replaceCards() }
        assertNull(rootService.currentGame)
        assertFalse(refreshable.refreshAfterReplaceCardsCalled)
        refreshable.reset()
    }
}
