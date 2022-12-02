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

    val opponentChoice = Choice.valueOf(split[0])
    val minePick = pickMine(opponentChoice.pick, Choice.valueOf(split[1]))

    Pair(opponentChoice.pick, minePick)
  }.sumOf {
    val shapeScore = it.second.points
    val outcomeScore = calculateGameScore(it.first, it.second)
    shapeScore + outcomeScore
  }

fun pickMine(opponent: Pick, mine: Choice):Pick {
  if(mine == Choice.Y) return opponent

  return when(mine) {
    Choice.X -> when(opponent) {
      Pick.SCISSORS -> Pick.PAPER
      Pick.ROCK -> Pick.SCISSORS
      Pick.PAPER -> Pick.ROCK
    }

    Choice.Z -> when(opponent) {
      Pick.SCISSORS -> Pick.ROCK
      Pick.ROCK -> Pick.PAPER
      Pick.PAPER -> Pick.SCISSORS
    }

    else -> throw Exception("Unknown what choice ${mine}")
  }
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
