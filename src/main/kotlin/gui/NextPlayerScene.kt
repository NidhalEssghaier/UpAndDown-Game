package gui

import entity.Player
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * A scene that displays a transition screen for the next player before their turn starts.
 * This scene provides a confirmation button to proceed to the next turn.
 *
 * @param rootService The central service that manages the game state.
 */
class NextPlayerScene(
    private val rootService: RootService
) : MenuScene(700, 394), Refreshable {

    /**
     * The current player whose turn is about to start.
     */
    private val currentPLayer: Player = rootService.currentGame?.let { game ->
        if (game.currentPlayer == 1) game.player1 else game.player2
    } ?: throw IllegalStateException(" No Game started!")

    /**
     * Label displaying the name of the next player.
     */
    private val playerNameLabel = Label(
        width = 400, height = 60, posX = 150, posY = 90,
        text = " ${currentPLayer.name}! ",
        font = Font(size = 36, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(255, 215, 0)
    }

    /**
     * Confirmation button that starts the next player's turn.
     */
    private val confirmButton = Button(
        width = 350, height = 60, posX = 175, posY = 250,
        text = " Let's go!",
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(76, 175, 80)
        onMouseClicked = {
            rootService.gameService.startTurn()
        }
    }

    /**
     * Label displaying a message to prepare the next player.
     */
    private val messageLabel = Label(
        width = 400, height = 50, posX = 150, posY = 160,
        text = "Get ready for your turn!",
        font = Font(size = 28)
    ).apply {
        visual = ColorVisual(255, 255, 255)
    }

    /**
     * Initializes the scene
     */
    init {
        opacity = 0.85
        background = ColorVisual(44, 62, 80)
        addComponents(playerNameLabel, messageLabel, confirmButton)
    }
}
