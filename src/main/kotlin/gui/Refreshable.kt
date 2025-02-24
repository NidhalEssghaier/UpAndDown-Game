package gui
import service.AbstractRefreshingService
import tools.aqua.bgw.util.Stack
import entity.Card



/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame() {}

    /**
     * changes the scene after cards are replaced
     */
    fun refreshAfterReplaceCards() {}

    /**
    * changes the scene after a card is drawn
    */
    fun refreshAfterDrawCard(card: Card) {}

    /**
    * changes the scene after the current game ends
    */
    fun refreshAfterReturnToMenu() {}

    /**
     * refresh after the game is closed
     */
    fun refreshAfterExitGame() {}

    /**
     * changes the scene after player´s turn starts
     */
    fun refreshAfterStartTurn() {}

    /**
     * changes the scene after player´s turn ends
     */
    fun refreshAfterEndTurn() {}

    /**
     * changes the scene after a card has been played
     * @param card the played [Card]
     * @param playStack [playStack] on wich the played card is placed
     */
    fun refreshAfterPlayCard(
        card: Card,
        playStack: Stack<Card>,
    ) {}

    /**
     * changes the scene after the game is canceled
     */
    fun refreshAfterCancelGame() {}

    /**
     * changes the scene  after game is ended
     *
     * @param resultMessage that declares the winner
     */
    fun refreshAfterEndGame(resultMessage: String) {}


}

