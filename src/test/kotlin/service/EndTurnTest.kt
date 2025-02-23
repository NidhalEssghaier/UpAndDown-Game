package service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test





class EndTurnTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
    }

    @Test
    fun testEndTurnSwitchesPlayer() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame ?: fail("The game should be started.")
        game.currentPlayer = 1

        gameService.endTurn()

        assertEquals(2, game.currentPlayer) { "The active player should now be Player 2." }
    }

    @Test
    fun testEndTurnEndsGameWhenBothHandsEmpty() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame ?: fail("The game should be started.")

        game.currentPlayer = 1
        game.player1.hand.clear()
        game.player1.drawStack.clear()

        game.player2.hand.clear()
        game.player2.drawStack.clear()

        assertDoesNotThrow {
            gameService.endTurn()
        }

        assertNull(rootService.currentGame) { "The game should be over after this turn." }
    }

    @Test
    fun testEndTurnThrowsExceptionIfNoGameStarted() {
        val exception = assertThrows<IllegalStateException> {
            gameService.endTurn()
        }

        assertEquals("No game started yet.", exception.message)
    }

    @Test
    fun testEndTurnEndsGameWhenPassCounterIsTwo() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame ?: fail("The game should be started.")

        game.passCounter = 2

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.ACE))
        game.player2.hand.add(Card(CardSuit.SPADES , CardValue.KING))

        game.currentPlayer = 1

        assertDoesNotThrow {
            gameService.endTurn()
        }

        assertNull(rootService.currentGame) { "The game should be over after this turn." }


    }
}