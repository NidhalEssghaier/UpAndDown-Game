package service


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class UpAndDownGameTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
    }

    @Test
    fun testStartNewGame() {
        val name1 = "Player1"
        val name2 = "Player2"

        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame

        assertNotNull(game) { "Das Spiel sollte nicht null sein" }

        assertEquals(name1, game!!.player1.name)
        assertEquals(name2, game.player2.name)

        assertEquals(5, game.player1.hand.size)
        assertEquals(5, game.player2.hand.size)

        assertEquals(20, game.player1.drawStack.size)
        assertEquals(20, game.player2.drawStack.size)

        assertTrue(game.currentPlayer in listOf(1, 2))

        assertEquals(1, game.playStack1.size)
        assertEquals(1, game.playStack2.size)

    }
}