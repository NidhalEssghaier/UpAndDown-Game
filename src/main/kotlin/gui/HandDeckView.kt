package gui

import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView

/**
 * Players hand cards placement
 */
class HandDeckView(
    posX: Number,
    posY: Number,
    rotate: Boolean = false,
) : LinearLayout<CardView>(spacing = 10.0, posX = posX, posY = posY) {
    init {
        width = 800.0
        height = 200.0
        if (rotate) rotation = 180.0
    }
}