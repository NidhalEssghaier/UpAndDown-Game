package service
import entity.*

/**
 * Service class that manages player actions in the game.
 * Provides functionalities for drawing, playing, replacing, and passing turns.
 *
 * @property rootService The root service managing the overall game state.
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Allows the current player to draw a card if possible.
     * The drawn card is added to the player's hand, and the pass counter is reset.
     *
     * @throws IllegalStateException if the player cannot draw a card.
     */
    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
        require(canDrawCard(currentPlayer))
        val drawnCards: Card = currentPlayer.drawStack.removeAt(0)
        currentPlayer.hand.add(drawnCards)
        game.passCounter = 0
        //onAllRefreshables { refreshAfterDrawCard() }
        //rootService.gameService.endTurn()
    }

    /**
     * Plays the specified card on the given play stack if the move is valid.
     * The card is removed from the player's hand and placed on top of the stack.
     *
     * @param card The card to be played.
     * @param playStack The stack on which the card is played.
     * @throws IllegalArgumentException if the move is not valid.
     */
    fun playCard(card: Card, playStack: MutableList<Card>) {
        val game = rootService.currentGame
        checkNotNull(game)
        require(game.currentPlayer in 1..2){"for debugging : current player must be 1 or 2"}
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

    /**
     * Allows the player to replace their entire hand with new cards from the draw stack.
     * The old hand is shuffled back into the draw stack before drawing new cards.
     *
     * @throws IllegalStateException if the player is not allowed to replace cards.
     */
    fun replaceCards(){
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
        require(canReplaceCards(currentPlayer))
        game.passCounter = 0
        currentPlayer.drawStack.addAll(currentPlayer.hand)
        currentPlayer.hand.clear()
        currentPlayer.drawStack.shuffle()

        val cardsToDraw = currentPlayer.drawStack.take(5)
        currentPlayer.hand.addAll(cardsToDraw)

        for (card in cardsToDraw) {
            currentPlayer.drawStack.remove(card)
        }

        //onAllRefreshables { refreshAfterReplaceCards() }
        //rootService.gameService.endTurn()
    }

    /**
     * Allows the player to pass their turn if no other actions are possible.
     * Increases the pass counter and may trigger the end of the game.
     *
     * @throws IllegalStateException if passing is not allowed.
     */
    fun pass() {
        val game = rootService.currentGame
        checkNotNull(game)
        require(canPass()){"You cant Pass !"}
        // require(!checkCanReplaceCards(game.player2)){"you can Replace Cards*"}
        //require(!checkCanDrawCard(game.player1)){"you can draw*"}
        //require(!canPlayCard(game.player1, game.playStack1, game.playStack2)){"you can play a card*"}

        game.passCounter++
        rootService.gameService.endTurn()
    }

    /**
     * Checks whether a given card can be played on the specified play stack.
     *
     * @param card The card to check.
     * @param playStack The stack to validate the move against.
     * @return `true` if the move is valid, otherwise `false`.
     */
    private fun isValidMove(card: Card, playStack: MutableList<Card>): Boolean {
        val playStackTopCard = playStack.first()
        val valueDifference = kotlin.math.abs(card.value.ordinal - playStackTopCard.value.ordinal)
        return (valueDifference == 12 || valueDifference == 1)
                || ((valueDifference == 2 || valueDifference == 11) && (card.suit == playStackTopCard.suit))
    }

    /**
     * Determines if the given player is allowed to draw a card.
     *
     * @param player The player whose ability to draw is checked.
     * @return `true` if the player can draw a card, otherwise throws an exception.
     * @throws IllegalStateException if the player cannot draw a card.
     */
    private fun canDrawCard(player : Player): Boolean {
        if  (player.hand.size >= 10) {
            throw IllegalStateException ("you can´t draw cards ! you have more than 9 card , please replace you cards")
        }
        else if (player.drawStack.isEmpty()) {
            throw IllegalStateException("you dont have any cards left to draw")
        }
        else return true
    }

    /**
     * Checks if the given player is eligible to draw a card.
     *
     * @param player The player to check.
     * @return `true` if the player can draw, otherwise `false`.
     */
    private fun checkCanDrawCard(player: Player): Boolean {
        return player.hand.size < 10 && player.drawStack.isNotEmpty()
    }

    /**
     * Determines if the player has at least one valid move on either play stack.
     *
     * @param player The player to check.
     * @param playStack1 The first play stack.
     * @param playStack2 The second play stack.
     * @return `true` if the player has a valid move, otherwise `false`.
     */
    private fun canPlayCard(player : Player, playStack1: MutableList<Card>, playStack2: MutableList<Card>): Boolean {
        val playerHand = player.hand
        for (card in playerHand) {
            if(isValidMove(card, playStack1)|| isValidMove(card, playStack2)) return true
        }
        println("you don´t have any playable cards! ")
        return false
    }

    /**
     * Determines if the player is allowed to replace their hand cards.
     *
     * @param player The player to check.
     * @return `true` if replacing is allowed, otherwise throws an exception.
     * @throws IllegalStateException if replacing is not possible.
     */
    private fun canReplaceCards(player : Player): Boolean {
        if (player.hand.size<8){
            throw IllegalStateException("you have less than 8 cards , you cant replace you cards")
        }else if (player.drawStack.isEmpty()){
            throw IllegalStateException ("you draw Deck is Empty , you cant replace you cards")
        }
        else{
            return true
        }
    }


    private fun checkCanReplaceCards(player : Player): Boolean {
        return player.hand.size>=8 && player.drawStack.isNotEmpty()
    }


    /**
     * Determines if the current player is allowed to pass their turn.
     * A player can only pass if they cannot draw, play, or replace cards.
     *
     * @return `true` if passing is allowed, otherwise `false`.
     * @throws IllegalStateException if the game has not started.
     */
    private fun canPass():Boolean{
        val currentGame = rootService.currentGame
        checkNotNull(currentGame){" the game has not been started yet!"}
        val currentPlayer: Player = if (currentGame.currentPlayer == 1) currentGame.player1 else currentGame.player2
        val playStack1 = currentGame.playStack1
        val playStack2 = currentGame.playStack2

        return !checkCanDrawCard(currentPlayer) &&
                !canPlayCard(currentPlayer, playStack1, playStack2) &&
                !checkCanReplaceCards(currentPlayer)
    }
}

