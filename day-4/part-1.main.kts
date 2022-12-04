#!/usr/bin/env kotlin

import java.io.File

val result = File("./input")
  .readLines()
  .map {
    val rawRanges = it.trim().split(",")
    val range1 = rawRanges[0].toRangeSet()
    val range2 = rawRanges[1].toRangeSet()
    val intersection = range1.intersect(range2)
    intersection.size == range1.size || intersection.size == range2.size
  }.count { it }

println(result)

fun String.toRangeSet(): Set<Int> {
  val split = this.split("-")
  val lowerRange = split[0].toInt()
  val upperRange = split[1].toInt()
  return lowerRange.rangeTo(upperRange).toSet()
}
