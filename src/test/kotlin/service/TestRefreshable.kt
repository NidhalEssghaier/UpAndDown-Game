package service

import gui.Refreshable
import entity.Card

/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterStartNewGameCalled: Boolean = false
        private set

    var refreshAfterPlayCardCalled: Boolean = false
        private set

    var refreshAfterReplaceCardsCalled: Boolean = false
        private set

    var refreshAfterEndTurnCalled: Boolean = false
        private set

    var refreshAfterReturnToMenuCalled: Boolean = false
        private set

    var refreshAfterEndGameCalled: Boolean = false
        private set

    var refreshAfterDrawCardCalled: Boolean = false
        private set

    var refreshAfterStartTurnCalled: Boolean = false
        private set

    var refreshAfterCancelGameCalled: Boolean = false
        private set

    var refreshAfterExitGameCalled: Boolean = false
        private set
    /**
     * resets all *Called properties to false
     */
    fun reset() {

        refreshAfterEndTurnCalled = false
        refreshAfterReturnToMenuCalled = false
        refreshAfterExitGameCalled = false
        refreshAfterStartNewGameCalled = false
        refreshAfterEndGameCalled = false
        refreshAfterReplaceCardsCalled = false
        refreshAfterPlayCardCalled = false
        refreshAfterDrawCardCalled = false

    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterDrawCard(card: Card) {
        refreshAfterDrawCardCalled = true
    }

    override fun refreshAfterEndGame(resultMessage: String) {
        refreshAfterEndGameCalled = true
    }

    override fun refreshAfterReplaceCards() {
        refreshAfterReplaceCardsCalled = true
    }

    override fun refreshAfterReturnToMenu() {
        refreshAfterReturnToMenuCalled = true
    }

    override fun refreshAfterStartTurn() {
        refreshAfterStartTurnCalled = true
    }

    override fun refreshAfterEndTurn(nextPlayerSceneMessage: String) {
        refreshAfterEndTurnCalled = true
    }

    override fun refreshAfterCancelGame() {
        refreshAfterCancelGameCalled = true
    }
    override fun refreshAfterPlayCard(
        card: Card,
        playStack: MutableList<Card>,
    ) {
        refreshAfterPlayCardCalled = true
    }


    override fun refreshAfterExitGame() {
        refreshAfterExitGameCalled = true
    }


}
