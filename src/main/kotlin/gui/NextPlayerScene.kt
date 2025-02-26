package gui

import entity.Player
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.MenuScene
import service.RootService
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual


class NextPlayerScene(
    private val rootService: RootService
) : MenuScene(700, 394), Refreshable {

    val currentPLayer:Player = if (rootService.currentGame!!.currentPlayer==1) rootService.currentGame!!.player2
    else rootService.currentGame!!.player1
    private val playerNameLabel = Label(
        width = 400, height = 60, posX = 150, posY = 90,
        text = " ${currentPLayer.name}! ",
        font = Font(size = 36, fontWeight = Font.FontWeight.BOLD)
     ).apply {
        visual = ColorVisual(255, 215, 0)
     }



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

    private val messageLabel = Label(
        width = 400, height = 50, posX = 150, posY = 160,
        text = "Get ready for your turn!",
        font = Font(size = 28)
    ).apply {
        visual = ColorVisual(255, 255, 255)
    }

    init {
        opacity = 0.85
        background = ColorVisual(44, 62, 80)
        addComponents(playerNameLabel, messageLabel, confirmButton)
    }
}
