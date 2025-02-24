package entity

/**
 * Represents a player in the "Up and Down" card game.
 *
 * Each player has:
 * - A -name- for identification.
 * - A -hand- containing up to 10 cards.
 *
 * The player's hand is stored as a mutable list, allowing dynamic card management.
 * @property -name- The player's name.
 * @property -hand- The list of cards in the player's hand (max 10 cards).
 */
class Player (var name: String){
    var drawStack : MutableList<Card> = mutableListOf()
    var hand : MutableList<Card> = mutableListOf()

    override fun toString(): String =
        "$name: D$drawStack H$hand"


}
