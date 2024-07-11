package com.hannah.slidinggame.logic

class GameBoard {
    //Grid => 5 x 5 two dimensionat array
    // 5 row, 5 column
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
//            val col = move.toInt() - 1 // toInt() 已被棄用
            val col = move.toString().toInt() -1 // 對上 array index
            for (row in 0 until DIM) {
                if (grid[row][col] == Player.BLANK) {
                    grid[row][col] = currentPlayer
                    break
                }
            }
        }

        // Player X, O 輪替
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X

        // print the current grid
        for (arr in grid) {
            for (element in arr) {
                print("$element, ")
            }
        }

    }


}