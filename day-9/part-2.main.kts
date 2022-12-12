#!/usr/bin/env kotlin

import java.io.File
import kotlin.math.*

typealias Point = Pair<Int,Int>
typealias Vector = Pair<Int,Int>

val rope = MutableList(10) { Pair(0,0) }
val positions = mutableSetOf(rope.last())

File("./input")
  .readLines()
  .filter { it.isNotBlank() }
  .forEach(::process)

println(positions.size)

fun process(text:String) {
  val (direction, rawSteps) = text.split(" ")
  val steps = rawSteps.toInt()
  val vector = when(direction) {
    "U" -> Pair(0,1)
    "R" -> Pair(1,0)
    "D" -> Pair(0,-1)
    "L" -> Pair(-1, 0)
    else -> throw Exception("Unknown direction $direction")
  }

  repeat(steps) {
    rope[0] = move(rope[0], vector)

    for((index, item) in rope.withIndex().filter { it.index > 0 }) {
      val distance = calculateDistance(rope[index - 1], item)
      if (distance >= 2) {
        val vector = findTailVector(rope[index - 1], item)
        rope[index] = move(item, vector)
      }
    }

    positions.add(rope.last())
  }
}

fun calculateDistance(head:Point, tail:Point):Double =
  sqrt((tail.first - head.first).toDouble().pow(2.0) + (tail.second - head.second).toDouble().pow(2.0))


fun move(position:Point, vector:Vector):Point =
  Pair(position.first+vector.first, position.second + vector.second)

fun findTailVector(head:Pair<Int,Int>, tail:Pair<Int,Int>): Pair<Int,Int> {
  val rawVector = Pair(head.first - tail.first,head.second - tail.second)
  val length = sqrt(rawVector.first.toDouble().pow(2) + rawVector.second.toDouble().pow(2))
  val isXNegative = rawVector.first < 0
  val isYNegative = rawVector.second < 0

  val x = rawVector.first / length
  val y = rawVector.second / length

  return Pair(
    (if(isXNegative) floor(x) else ceil(x)).toInt(),
    (if(isYNegative) floor(y) else ceil(y)).toInt()
  )
}
