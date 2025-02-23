package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class PlayCardTest {


    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerActionService: PlayerActionService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        playerActionService = PlayerActionService(rootService)
    }

    @Test
    fun testPlayCard() {
        val name1 = "Player1"
        val name2 = "Player2"
        gameService.startNewGame(name1, name2)
        val game = rootService.currentGame
        checkNotNull(game)

        val playStackTopCard = Card(CardSuit.HEARTS, CardValue.KING)
        game.playStack1.clear()
        game.playStack1.add(0, playStackTopCard)
        val playStack = game.playStack1

        game.player1.hand.clear()
        game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.ACE))
        val validCard = game.player1.hand[0]


            val initialHandSize = game.player1.hand.size
            val initialPlayStackSize1 = playStack.size

            playerActionService.playCard(validCard, playStack)

            assertFalse(game.player1.hand.contains(validCard)) { "Die Karte sollte aus der Hand des Spielers entfernt werden" }
            assertEquals(initialHandSize - 1, game.player1.hand.size) { "Die Handgröße sollte um 1 verringert werden" }
            assertEquals(
                initialPlayStackSize1 + 1,
                playStack.size
            ) { "Die Größe des Spielstapels sollte um 1 erhöht werden" }
            assertEquals(validCard, playStack.first()) { "Die gespielte Karte sollte oben auf dem Stapel liegen" }
    }




        @Test
        fun testInvalidPlayCard() {
            val name1 = "Player1"
            val name2 = "Player2"
            gameService.startNewGame(name1, name2)
            val game = rootService.currentGame!!
            game.player1.hand.clear()
            game.player1.hand.add(Card(CardSuit.HEARTS, CardValue.KING))

            val invalidCard = Card(CardSuit.HEARTS, CardValue.TWO) // KARTE DIE NICHT IN PLAYER1.HAND LIEGT
            val exception = assertThrows<IllegalArgumentException> {
                playerActionService.playCard(invalidCard, game.playStack1)
            }

            assertEquals("you cant play this Card  ! , please choose a Card from you hand Deck", exception.message)
        }

        @Test
        fun testInvalidMove() {
            val name1 = "Player1"
            val name2 = "Player2"
            gameService.startNewGame(name1, name2)
            val game = rootService.currentGame!!

            val hand = game.player1.hand

            hand.clear()
            hand.add(Card(CardSuit.HEARTS, CardValue.KING))

            val playStack = game.player1.hand


            playStack.add(0,Card(CardSuit.HEARTS, CardValue.KING))


            val exception = assertThrows<IllegalArgumentException> {
                playerActionService.playCard(hand[0], playStack)
            }

            assertEquals("this is not a valid move!", exception.message)
        }
}
