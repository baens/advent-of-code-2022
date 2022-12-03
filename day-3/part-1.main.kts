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
  .map {
    val fullItems = it
      .trim()
      .toCharArray()

    val container1 = fullItems.copyOfRange(0, (fullItems.size)/2).toSet()
    val container2 = fullItems.copyOfRange((fullItems.size)/2, fullItems.size).toSet()

    container1.intersect(container2)
  }
  .flatten()
  .sumOf { priority[it] ?: throw Exception("Item not found $it") }

println(result)
