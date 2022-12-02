#!/usr/bin/env kotlin

import java.io.File

enum class Pick(val points: Int) {
  ROCK(1),
  PAPER(2),
  SCISSORS(3)
}

enum class Choice(val pick:Pick) {
  A(Pick.ROCK),
  B(Pick.PAPER),
  C(Pick.SCISSORS),
  X(Pick.ROCK),
  Y(Pick.PAPER),
  Z(Pick.SCISSORS)
}

val data = File("./input")
  .readLines()
  .mapNotNull {
    if (it.isBlank()) return@mapNotNull null

    val split = it.trim().split(" ")

    Pair(Choice.valueOf(split[0]).pick, Choice.valueOf(split[1]).pick)
  }.sumOf {
    val shapeScore = it.second.points
    val outcomeScore = calculateGameScore(it.first, it.second)
    shapeScore + outcomeScore
  }

fun calculateGameScore(opponent: Pick, mine: Pick):Int {
  if(opponent == mine) return 3

  return when(Pair(opponent, mine)) {
    Pick.ROCK to Pick.SCISSORS -> 0
    Pick.ROCK to Pick.PAPER -> 6
    Pick.PAPER to Pick.ROCK -> 0
    Pick.PAPER to Pick.SCISSORS -> 6
    Pick.SCISSORS to Pick.PAPER -> 0
    Pick.SCISSORS to Pick.ROCK -> 6
    else -> throw Exception("Unknown answer ${opponent} : ${mine}")
  }
}


println(data)
