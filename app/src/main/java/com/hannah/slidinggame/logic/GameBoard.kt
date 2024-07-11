package com.hannah.slidinggame.logic

class GameBoard {
    //Grid => 5 x 5 two dimensionat array
    private val grid = arrayOf(
        arrayOf(Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK),
        arrayOf(Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK),
        arrayOf(Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK),
        arrayOf(Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK),
        arrayOf(Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK, Player.BLANK)
    )
    private val DIM = 5
    private var currentPlayer: Player = Player.X

    fun submittMove(move: Char) {
        if (move in '1'..'5') {
            val col = move - 1 // 對上 array index
            println(col)
        }
        println("=======")
        println(grid.size)
        for (arr in grid) {
            for (element in arr) {
                print("$element, ")
            }
            // BLANK, BLANK, BLANK, BLANK, BLANK, =======
            println("=======")
        }

    }


}