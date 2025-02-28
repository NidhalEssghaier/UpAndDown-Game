
package gui

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.RootService
import tools.aqua.bgw.visual.ImageVisual

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class MainMenuScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private val p1Input: TextField = TextField(
        width = 150, height = 35,
        posX = 930, posY = 170,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "papay").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    private val p2Input: TextField = TextField(
        width = 150, height = 35,
        posX = 930, posY = 220,
        text = listOf("Fry", "Bender", "Zaytouna", "Amy", "Zitoun").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val p1Label = Label(
        width = 100, height = 35,
        posX = 820, posY = 170,
        text = "Player 1:",
        font = Font(size = 21)
    )

    private val p2Label = Label(
        width = 100, height = 35,
        posX = 820, posY = 220,
        text = "Player 2:",
        font = Font(size = 21)
    )


    private val headlineLabel = Label(
        width = 300, height = 50, posX = 810, posY = 90,
        text = "Start New Game",
        font = Font(size = 32, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(255, 255, 255, 0)
    }
    private val startButton = Button(
        width = 140, height = 40,
        posX = 890, posY = 280,
        text = "Start"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        font = Font(size = 18, fontWeight = Font.FontWeight.BOLD)
        onMouseClicked = {
            rootService.gameService.startNewGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    private val quitButton = Button(
        width = 140, height = 40,
        posX = 890, posY = 345,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        font = Font(size = 18, fontWeight = Font.FontWeight.BOLD)
        onMouseClicked = { rootService.gameService.exitGame() }
    }

    /**
     * Initializes the scene
     */
    init {
        opacity = 0.8
        //background = ColorVisual(240, 240, 240)
        background = ImageVisual("menu.jpg")
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            startButton, quitButton
        )
    }
}
