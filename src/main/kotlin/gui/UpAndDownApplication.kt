package gui

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

/**
 * Implementation of the BGW [BoardGameApplication] for the  card game "Up And Down"
 */
class UpAndDownApplication : BoardGameApplication("Up And Down"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)
    private val mainMenuScene = MainMenuScene(rootService)

    init {

        // all scenes and the application itself need to
        // react to changes done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,mainMenuScene
        )

        // This is just done so that the blurred background when showing
        // the new game menu has content and looks nicer
        rootService.gameService.startNewGame("Bob", "Alice")

        this.showGameScene(gameScene)
        this.showMenuScene(mainMenuScene, 0)

    }

    override fun refreshAfterStartNewGame() {
        val nextPlayerScene = NextPlayerScene(rootService)
        this.showMenuScene(nextPlayerScene)
        this.hideMenuScene()

    }

    override fun refreshAfterEndGame(resultMessage: String) {
        val resultMenuScene = ResultMenuScene(rootService, resultMessage)
        this.showMenuScene(mainMenuScene)
    }

    override fun refreshAfterExitGame() {
        this.exit()
    }

    override fun refreshAfterReturnToMenu() {
        this.hideMenuScene()
        this.showMenuScene(mainMenuScene)
    }

    override fun refreshAfterCancelGame() {
        this.showMenuScene(mainMenuScene)
    }

    override fun refreshAfterStartTurn() {
        this.hideMenuScene()
    }

    override fun refreshAfterEndTurn(nextPlayerSceneMessage: String) {

        val nextPlayerScene = NextPlayerScene(rootService).apply { }
        this.showMenuScene(nextPlayerScene)
    }


}



