package service
import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Unit tests for the [GameService.endTurn] method.
 *
 */
class EndTurnTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var refreshable: TestRefreshable

    /**
     *  initialize [RootService] and [GameService] before each Test
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = rootService.gameService
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)
    }

    /**
     * Tests that calling [GameService.endTurn] correctly switches the active player.
     */
    @Test
    fun testEndTurnSwitchesPlayer() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame ?: fail("The game should be started.")
        game.currentPlayer = 1

        gameService.endTurn()

        assertEquals(2, game.currentPlayer) { "The active player should now be Player 2." }
        assertTrue(refreshable.refreshAfterEndTurnCalled)
        refreshable.reset()
    }


    /**
     * Tests that the game ends when both players' hands and draw stacks are empty.
     */
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
        assertTrue(refreshable.refreshAfterEndGameCalled)
        refreshable.reset()
    }

    /**
     * Tests that calling [GameService.endTurn] without a started game throws an exception.
     */
    @Test
    fun testEndTurnThrowsExceptionIfNoGameStarted() {
        val exception = assertThrows<IllegalStateException> {
            gameService.endTurn()
        }
        assertEquals("No game started yet.", exception.message)
        assertNull(rootService.currentGame)
    }

    /**
     * Tests that the game ends when the pass counter reaches two.
     */
    @Test
    fun testPassCounterIsTwoBothWins() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)
        val game = rootService.currentGame ?: fail("The game should be started.")
        game.passCounter = 2

        assertDoesNotThrow {
            gameService.endTurn()
        }
        assertNull(rootService.currentGame) { "The game should be over after this turn." }
        assertTrue(refreshable.refreshAfterEndGameCalled)
        refreshable.reset()
    }
    /**
     * Tests that the game ends when the pass counter reaches two and the active player has fewer card in hand.
     * active player should win
     */
    @Test
    fun testPassCounterIsTwoActivePlayerWins() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)
        val game = rootService.currentGame ?: fail("The game should be started.")
        game.currentPlayer = 1
        val waitingPlayer=game.player2
        waitingPlayer.hand.add(Card(CardSuit.SPADES, CardValue.SEVEN))
        game.passCounter = 2

        assertDoesNotThrow {
            gameService.endTurn()
        }
        assertNull(rootService.currentGame) { "The game should be over after this turn." }
        assertTrue(refreshable.refreshAfterEndGameCalled)
        refreshable.reset()
    }

    /**
     * Tests that the game ends when the pass counter reaches two and the waiting player has fewer card in hand.
     * waiting player should win
     */
    @Test
    fun testPassCounterIsTwoWaitingPlayerWins() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)
        val game = rootService.currentGame ?: fail("The game should be started.")
        game.currentPlayer = 1
        
        game.player1.hand.add(Card(CardSuit.SPADES, CardValue.SEVEN))
        game.passCounter = 2

        assertDoesNotThrow {
            gameService.endTurn()
        }
        assertNull(rootService.currentGame) { "The game should be over after this turn." }
        assertTrue(refreshable.refreshAfterEndGameCalled)
        refreshable.reset()
    }
}
