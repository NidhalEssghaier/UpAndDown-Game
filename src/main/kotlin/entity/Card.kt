package entity
/**
 * Data class for the single typ of game elements that the game "UpAndDown " knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */

class Card (val suit : CardSuit , val value : CardValue) {

    override fun toString() = "$suit$value"
}