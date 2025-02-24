package service

import entity.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

/**
 * Test class for testing play card functionality
 */
class PlayCardTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var game: UpAndDownGame
    private lateinit var refreshable: TestRefreshable

    /**
     * Initializes game and services before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = rootService.gameService
        playerActionService = rootService.playerActionService
        game = UpAndDownGame(Player("player1"), Player("player2"))
        rootService.currentGame = game
        refreshable = TestRefreshable()
        rootService.addRefreshable(refreshable)

    }

    /**
     * Verifies valid card play functionality with different scenarios.
     */
    @Test
    fun testPlayCard() {
        // Valid card play (different suits)
        val playStackTopCard = Card(CardSuit.HEARTS, CardValue.KING)
        game.playStack1.add(0, playStackTopCard)
        val validCard = Card(CardSuit.SPADES, CardValue.ACE)
        game.player1.hand.clear()
        game.player1.hand.add(0, validCard)
        game.currentPlayer = 1

        val initialHandSize = game.player1.hand.size
        val initialPlayStackSize1 = game.playStack1.size

        playerActionService.playCard(game.player1.hand[0], game.playStack1)

        assertTrue(game.player1.hand.isEmpty())

        assertFalse(game.player1.hand.contains(validCard)) {
            "Die Karte sollte aus der Hand des Spielers entfernt werden"
        }
        assertEquals(initialHandSize - 1, game.player1.hand.size) {
            "Die Handgröße sollte um 1 verringert werden"
        }
        assertEquals(
            initialPlayStackSize1 + 1,
            game.playStack1.size
        ) { "Die Größe des Spielstapels sollte um 1 erhöht werden" }
        assertEquals(validCard, game.playStack1.first()) { "Die gespielte Karte sollte oben auf dem Stapel liegen" }
        assertTrue(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }

    /**
     * Verifies valid card play functionality with edge case.
     */
    @Test
    fun edgeCaseTest() {


        game.currentPlayer = 1
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.KING))
        val validCard = Card(CardSuit.HEARTS, CardValue.TWO)
        game.player1.hand.add(0, validCard)
        val initialHandSize = game.player1.hand.size
        val initialPlayStackSize1 = game.playStack1.size
        playerActionService.playCard(game.player1.hand[0], game.playStack1)
        assertTrue(game.player1.hand.isEmpty())
        assertFalse(game.player1.hand.contains(validCard)) {
            "Die Karte sollte aus der Hand des Spielers entfernt werden"
        }
        assertEquals(initialHandSize-1 , game.player1.hand.size) {
            "Die Handgröße sollte um 1 verringert werden"
        }
        assertEquals(
            initialPlayStackSize1 + 1,
            game.playStack1.size
        ) { "Die Größe des Spielstapels sollte um 1 erhöht werden" }
        assertEquals(validCard, game.playStack1.first()) { "Die gespielte Karte sollte oben auf dem Stapel liegen" }
        assertTrue(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }

    /**
     * Verifies valid card play functionality with same suits and no edge case.
     */
    @Test
        fun sameSuitNotEdgeCaseTest() {

            game.currentPlayer = 1
            val validCard = Card(CardSuit.HEARTS, CardValue.FOUR)
            game.player1.hand.add(0, validCard)
            game.playStack1.add(Card(CardSuit.HEARTS, CardValue.TWO))
            playerActionService.playCard(game.player1.hand[0], game.playStack1)
            assertFalse(game.player1.hand.contains(validCard))
            assertTrue(refreshable.refreshAfterPlayCardCalled)
            refreshable.reset()
        }

    /**
     * Verifies valid card play functionality with different suits and no edge case.
     */
    @Test
    fun differentSuitNotEdgeCaseTest() {

        game.currentPlayer=1
        val validCard = Card(CardSuit.SPADES, CardValue.FIVE)
        game.player1.hand.add(0,validCard)
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.FOUR))
        playerActionService.playCard(game.player1.hand[0], game.playStack1)
        assertFalse(game.player1.hand.contains(validCard))
        assertTrue(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }

    /**
     * Tests invalid card play where the played card is not in the player's hand.
     */
    @Test
    fun testInvalidPlayCard() {

        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))
        game.currentPlayer = 1
        val invalidCard = Card(CardSuit.HEARTS, CardValue.TWO) // CARD NOT IN PLAYER1'S HAND
        val exception = assertThrows<IllegalArgumentException> {
            playerActionService.playCard(invalidCard, game.playStack1)
        }
        assertEquals("you cant play this Card  ! , please choose a Card from you hand Deck",
                     exception.message)
        assertFalse(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }

    /**
     * Verifies invalid moves based on  card values and suits.
     */
    @Test
    fun testInvalidMove() {
        // Same suit and same value
        game.currentPlayer = 1
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))

        val playStack = game.player1.hand
        //playStack.add(0, Card(CardSuit.HEARTS, CardValue.KING))
        val exception = assertThrows<IllegalArgumentException> {
            playerActionService.playCard(game.player1.hand[0], playStack)
        }
        assertEquals("this is not a valid move!", exception.message)
        assertFalse(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()


        // Card with same suits but invalid value
        game.player1.hand.add(0, Card(CardSuit.HEARTS, CardValue.THREE))
        game.playStack1.add(0, Card(CardSuit.HEARTS, CardValue.SIX))

        val exception2 = assertThrows<IllegalArgumentException> {
            playerActionService.playCard(game.player1.hand[0], game.playStack1)
        }
        assertEquals("this is not a valid move!", exception2.message)
        assertFalse(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()

        // Card with same values
        game.player1.hand.add(0, Card(CardSuit.HEARTS, CardValue.THREE))
        game.playStack1.add(0, Card(CardSuit.SPADES, CardValue.THREE))

        val exception3 = assertThrows<IllegalArgumentException> {
            playerActionService.playCard(game.player1.hand[0], game.playStack1)
        }
        assertEquals("this is not a valid move!", exception3.message)
        assertFalse(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }

    /**
     * Verifies that attempting to play a card  when there is no active throws an error.
     */
    @Test
    fun testPlayCardWithNoActiveGame() {
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.JACK)
        rootService.currentGame = null
        assertThrows<IllegalStateException> { playerActionService.playCard(cardToPlay, game.playStack1) }
        assertFalse(refreshable.refreshAfterPlayCardCalled)
        refreshable.reset()
    }
}
