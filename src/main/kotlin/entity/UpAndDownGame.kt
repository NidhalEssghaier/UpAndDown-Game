package entity
/**
 * Die Klasse repräsentiert das Spiel "UpAandDown".
 * Sie verwaltet den Spielverlauf, indem sie:
 * - die aktuelle Runde und den aktiven Spieler speichert,
 * - die Spieler speichert,
 * - die Kartenstapel verwaltet : zwei Ablagestapel*
 * Attribute:
 * @property -passCounter-- Zählt, wie oft hintereinander gepasst wurde.
 * @property -currentPlayer-- Speichert den Index des aktuell am Zug befindlichen Spielers.
 *  @property -players-- Liste der teilnehmenden Spieler (maximal 2).
 *  @property -drawStack-- Der Stapel der noch nicht gezogenen Karten.
 *  @property -playStack1-- Erster Ablagestapel für gespielte Karten.
 *  @property -playStack2-- Zweiter Ablagestapel für gespielte Karten.
  */

data class UpAndDownGame(val player1: Player, val player2: Player) {
    val playStack1: MutableList<Card> = mutableListOf()
    val playStack2: MutableList<Card> = mutableListOf()
    var currentPlayer: Int = 0
    var passCounter: Int = 0

    override fun toString() = "$player1$player2"
}

