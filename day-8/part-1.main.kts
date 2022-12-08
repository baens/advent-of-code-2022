#!/usr/bin/env kotlin

import java.io.File

val items = mutableListOf<List<Int>>()

File("./input")
  .readLines()
  .forEach(::loadData)

println(searchForTrees()+items.size+items.size-1+items.size-1+items.size-2)

fun loadData(line:String) {
  items.add(line.trim().split("").filter(String::isNotBlank).map(String::toInt))
}

fun searchForTrees():Int =
  (1 until items.size-1)
    .flatMap { row -> (1 until items[0].size-1).map { col -> Pair(row,col) } }
    .filter { (row,col) -> checkVisibility(row,col) }
    .size

fun checkVisibility(row:Int, col:Int):Boolean {
  val loc = items[row][col]

  if((0 until row).map { Pair(it,col) }.all { loc > items[it.first][it.second] }) return true
  if((col+1 until items[0].size).map{ Pair(row,it) }.all { loc > items[it.first][it.second]}) return true
  if((row+1 until items.size).map { Pair(it,col) }.all { loc > items[it.first][it.second]}) return true
  if((0 until col).map { Pair(row,it) }.all { loc > items[it.first][it.second]}) return true

  return false
}
