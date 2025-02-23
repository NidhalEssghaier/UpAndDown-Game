package service
import entity.UpAndDownGame
import entity.Card
import entity.CardSuit
import entity.CardValue
import entity.Player
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertFails
import kotlin.test.assertEquals

class DrawCardTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerActionService: PlayerActionService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        playerActionService = PlayerActionService(rootService)

        // Initialize players and their draw stacks.
        val player1 = Player(name = "Player 1").apply {
            drawStack.add(Card(CardSuit.HEARTS, CardValue.ACE)) // Beispielkarte zum Ziehen.
            drawStack.add(Card(CardSuit.DIAMONDS, CardValue.TWO))
            drawStack.add(Card(CardSuit.CLUBS, CardValue.THREE))
        }

        val player2 = Player(name = "Player 2").apply {
            drawStack.add(Card(CardSuit.SPADES, CardValue.FOUR)) // Beispielkarte zum Ziehen.
        }

        // Create a new UpAndDownGame instance and set it in root service.
        val game = UpAndDownGame(player1, player2)

        rootService.currentGame = game

        // Set current player to Player 1.
        game.currentPlayer = 1
    }

    @Test
    fun testDrawCardSuccessfullyAddsCardToHand() {
        val currentGame = rootService.currentGame ?: throw IllegalStateException("No current game.")

        assertEquals(0, currentGame.player1.hand.size)

        assertDoesNotThrow {
            playerActionService.drawCard()
        }

        assertEquals(1, currentGame.player1.hand.size)
        assertEquals(2, currentGame.player1.drawStack.size)
        assertEquals(0, currentGame.passCounter)
    }



    @Test
        /**
         * Test for invalid action when trying to draw a card from an empty deck.
         * This test verifies that the drawCard action fails when there is no card to draw.
         */
   fun testInvalidDrawDeckAction1() {
     val rootService = RootService()
     val game = UpAndDownGame(
        player1 = Player("Player1"),
        player2 = Player("Player2")
      )

      rootService.currentGame = game
      assertFails{rootService.playerActionService.drawCard()}
   }

       /**
         * Test for invalid action when a player exceeds the draw limit.
         * This test checks that a player cannot draw more than the allowed number of cards.*/
     @Test fun testInvalidDrawDeckAction() {

           val name1 = "Player1"
           val name2 = "Player2"
           gameService.startNewGame(name1, name2)
           val game = rootService.currentGame
           kotlin.test.assertNotNull(game)

           for( i in 1..10 ){
            rootService.playerActionService.drawCard()
        }
        assertFails{ rootService.playerActionService.drawCard()}
     }
}