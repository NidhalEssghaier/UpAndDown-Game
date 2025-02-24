package service

import entity.*
import org.junit.jupiter.api.Assertions.assertNull
import kotlin.test.assertFails
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals



/**
 * Test class for the pass action
 */
class PassTest {

    private lateinit var rootService: RootService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var game: UpAndDownGame

    /**
     * Initializes game and services before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        playerActionService = rootService.playerActionService
        game = UpAndDownGame(Player("player1"), Player("player2"))
        game.currentPlayer = 1
        rootService.currentGame = game
    }

    /**
     * Verifies that a valid pass works and updates the pass counter and current player.
     */
    @Test
    fun passValidTest() {
        val handCard = Card(CardSuit.SPADES, CardValue.SEVEN)
        game.player1.hand.add(handCard)
        game.playStack1.add(Card(CardSuit.CLUBS, CardValue.SEVEN))
        game.playStack2.add(Card(CardSuit.HEARTS, CardValue.SEVEN))
        rootService.playerActionService.pass()

        assert(game.passCounter == 1)
        assertEquals(game.currentPlayer, 2)
    }

    /**
     * Ensures pass fails if the player can still play a valid card.
     */
    @Test
    fun passAndCanPlayCardTest() {
        val handCard = Card(CardSuit.SPADES, CardValue.SEVEN)
        game.player1.hand.add(handCard)
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.SIX))
        game.playStack2.add(Card(CardSuit.CLUBS, CardValue.KING))

        assertFails { rootService.playerActionService.pass() }

        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.KING))
        game.playStack2.add(Card(CardSuit.CLUBS, CardValue.SIX))
        assertFails { rootService.playerActionService.pass() }
    }

    /**
     * Verifies that pass fails if the player can  draw a card.
     */
    @Test
    fun passAndCanDrawCardTest() {
        game.player1.hand.clear()
        val card = Card(CardSuit.HEARTS, CardValue.SEVEN)
        game.player1.drawStack.add(card)

        assertFails { rootService.playerActionService.pass() }
    }

    /**
     * Ensures that pass fails when the player can replace cards.
     */
    @Test
    fun passAndCanReplaceCardsTest() {
        game.player1.hand.clear()
        val card = Card(CardSuit.SPADES, CardValue.SEVEN)
        repeat(10) { game.player1.hand.add(card) }
        game.player1.drawStack.add(card)

        assertFails { rootService.playerActionService.pass() }
    }

    /**
     * Verifies that pass fails when no game is active.
     */
    @Test
    fun passWhenNoGameIsActive() {
        rootService.currentGame = null

        assertFails { rootService.playerActionService.pass() }
        assertThrows<IllegalStateException> { playerActionService.pass() }
        assertNull(rootService.currentGame)
    }
}
