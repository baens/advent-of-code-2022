#!/usr/bin/env kotlin

import java.io.File

val items = mutableListOf<List<Int>>()

File("./input")
  .readLines()
  .forEach(::loadData)

println(searchForTrees())

fun loadData(line:String) {
  items.add(line.trim().split("").filter(String::isNotBlank).map(String::toInt))
}

fun searchForTrees():Int =
  (1 until items.size-1)
    .flatMap { row -> (1 until items[0].size-1).map { col -> Pair(row,col) } }
    .maxOf { (row,col) -> viewScore(row,col) }

fun viewScore(row: Int, col: Int): Int {
  val loc = items[row][col]

  var upScore = (0 until row).reversed().map { Pair(it, col) }.indexOfFirst { loc <= items[it.first][it.second] }
  if (upScore == -1) {
    upScore = (0 until row).toList().size
  } else {
    upScore += 1
  }

  var rightScore = (col + 1 until items[0].size).map { Pair(row, it) }.indexOfFirst { loc <= items[it.first][it.second] }
  if (rightScore == -1) {
    rightScore = (col + 1 until items[0].size).toList().size
  } else {
    rightScore += 1
  }

  var downScore = (row + 1 until items.size).map { Pair(it, col) }.indexOfFirst { loc <= items[it.first][it.second] }
  if (downScore == -1) {
    downScore = (row + 1 until items.size).toList().size
  } else {
    downScore += 1
  }

  var leftScore = (0 until col).reversed().map { Pair(row, it) }.indexOfFirst { loc <= items[it.first][it.second] }
  if (leftScore == -1) {
    leftScore = (0 until col).toList().size
  } else {
    leftScore += 1
  }

  return upScore * rightScore * downScore * leftScore
}
