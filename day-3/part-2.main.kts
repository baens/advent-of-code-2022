#!/usr/bin/env kotlin

import java.io.File

val priority = listOf(
  'a'..'z',
  'A'..'Z'
)
  .flatten()
  .withIndex()
  .associate { it.value to it.index+1 }

val result = File("./input")
  .readLines()
  .asSequence()
  .map {
    it
      .trim()
      .toCharArray()
      .toSet()
  }
  .chunked(3)
  .map {
    it.reduce { acc, chars ->  acc.intersect(chars)}
  }
  .flatten()
  .sumOf { priority[it] ?: throw Exception("Item not found $it") }

println(result)
