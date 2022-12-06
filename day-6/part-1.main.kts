#!/usr/bin/env kotlin

import java.io.File

val test1 = findSignal("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
println("Test 1: $test1 ${test1==7}")
val test2 = findSignal("bvwbjplbgvbhsrlpgdmjqwftvncz")
println("Test 2 $test2 ${test2==5}")
val test3 = findSignal("nppdvjthqldpwncqszvftbrmjlhg")
println("Test 3 $test3 ${test3==6}")
val test4 = findSignal("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
println("Test 4 $test4 ${test4==10}")
val test5 = findSignal("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
println("Test 5 $test5 ${test5==11}")
println(findSignal(File("./input").readText()))

fun findSignal(text:String):Int {
  val buffer = mutableListOf<Char>()
  text.forEachIndexed { index, a->
    buffer.add(a)

    if(buffer.size < 4) return@forEachIndexed

    if(buffer.distinct().size == 4) return index+1

    buffer.removeFirst()
  }

  return -1
}
