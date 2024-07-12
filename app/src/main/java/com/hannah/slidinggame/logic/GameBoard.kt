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
        // 直向推移
        if (move in '1'..'5') {
//            val column = move.toInt() - 1 // toInt() 已被棄用
            val column = move.toString().toInt() - 1 // 對上 array index
            val neighbors = ArrayList<Player>()

            for (row in 0 until DIM) {
                if (grid[row][column] != Player.BLANK) {
                    neighbors.add(grid[row][column])
                }else {
                    break
                }
            }

            // 推移表格上的 Player
            for (index in neighbors.indices) {
                if (index + 1 < DIM) {
                    grid[index + 1 ][column] = neighbors[index]
                }

            }
            grid[0][column] = currentPlayer

        // 橫向推移
        }else {
            var row = (move.code - 'A'.code)
            val neighbors = ArrayList<Player>()
            for (column in 0 until DIM) {
                if (grid[row][column] != Player.BLANK) {
                    neighbors.add(grid[row][column])
                }else {
                    break
                }
            }
            for (index in neighbors.indices) {
                if (index + 1 < DIM) {
                    grid[row][index + 1] = neighbors[index]
                }
            }
            grid[row][0] = currentPlayer
        }
        // Player X, O 輪替
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X

        // print the current grid
        println("=====")
        for (arr in grid) {
            for (element in arr) {

                print("$element, ")
            }
            println("=====")
        }

    }

}