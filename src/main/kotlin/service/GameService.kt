package service

import entity.*


class GameService(private val rootService :RootService): AbstractRefreshingService() {

    fun startNewGame( name1: String, name2: String, ) {

        require(name1.isNotEmpty() && name2.isNotEmpty()){"Player names must not be empty"}
        require(name1!=name2) { "Players must have different names"}

        val player1 : Player = Player(name1)
        val player2 : Player = Player(name2)
        val startingPlayer  = determineStartingPlayer()


        val allCards: MutableList<Card> = defaultRandomCardList().toMutableList() //52 Karten

        distributeHandAndDrawCards(player1, player2, allCards)


        val game = UpAndDownGame(player1, player2).apply {
            currentPlayer = startingPlayer
            playStack1.add(allCards.removeAt(0))
            playStack2.add(allCards.removeAt(0))
            passCounter = 0
        }

        rootService.currentGame = game
      //  onAllRefreshables { refreshAfterStartNewGame() }




    }

    fun endTurn() {

        val game = rootService.currentGame ?: throw IllegalStateException("No game started yet.")
        if (gameEnded()) throw IllegalStateException("Current game has already ended.")

        require ( game.currentPlayer in 1..2){ "there is no active player"}

        val activePlayer: Player = if (game.currentPlayer ==1) game.player1 else game.player2
        val waitingPlayer : Player = if (game.currentPlayer == 1) game.player2 else game.player1



        if (activePlayer.drawStack.isEmpty()&& activePlayer.hand.isEmpty()) {
             // onAllRefreshables { refreshAfterEndGame("${activePlayer.name} wins the game!") }
               rootService.currentGame = null
            return
        }
        if (game.passCounter >= 2) {

            val activePlayerHandCardsNumber : Int = activePlayer.hand.size
            val waitingPlayerHandCardsNumber : Int = waitingPlayer.hand.size

                onAllRefreshables {
                    when {
                        (activePlayerHandCardsNumber < waitingPlayerHandCardsNumber) ->
                            println("${activePlayer.name} wins the game!")
                            //refreshAfterEndGame("${activePlayer.name} wins the game!")
                        (activePlayerHandCardsNumber > waitingPlayerHandCardsNumber) ->
                           // refreshAfterEndGame("${waitingPlayer.name} wins the game!")
                            println("${waitingPlayer.name} wins the game!")
                        else ->
                            //refreshAfterEndGame("Game ended with a Draw, you both won the Game!")
                            println("Game ended with a Draw, you both won the Game!")

                    }
                }
            rootService.currentGame = null
            return

        }
        game.currentPlayer = if (game.currentPlayer == 1) 2 else 1
        val nextPlayer: Player = if (game.currentPlayer == 1) game.player1 else game.player2
       // onAllRefreshables { refreshAfterNextPlayer("${nextPlayer.name},it's you turn ! Are you Ready ?") }

    }


    /**
     * Creates a shuffled deck of 52 cards (all four suits, values from 2 to Ace).
     */
    private fun defaultRandomCardList(): MutableList<Card> =
        CardSuit.entries.flatMap { suit ->
            CardValue.entries.map { value ->
                Card(suit, value)
            }
        }.shuffled().toMutableList()

    /**
     * Determines which player starts the game randomly.
     */
    private fun determineStartingPlayer() :Int = (1..2).random()


    private fun distributeHandAndDrawCards(player1: Player, player2: Player, allCards: MutableList<Card>) {
        player1.drawStack = allCards.take(25).toMutableList()
        player2.drawStack = allCards.drop(25).take(25).toMutableList()

        player1.hand = player1.drawStack.take(5).toMutableList().also { player1.drawStack.removeAll(it) }
        player2.hand = player2.drawStack.take(5).toMutableList().also { player2.drawStack.removeAll(it) }
    }

    private fun gameEnded() : Boolean {

        val game = rootService.currentGame ?: throw IllegalStateException("No game currently running.")

        // all draw stacks must be empty
        val allStacksEmpty = game.player1.drawStack.isEmpty() &&
                game.player1.hand.isEmpty() &&
                game.player2.drawStack.isEmpty() &&
                game.player2.hand.isEmpty() &&
                game.playStack1.isEmpty() &&
                game.playStack2.isEmpty()

        // both have not drawn a card
        val bothNotDrawn = !game.player1.hasDrawnCard &&
                !game.player2.hasDrawnCard

        return allStacksEmpty && bothNotDrawn
    }

}