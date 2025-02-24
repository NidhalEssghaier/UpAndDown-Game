package service
import entity.UpAndDownGame
import entity.Card
import entity.CardSuit
import entity.CardValue
import entity.Player
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import kotlin.test.assertFails
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class DrawCardTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var game: UpAndDownGame


    /**
     *  initialize [RootService] and [GameService] and [playerActionService] before each Test
     *  initialize an [UpAndDownGame] before each Test
     *  set Current Player to 1
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        playerActionService = rootService.playerActionService
        game = UpAndDownGame(Player("player1"), Player("player2"))
        game.currentPlayer = 1
        rootService.currentGame = game
    }

    /**
     * Tests whether drawing a card successfully adds the card to the player's hand.
     */
    @Test
    fun testDrawCardSuccessfullyAddsCardToHand() {
        val cardToDraw: Card = (Card(CardSuit.HEARTS, CardValue.ACE))
        game.player1.drawStack.add(0, cardToDraw)
        assertEquals(0, game.player1.hand.size)

        assertDoesNotThrow {
            playerActionService.drawCard()
        }

        assertEquals(1, game.player1.hand.size)
        assertEquals(0, game.player1.drawStack.size)
        assertEquals(0, game.passCounter)
        assertTrue(game.player1.hand.contains(cardToDraw))
        assertFalse(game.player1.drawStack.contains(cardToDraw))
    }

    /**
     * Tests drawing a card from an empty deck
     */
    @Test
    fun testDrawCardWhileDeckEmpty() {
        val exception = assertThrows<IllegalStateException> { playerActionService.drawCard() }
        assertEquals("you dont have any cards left to draw", exception.message)
        assertFails { playerActionService.drawCard() }
    }

    /**
     * Tests an invalid draw action when the player's hand already has more than 9 cards.
     */
    @Test
    fun testInvalidDrawDeckAction() {
        val card: Card = (Card(CardSuit.SPADES, CardValue.SEVEN))
        game.player1.drawStack.add(0, card)
        for (i in 1..10) {
            game.player1.hand.add(card)
        }
        val exception = assertThrows<IllegalStateException> { playerActionService.drawCard() }
        assertEquals(
            "you can´t draw cards ! you have more than 9 card , please replace you cards",
            exception.message
        )
        assertFails { playerActionService.drawCard() }
    }

    /**
     * Tests drawing a card when there is no active game
     */
    @Test
    fun testDrawCardWhenNoGameIsActive() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> { playerActionService.drawCard() }
        assertNull(rootService.currentGame)
    }
}
