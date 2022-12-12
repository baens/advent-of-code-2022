#!/usr/bin/env kotlin

import java.io.File

val checkPoints = setOf(20,60,100,140,180,220)

val result = File("./input")
  .readLines()
  .asSequence()
  .flatMap(CPU::process)
  .filter { checkPoints.contains(it.first) }
  .sumOf {
    it.first * it.second
  }

println(result)

object CPU {
  private var value = 1
  private var cycle = 1
  fun process(text:String) = sequence {
    if(text == "noop") {
      yield(Pair(cycle,value))
      cycle += 1
    } else {
      val (operation,rawValue) = text.split(" ")
      yield(Pair(cycle,value))
      cycle += 1

      yield(Pair(cycle,value))
      value += rawValue.toInt()
      cycle += 1
    }
  }
}
