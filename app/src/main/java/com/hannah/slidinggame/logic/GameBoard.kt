package com.hannah.slidinggame.logic

import kotlin.random.Random

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
        for (arr in grid) {
            println("")
            for (element in arr) {
                print("$element, ")
            }
            println("")
        }
        println("=====")

    }
    fun checkWinners(): Player {
        val winners: ArrayList<Player> = ArrayList()
        // check all rows
        for (i in 0 until DIM) {
            // 第一格不是空的，表示遊戲開始
            if (grid[i][0] != Player.BLANK) {
                val firstElement = grid[i][0]
                var allTheSame = true // row 裡的每個值都一樣
                for (j in 0 until DIM) {
                    if (grid[i][j] != firstElement) {
                        allTheSame = false
                        break
                    }
                }
                // 有相同的整條 row 是同樣標示，就不要再加到贏家(winners)裡
                if (allTheSame && !winners.contains(firstElement)) {
                    winners.add(firstElement)
                }
            }
        }
        if (winners.size == 1) {
            return winners[0]
        }else if (winners.size > 1) {
            return Player.TIE // 平手的情況
        }

        // check all columns
        for (i in 0 until DIM) {
            if (grid[0][i] != Player.BLANK) {
                val firstElement = grid[0][i]
                var allTheSame = true
                for (j in 0 until DIM) {
                    if (grid[j][i] != firstElement) {
                        allTheSame = false
                        break
                    }
                }
                if (allTheSame && !winners.contains(firstElement)) {
                    winners.add(firstElement)
                }
            }
        }
        if (winners.size == 1) {
            return winners[0]
        }else if (winners.size > 1) {
            return Player.TIE
        }

        // check diagonals
        if (grid[0][0] != Player.BLANK) {
            val firstElement = grid[0][0]
            var allTheSame = true
            for (i in 0 until DIM) {
                if (grid[i][i] != firstElement) {
                    allTheSame = false
                    break
                }
            }
            if (allTheSame) {
                return firstElement
            }
        }

        if (grid[DIM-1][0] != Player.BLANK) {
            val firstElement = grid[DIM-1][0]
            var allTheSame = true
            for (i in 0 until DIM) {
                if (grid[DIM-1-i][i] != firstElement) {
                    allTheSame = false
                    break
                }
            }
            if (allTheSame) {
                return firstElement
            }
        }

        return Player.BLANK
    }

}

