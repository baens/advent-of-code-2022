#!/usr/bin/env kotlin

import java.io.File
import kotlin.math.abs

File("./input")
  .readLines()
  .asSequence()
  .flatMap(CPU::process)
  .chunked(40)
  .forEach {
    for((index, cycle) in it.withIndex()) {
      if(abs(cycle.second - index) <= 1) {
        print("#")
      } else {
        print(".")
      }
    }
    println("")
  }

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
