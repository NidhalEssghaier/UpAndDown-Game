package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import entity.Player
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class PlayerActionServiceTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerActionService: PlayerActionService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        playerActionService = PlayerActionService(rootService)

        gameService.startNewGame("player1", "player2")

        val player1 = rootService.currentGame!!.player1
        val player2 = rootService.currentGame!!.player2

        player1.hand.clear()

        player1.hand.addAll(listOf(
            Card(CardSuit.HEARTS, CardValue.ACE),
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.HEARTS, CardValue.THREE),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.DIAMONDS, CardValue.SIX),
            Card(CardSuit.CLUBS, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.EIGHT)
        ))

        player1.drawStack.clear()
        player1.drawStack.addAll(listOf(
            Card(CardSuit.DIAMONDS, CardValue.NINE),
            Card(CardSuit.DIAMONDS, CardValue.TEN)
        ))

        player2.hand.clear()

        player2.hand.addAll(listOf(
            Card(CardSuit.HEARTS, CardValue.ACE),
            Card(CardSuit.HEARTS, CardValue.TWO),

        ))

        player2.drawStack.clear()
        player2.drawStack.addAll(listOf(
            Card(CardSuit.DIAMONDS, CardValue.NINE),
            Card(CardSuit.DIAMONDS, CardValue.TEN)
        ))


    }

    @Test
    fun testReplaceCardsSuccessfullyUpdatesHandAndDrawStack() {
        val currentPlayer = rootService.currentGame!!.player1

        assertDoesNotThrow {
            playerActionService.replaceCards()
        }

        assertEquals(5, currentPlayer.hand.size)
        assertEquals(5 , currentPlayer.drawStack.size)
        assertEquals(0 , rootService.currentGame!!.passCounter)
    }

    /*@Test
    fun testReplaceCardsThrowsExceptionIfCannotReplace() {
        val currentPlayer = rootService.currentGame!!.player2

        val exception = assertThrows<IllegalArgumentException> {
            playerActionService.replaceCards()
        }

        assertEquals("you have less than 8 cards , you cant replace you cards", exception.message)
    }*/
}