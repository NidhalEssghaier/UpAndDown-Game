package service

import entity.Card
import entity.CardSuit
import entity.CardValue
import kotlin.test.assertFails
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test



class PassTest {

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
    fun passValidTest(){

        val name1 = "Player1"
        val name2 = "Player2"
        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer = if (game.currentPlayer==1) game.player1 else game.player2
        currentPlayer.hand.clear()
        val sublist = listOf(
            Card(CardSuit.HEARTS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.FOUR)
        )
        currentPlayer.hand.addAll(sublist)
        currentPlayer.drawStack.clear()
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.KING))
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.KING))

        rootService.playerActionService.pass()
        assert(game.passCounter==1)
    }
    @Test
    fun passAndCanPlayCardTest(){
        val name1 = "Player1"
        val name2 = "Player2"
        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer = if (game.currentPlayer==0) game.player1 else game.player2
        currentPlayer.hand.clear()
        val sublist = listOf(
            Card(CardSuit.HEARTS, CardValue.NINE),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.FOUR)
        )
        game.playStack1.add(Card(CardSuit.HEARTS, CardValue.TEN))
        currentPlayer.drawStack.clear()
        assertFails {rootService.playerActionService.pass()}
    }
    @Test
    fun passAndCanDrawCardTest(){
        val name1 = "Player1"
        val name2 = "Player2"
        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer = if (game.currentPlayer==0) game.player1 else game.player2
        currentPlayer.hand.clear()
        val sublist = listOf(
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.FOUR)
        )
        currentPlayer.hand.addAll(sublist)
        assertFails {rootService.playerActionService.pass()}
    }
    @Test
    fun passAndCanRedrawHand(){
        val name1 = "Player1"
        val name2 = "Player2"
        gameService.startNewGame(name1, name2)

        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = if (game.currentPlayer==0) game.player1 else game.player2
        currentPlayer.hand.clear()
        val sublist = listOf(
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.NINE),
            Card(CardSuit.HEARTS, CardValue.KING),
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.CLUBS, CardValue.FIVE),
            Card(CardSuit.HEARTS, CardValue.FOUR),
            Card(CardSuit.HEARTS, CardValue.THREE)
        )
        currentPlayer.hand.addAll(sublist)
        assertFails {rootService.playerActionService.pass()}

    }
}