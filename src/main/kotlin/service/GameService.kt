package service
import entity.*

/**
 * @property rootService Reference to the [RootService]
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * Starts a new game with two players.
     *
     * @param name1 Name of the first player.
     * @param name2 Name of the second player.
     * @throws IllegalArgumentException If player names are empty or identical.
     */
    fun startNewGame(name1: String, name2: String) {
        require(name1.isNotEmpty() && name2.isNotEmpty()) { "Player names must not be empty" }
        require(name1 != name2) { "Players must have different names" }

        val player1 = Player(name1)
        val player2 = Player(name2)
        val startingPlayer = determineStartingPlayer()
        val allCards: MutableList<Card> = defaultRandomCardList()

        distributeHandAndDrawCards(player1, player2, allCards)

        val game = UpAndDownGame(player1, player2).apply {
            playStack1.add(allCards.removeAt(0))
            playStack2.add(allCards.removeAt(0))
            currentPlayer = startingPlayer
            passCounter = 0
        }
        rootService.currentGame = game


        onAllRefreshables {refreshAfterStartNewGame()}
    }

    /**
     * Ends the current player's turn and determines if the game has ended.
     *
     * @throws IllegalStateException If no game is active or if the game has already ended.
     */
    fun endTurn() {
        val game = rootService.currentGame ?: throw IllegalStateException("No game started yet.")
        if (gameEnded()) throw IllegalStateException("Current game has already ended.")

        require(game.currentPlayer in 1..2) { "There is no active player" }

        val activePlayer = if (game.currentPlayer == 1) game.player1 else game.player2
        val waitingPlayer = if (game.currentPlayer == 1) game.player2 else game.player1

        if (activePlayer.drawStack.isEmpty() && activePlayer.hand.isEmpty()) {
            onAllRefreshables {  refreshAfterEndGame("${activePlayer.name} wins the game!")}
            rootService.currentGame = null
            return
        }

        if (game.passCounter >= 2) {

            val activePlayerHandCardsNumber : Int = activePlayer.hand.size
            val waitingPlayerHandCardsNumber : Int = waitingPlayer.hand.size

            onAllRefreshables {
                when {
                    (activePlayerHandCardsNumber < waitingPlayerHandCardsNumber) ->
                        //println("${activePlayer.name} wins the game!")
                      refreshAfterEndGame("${activePlayer.name} wins the game!")
                    (activePlayerHandCardsNumber > waitingPlayerHandCardsNumber) ->
                         refreshAfterEndGame("${waitingPlayer.name} wins the game!")
                        //println("${waitingPlayer.name} wins the game!")
                    else ->
                        refreshAfterEndGame("Game ended with a Draw, you both won the Game!")
                        //println("Game ended with a Draw, you both won the Game!")

                }
            }
            rootService.currentGame = null
            return

        }

        game.currentPlayer = if (game.currentPlayer == 1) 2 else 1
        val nextPlayer :Player = if (game.currentPlayer == 1) game.player2 else game.player1
        onAllRefreshables{(refreshAfterEndTurn("${nextPlayer.name} Its you turn!"))}
    }

    /**
     * Creates a shuffled deck of 52 cards (all four suits, values from 2 to Ace).
     *
     * @return A shuffled list of [Card]  representing a full deck.
     */
    private fun defaultRandomCardList(): MutableList<Card> =
        CardSuit.entries.flatMap { suit ->
            CardValue.entries.map { value -> Card(suit, value) }
        }.shuffled().toMutableList()

    /**
     * Randomly determines the starting player.
     *
     * @return The player number (1 or 2) who starts the game.
     */
    private fun determineStartingPlayer(): Int = (1..2).random()

    /**
     * Distributes initial hand and draw stacks for both players.
     *
     * @param player1 First player.
     * @param player2 Second player.
     * @param allCards The shuffled deck of cards.
     */
    private fun distributeHandAndDrawCards(player1: Player, player2: Player, allCards: MutableList<Card>) {
        player1.drawStack = allCards.take(25).toMutableList()
        player2.drawStack = allCards.drop(25).take(25).toMutableList()
        player1.hand = player1.drawStack.take(5).toMutableList().also { player1.drawStack.removeAll(it) }
        player2.hand = player2.drawStack.take(5).toMutableList().also { player2.drawStack.removeAll(it) }
    }

    /**
     * Checks if the game has ended.
     *
     * @return true if all draw stacks are empty and neither player has drawn a card.
     */
    private fun gameEnded(): Boolean {
        val game = rootService.currentGame ?: throw IllegalStateException("No game currently running.")
        return game.player1.drawStack.isEmpty() && game.player1.hand.isEmpty() &&
                game.player2.drawStack.isEmpty() && game.player2.hand.isEmpty() &&
                game.playStack1.isEmpty() && game.playStack2.isEmpty()
    }

    /**
    * updates the Application scene when for next players turn
    **/
    fun startTurn() {
        onAllRefreshables { refreshAfterStartTurn() }
    }

    /**
     *  exit and returns to main menu while a game is running
     **/
    fun cancelGame() {
        rootService.currentGame = null
        onAllRefreshables { refreshAfterCancelGame() }
    }

    /**
    * Exits UpAndDownGame application
     * */
    fun exitGame() {
        onAllRefreshables { refreshAfterExitGame() }
    }
    /**
    *  returns to main menu during a running game
    * */
    fun exitToMainMenu() {
        onAllRefreshables { refreshAfterReturnToMenu() }
    }


}
