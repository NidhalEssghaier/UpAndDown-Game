package gui

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.RootService

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class MainMenuScene(private val rootService: RootService) : MenuScene(400, 1080), Refreshable {

    private val startButton = Button(
        width = 140, height = 35,
        posX = 360, posY = 240,
        text = "Start"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.gameService.startNewGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 200, posY = 50,
        text = "Start New Game",
        font = Font(size = 32)
    )

    private val p1Label = Label(
        width = 100, height = 35,
        posX = 200, posY = 125,
        text = "Player 1:"
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 200, height = 35,
        posX = 300, posY = 125,
        text = listOf("Homer", "Marge", "Bart", "Lisa", "Maggie").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    private val p2Label = Label(
        width = 100, height = 35,
        posX = 200, posY = 170,
        text = "Player 2:"
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 200, height = 35,
        posX = 300, posY = 170,
        text = listOf("Fry", "Bender", "Leela", "Amy", "zitoun").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val quitButton = Button(
        width = 140, height = 35,
        posX = 200, posY = 240,
        text = "Quit"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = { rootService.gameService.exitGame() }
    }



    init {
        opacity = .5
        addComponents(
            headlineLabel,
            p1Label, p1Input,
            p2Label, p2Input,
            startButton, quitButton
        )
    }
}
