package service
import entity.*

class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
        require(canDrawCard(currentPlayer))
        val drawnCards: Card = currentPlayer.drawStack.removeAt(0)
        currentPlayer.hand.add(drawnCards)
        game.passCounter = 0
        //onAllRefreshables { refreshAfterDrawCard() }
        rootService.gameService.endTurn()

    }

    fun playCard(card: Card, playStack: MutableList<Card>) {

        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
        require(currentPlayer.hand.contains(card)) { "you cant play this Card  ! , please choose a Card from you hand Deck" }
        if (!isValidMove(card, playStack)) {
            throw IllegalArgumentException("this is not a valid move!")
        }

        playStack.add(0, card)
        currentPlayer.hand.remove(card)
        game.passCounter = 0
        //onAllRefreshables { refreshAfterPlayCard }
       // rootService.gameService.endTurn()


    }

    fun replaceCards(){
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
        require(canReplaceCards(currentPlayer))
        game.passCounter = 0
        currentPlayer.drawStack.addAll(currentPlayer.hand)
        currentPlayer.hand.clear()
        currentPlayer.drawStack.shuffle()
        val cardsToDraw : MutableList<Card> = currentPlayer.drawStack.take(5).toMutableList()
        currentPlayer.hand.addAll(cardsToDraw)
        currentPlayer.drawStack.removeAll(cardsToDraw)

        //onAllRefreshables { refreshAfterReplaceCards() }
        //rootService.gameService.endTurn()

    }

    fun pass() {
        val game = rootService.currentGame
        checkNotNull(game)
        require(canPass()){"You cant Pass !"}
        game.passCounter++
        rootService.gameService.endTurn()
    }


    private fun isValidMove(card: Card, playStack: MutableList<Card>): Boolean {
        val playStackTopCard = playStack.first()
        val valueDifference = kotlin.math.abs(card.value.ordinal - playStackTopCard.value.ordinal)
        return (valueDifference == 12 || valueDifference == 1)
                || ((valueDifference == 2 || valueDifference == 11) && (card.suit == playStackTopCard.suit))
    }

    private fun canDrawCard(player : Player): Boolean {

         return when {
            player.hand.size >= 10 -> false.also{ println("you can´t draw cards ! you have more than 10 card , please replace you cards") }
             player.drawStack.isEmpty()->  false.also{ println("You dont have any cards left to draw") }
             else ->  true
        }

    }
    private fun canPlayCard(player : Player, playStack1: MutableList<Card>, playStack2: MutableList<Card>): Boolean {
        val playerHand = player.hand
        for (card in playerHand) {
            if(isValidMove(card, playStack1)|| isValidMove(card, playStack2)) return true
        }
        println("you don´t have any playable cards! ")
        return false

    }

    private fun canReplaceCards(player : Player): Boolean {
        return when {
            player.hand.size<8 -> false.also{ println("you have less than 8 cards , you cant replace you cards") }
            player.drawStack.isEmpty() -> false.also{ println("you draw Deck is Empty , you cant replace you cards") }
            else ->  true
        }
    }

    private fun canPass():Boolean{

        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val currentPlayer: Player = if (currentGame.currentPlayer == 1) currentGame.player1 else currentGame.player2
        val playStack1 = currentGame.playStack1
        val playStack2 = currentGame.playStack2

        return !canDrawCard(currentPlayer) &&
                !canPlayCard(currentPlayer, playStack1, playStack2) &&
                !canReplaceCards(currentPlayer)
    }

}