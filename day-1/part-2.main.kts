#!/usr/bin/env kotlin

import java.io.File

main()

fun main() {
  val splitByElfs = mutableListOf<Int>()
  var currentList = mutableListOf<Int>()
  for(line in File("./input")
    .readLines()) {
    if(line.isBlank()) {
      splitByElfs.add(currentList.sum())
      currentList = mutableListOf()
    } else {
      currentList.add(line.toInt())
    }
  }

  splitByElfs.add(currentList.sum())

  splitByElfs.sortDescending()

  val top3 = splitByElfs.take(3).sum()

  println(top3)
}
