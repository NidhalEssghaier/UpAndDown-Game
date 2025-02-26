package gui

import entity.*
import service.RootService
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ImageVisual


/**
 *  main scene during a  game.
 */
class GameScene(
    private val rootService: RootService,
) : BoardGameScene(1920, 1080),
    Refreshable {


    private val passButton = Button(
        width = 170,
        height = 65,
        posX = 1580,
        posY = 510,
        text = "Pass Turn",
        font = Font(size = 26, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(220, 53, 69)
        onMouseClicked = { rootService.playerActionService.pass() }
    }


    private val drawButton = Button(
        width = 350,
        height = 350,
        posX = 340,
        posY = 200,
        text = "Draw Card",
        font = Font(size = 24, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(255, 0, 0)
        onMouseClicked = { rootService.playerActionService.drawCard() }
    }

    private val replaceButton = Button(
        width = 200,
        height = 200,
        posX = 400,
        posY = 820,
        text = "Replace Cards",
        font = Font(size = 24, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(255, 193, 7)
        onMouseClicked = { rootService.playerActionService.replaceCards() }
    }

    private val endGameButton = Button(
        width = 80,
        height = 80,
        posX = 20,
        posY = 20,
        text = "X",
        font = Font(size = 40, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(108, 117, 125, 0.6)
        onMouseClicked = { rootService.gameService.cancelGame() }
    }


    private var clickedCard: Card? = null
    private var clickedPlayStack: MutableList<Card>? = null
    private var clickedCardElement: CardView? = null
    private var clickedPlayStackElement: CardView? = null


    // Spieler 1 (unten)
    private val player1Name = Label(
        posX = 600, posY = 1010, width = 400, height = 60,
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(52, 152, 219)
    }
    private val player1Hand = HandDeckView(posX = 620, posY = 780)
    private val player1DrawStack = LabeledStackView(
        posX = 400, posY = 780, "Draw Stack"
    ).apply { visual = ColorVisual(41, 128, 185) }

    // Spieler 2 (oben)
    private val player2Name = Label(
        posX = 600, posY = 20, width = 400, height = 60,
        font = Font(size = 28, fontWeight = Font.FontWeight.BOLD)
    ).apply { visual = ColorVisual(231, 76, 60) }

    private val player2Hand = HandDeckView(posX = 400, posY = 100).apply { rotation = 180.0 }

    private val player2DrawStack = LabeledStackView(
        posX = 1290, posY = 100, "Draw stack", true
    ).apply { visual = ColorVisual(192, 57, 43) }

    // Play Stacks in der Mitte
    private val playStack1 = LabeledStackView(
        posX = 800, posY = 440, "Play Stack 1"
    ).apply {
        visual = ColorVisual(39, 174, 96)
        onMouseClicked = {
            clickedPlayStack = rootService.currentGame?.playStack1
            PlayingACard()
        }
    }

    private val playStack2 = LabeledStackView(
        posX = 1050, posY = 440, "Play Stack 2"
    ).apply {
        visual = ColorVisual(39, 174, 96)
        onMouseClicked = {
            clickedPlayStack = rootService.currentGame?.playStack2
            PlayingACard()
        }
    }

    /**
     * structure to hold pairs of (card, cardView) that can be used
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     * 2. to find the corresponding card to pass to a service method on the occurrence of ui events on views (backward
     *    lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    // initialize scene with all components
    init {
        // dark green for "Casino table" flair
        background = ImageVisual("poker_tisch.png")

        addComponents(
            passButton,
            drawButton,
            replaceButton,
            endGameButton,
            player1DrawStack,
            player1Hand,
            player1Name,
            player2DrawStack,
            player2Hand,
            player2Name,
            playStack1,
            playStack2

        )
    }

    /** Initializes the complete GUI, i.e. the stack views, hand views and play stack views (each x2). */
    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        cardMap.clear()

        val cardImageLoader = CardImageLoader()

        initialPlayerNameLabel(player1Name, game.player1.name)
        initialStackView(game.player1.drawStack, player1DrawStack, cardImageLoader)
        initialHandView(game.player1.hand, player1Hand, cardImageLoader, game.currentPlayer == 1)
        initialPlayerNameLabel(player2Name, game.player2.name)
        initialStackView(game.player2.drawStack, player2DrawStack, cardImageLoader)
        initialHandView(game.player2.hand, player2Hand, cardImageLoader, game.currentPlayer == 2)
        initialStackView(game.playStack1, playStack1, cardImageLoader, true)
        initialStackView(game.playStack2, playStack2, cardImageLoader, true)


        moveDrawButtonAccordingly(game.currentPlayer)
    }

    /**
     * Refresh components after card has been played
     *
     */
    override fun refreshAfterPlayCard(
        card: Card,
        playStack: MutableList<Card>,
    ) {
        val game = rootService.currentGame
        checkNotNull(game)

        val cardImageLoader = CardImageLoader()

        if (playStack == game.playStack2) {
            initialStackView(game.playStack2, playStack2, cardImageLoader, true,)
        } else {
            initialStackView(game.playStack1, playStack1, cardImageLoader, true)
        }

        initialHandView(game.player1.hand, player1Hand, cardImageLoader, game.currentPlayer == 1)
        initialHandView(game.player2.hand, player2Hand, cardImageLoader, game.currentPlayer == 2)
        this.lock()
        Thread.sleep(1380)

        rootService.gameService.endTurn()
    }

    /**
     * Updates the hand after card has been drawn
     */
    override fun refreshAfterDrawCard(card: Card) {
        val game = rootService.currentGame
        checkNotNull(game)

        if (game.currentPlayer == 1) {
            val drawnCardView = player1DrawStack.pop().apply { this.showFront() }
            player1Hand.add(drawnCardView)
        } else {
            val drawnCardView = player2DrawStack.pop().apply { this.showFront() }
            player2Hand.add(drawnCardView)
        }
        this.lock()
        Thread.sleep(1610)

        rootService.gameService.endTurn()
    }

    /** Updates the stack view and hand view to display the correct cards */
    override fun refreshAfterReplaceCards() {
        val game = rootService.currentGame
        checkNotNull(game)
        val cardImageLoader = CardImageLoader()

        if (game.currentPlayer == 1) {
            initialHandView(game.player1.hand, player1Hand, cardImageLoader, game.currentPlayer == 1)
            initialStackView(game.player1.drawStack, player1DrawStack, cardImageLoader)
        } else {
            initialHandView(game.player2.hand, player2Hand, cardImageLoader, game.currentPlayer == 2)
            initialStackView(game.player2.drawStack, player2DrawStack, cardImageLoader)
        }

        this.lock()
        Thread.sleep(1610)

        rootService.gameService.endTurn()
    }


    override fun refreshAfterEndTurn(nextPlayerSceneMessage: String) {
        val game = rootService.currentGame
        checkNotNull(game)
        val cardImageLoader = CardImageLoader()
        cardMap.clear()
        initialPlayerNameLabel(player1Name, game.player1.name)
        initialStackView(game.player1.drawStack, player1DrawStack, cardImageLoader)
        initialHandView(game.player1.hand, player1Hand, cardImageLoader, game.currentPlayer == 1)
        initialPlayerNameLabel(player2Name, game.player2.name)
        initialStackView(game.player2.drawStack, player2DrawStack, cardImageLoader)
        initialHandView(game.player2.hand, player2Hand, cardImageLoader, game.currentPlayer == 2)
        initialStackView(game.playStack1, playStack1, cardImageLoader, true)
        initialStackView(game.playStack2, playStack2, cardImageLoader, true)

        moveDrawButtonAccordingly(game.currentPlayer)

        clickedCard = null
        clickedCard = null
    }




    private fun initialHandView(
        hand: MutableList<Card>,
        handView: HandDeckView,
        cardImageLoader: CardImageLoader,
        showCards: Boolean = false,
    ) {
        handView.clear()

        hand.forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage,
            ).apply {
                if (showCards) showFront()

                onMouseClicked = {
                    // Falls eine andere Karte bereits animiert wurde, setze sie zurück
                    clickedCardElement?.let { previousCard ->
                        resetMovement(previousCard)
                        playAnimation(
                            ScaleAnimation(
                                componentView = previousCard,
                                toScaleX = 1.0,
                                toScaleY = 1.0,
                                duration = 300
                            )
                        )
                    }

                    // Falls die gleiche Karte erneut geklickt wird, nicht erneut animieren
                    if (clickedCardElement == this) {
                        clickedCard = null
                        clickedCardElement = null
                    }

                    clickedCard = card
                    clickedCardElement = this

                    PlayingACard()

                    playAnimation(
                        MovementAnimation(
                            componentView = this,
                            byX = 0,
                            byY = -50,
                            duration = 500,
                        )
                    )
                    playAnimation(
                        ScaleAnimation(
                            componentView = this,
                            toScaleX = 1.1,
                            toScaleY = 1.1,
                            duration = 500
                        )
                    )
                }
            }
            handView.add(cardView)
            cardMap.add(card to cardView)
        }
    }
    /**
     * clears [stackView], adds a new [CardView] for each element of [stack] onto it, and adds the newly created
     * view/card pair to the global [cardMap].
     */
    private fun initialStackView(
        stack: MutableList<Card>,
        stackView: LabeledStackView,
        cardImageLoader: CardImageLoader,
        showCards: Boolean = false,
    ) {
        stackView.clear()

        stack.asReversed().forEachIndexed { idx, card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage,
            ).apply {
                if (showCards) {
                    showFront()

                    if (idx == stack.size - 1) {
                        onMouseClicked = {
                            clickedPlayStackElement?.let { previousCard ->
                                resetRotation(previousCard)
                                playAnimation(
                                    ScaleAnimation(
                                        componentView = previousCard,
                                        toScaleX = 1.0,
                                        toScaleY = 1.0,
                                        duration = 300
                                    )
                                )
                            }

                            clickedPlayStackElement = this

                            playAnimation(
                                RotationAnimation(
                                    componentView = this,
                                    byAngle = -20.0,
                                    duration = 500,
                                )
                            )
                            playAnimation(
                                ScaleAnimation(
                                    componentView = this,
                                    toScaleX = 1.1,
                                    toScaleY = 1.1,
                                    duration = 500
                                )
                            )
                        }
                    }
                }
            }
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }

    private fun PlayingACard() {
        val card = clickedCard
        val playStack = clickedPlayStack
        if (card != null && playStack != null) {
            clickedCard = null
            clickedPlayStack = null
            resetMovement(clickedCardElement)
            resetRotation(clickedCardElement)
            rootService.playerActionService.playCard(card, playStack)
        }
    }


    private fun initialPlayerNameLabel(
        label: Label,
        text: String,
    ) {
        label.text = text
        label.font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)

        label.apply {
            visual = ColorVisual(0, 0, 0, 0.6)

            opacity = 0.75
        }
    }

    private fun moveDrawButtonAccordingly(position: Int) {
        if (position == 2) {
            drawButton.apply {
                posX = 1300.0
                posY = 225.0
            }
            replaceButton.apply {
                posX = 1300.0
                posY = 150.0
            }

        } else {
            drawButton.apply {
                posX = 400.0
                posY = 745.0
            }
            replaceButton.apply {
                posX = 410.0
                posY = 820.0
            }

        }
    }




    private fun resetMovement(component: ComponentView?) {
        component?.apply {
            playAnimation(
                MovementAnimation(
                    componentView = this,
                    byX = 0,
                    byY = 0,
                    duration = 0,
                ),
            )
        }
    }

    private fun resetRotation(component: ComponentView?) {
        component?.apply {
            playAnimation(
                RotationAnimation(
                    componentView = this,
                    byAngle = 0.0,
                    duration = 0,
                ),
            )
        }
    }


}

