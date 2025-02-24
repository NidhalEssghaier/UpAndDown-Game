package service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for the [GameService.startNewGame] method.
 * - correct assignment of player names.
 * - Correct distribution of hand and draw cards.
 * - Proper initialization of the game state.
 */
class StartNewGameTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var refreshable: TestRefreshable

    /**
     * initialize [RootService] and [GameService] before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = rootService.gameService // what is the difference between this and "GameService(rootService)" ???
        // , when I do "GameService(rootService)" assertTrue(refreshable.refreshAfter...) fails !
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)
    }

    /**
     * Tests the successful initialization of a new game with valid player names.
     */
    @Test
    fun testStartNewGame() {

        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame
        assertNotNull(game, "Das Spiel sollte nicht null sein")
        assertEquals(name1, game!!.player1.name)
        assertEquals(name2, game.player2.name)
        assertEquals(5, game.player1.hand.size)
        assertEquals(5, game.player2.hand.size)
        assertEquals(20, game.player1.drawStack.size)
        assertEquals(20, game.player2.drawStack.size)
        assertTrue(game.currentPlayer in listOf(1, 2))
        assertEquals(1, game.playStack1.size)
        assertEquals(1, game.playStack2.size)
        assertTrue(refreshable.refreshAfterStartNewGameCalled)
        refreshable.reset()

    }

    /**
    * test if  application refreshes scene when players turn starts
    *  */
    @Test
    fun testStartTurn() {
        gameService.startTurn()
        assertTrue(refreshable.refreshAfterStartTurnCalled)
        refreshable.reset()
    }
    /**
     * test if  application refreshes scene for return to game menu
     *  */
    @Test
    fun testExitToMainMenu() {
        gameService.exitToMainMenu()
        assertTrue(refreshable.refreshAfterReturnToMenuCalled)
        refreshable.reset()
    }
    /**
     * test if  application refreshes scene for return to game menu
     *  */
    @Test
    fun testCancelGame() {
        gameService.cancelGame()
        kotlin.test.assertNull(rootService.currentGame)
        assertTrue(refreshable.refreshAfterCancelGameCalled)
        refreshable.reset()
    }
    /**
     * test if  application refreshes scene after exiting game
     *  */
    @Test
    fun testExitGame() {
        gameService.exitGame()
        assertTrue(refreshable.refreshAfterExitGameCalled)
        refreshable.reset()
    }

}
