package gui

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.RootService
import tools.aqua.bgw.style.BorderRadius

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 */
class ResultMenuScene
    (private val rootService: RootService ,
     private val resultMessage: String,
) : MenuScene(750, 400), Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 200, posY = 100,
        text = "Game Over",
        font = Font(size = 34)
    )

    private val gameResult = Label(width = 550, height = 55, posX = 50, posY = 160 , text= resultMessage)

    private val mainMenuButton =
        Button(width = 250, height = 40, posX = 200, posY = 225, text = "Return to main menu", font = Font(size = 23),
        ).apply {
            visual = ColorVisual(66, 150, 131).apply { style.borderRadius = BorderRadius.MEDIUM }
            onMouseClicked = { rootService.gameService.exitToMainMenu() }
        }

    init {
        opacity = .85
        addComponents(headlineLabel , gameResult, mainMenuButton )
    }

}
