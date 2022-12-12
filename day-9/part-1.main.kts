#!/usr/bin/env kotlin

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

typealias Point = Pair<Int,Int>
typealias Vector = Pair<Int,Int>

var headPosition = Pair(0,0)
var tailPosition = Pair(0,0)
val positions = mutableSetOf(tailPosition)

File("./input")
  .readLines()
  .filter { it.isNotBlank() }
  .forEach(::process)

println(positions.size)

fun process(text:String) {
  val (direction, rawSteps) = text.split(" ")
  val steps = rawSteps.toInt()
  val vector = when(direction) {
    "U" -> Pair(1,0)
    "R" -> Pair(0,1)
    "D" -> Pair(-1,0)
    "L" -> Pair(0,-1)
    else -> throw Exception("Unknown direction ${direction}")
  }

  repeat(steps) {
    val lastPosition = headPosition
    headPosition = move(headPosition, vector)
    val distance = calculateDistance(headPosition, tailPosition)
    if(distance >= 2) {
      tailPosition = lastPosition
      positions.add(tailPosition)
    }
  }
}

fun calculateDistance(head:Point, tail:Point):Double =
  sqrt((tail.first - head.first).toDouble().pow(2.0) + (tail.second - head.second).toDouble().pow(2.0))


fun move(position:Point, vector:Vector):Point =
  Pair(position.first+vector.first, position.second + vector.second)
