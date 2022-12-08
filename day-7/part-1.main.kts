#!/usr/bin/env kotlin

import java.io.File

File("./input")
  .readLines()
  .forEach(Walker::process)

println(Walker.directories.filterKeys { it != "/" }.filterValues { it <= 100000 }.map { it.value }.sum())

object Walker {
  val directories = mutableMapOf<String,Int>()
  private var currentLocation:MutableList<String> = mutableListOf()
  private var currentState:States? = null
  fun process(line:String) {
    currentState = when(States.values().find { line.startsWith(it.command) }) {
      States.LIST -> States.LIST

      States.GO_UP -> {
        currentLocation.removeLast()
        null
      }

      States.GO_TO -> {
        currentLocation.add(line.removePrefix("$ cd ").trim())
        null
      }

      null -> {
        if (currentState == States.LIST) {
          processLine(line)
          States.LIST
        } else throw Exception("Unknown item")
      }
    }
  }

  private fun processLine(line:String) {
    if(line.startsWith("dir")) {
      return
    }

    val (rawSize) = line.split(" ")
    val size = rawSize.toInt()

    currentLocation.fold("") {
        previous,current ->
      val path = "$previous/$current".replace("//","/")
      directories[path] = directories.geœœtOrPut(path) { 0 }+size
      path
    }
  }
}

enum class States(val command:String) {
  LIST("$ ls"),
  GO_UP("$ cd .."),
  GO_TO("$ cd"),
}
