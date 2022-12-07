#!/usr/bin/env kotlin

import java.io.File

File("./input")
  .readLines()
  .forEach(Walker::process)

processSize(Walker.root)
val dirs = findAllDirectories(Walker.root).sortedBy { it.size }.reversed()
val unusedSpace = 30000000 - (70000000 - dirs.first().size)
val dir = dirs.findLast { it.size > unusedSpace } ?: throw Exception("cant find")

println("${dir.name} ${dir.size}")


fun processSize(current: Objects.Directory) {
  current.contents
    .filterValues { it is Objects.Directory }
    .mapValues { it.value }
    .forEach { processSize(it.value as Objects.Directory) }

  current.size = current.contents.values.sumOf { it.size }
}

fun findAllDirectories(current: Objects.Directory):List<Objects.Directory> {
  val returnList = current.contents
    .mapNotNull { it.value as? Objects.Directory}

  return returnList + current.contents
    .filterValues { it is Objects.Directory }
    .flatMap {findAllDirectories(it.value as Objects.Directory) }
}

object Walker {
  val root:Objects.Directory = Objects.Directory("")
  private var currentLocation: MutableList<Objects.Directory> = mutableListOf()
  private var currentState:States? = null
  fun process(line:String) {
    currentState = when(States.values().find { line.startsWith(it.command) }) {
      States.LIST -> States.LIST

      States.GO_UP -> {
        currentLocation.removeLast()
        null
      }

      States.GO_TO -> {
        currentLocation.add(Objects.Directory(line.removePrefix("$ cd ").trim()))
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

    val (rawSize, name) = line.split(" ")
    val size = rawSize.toInt()

    val directory = currentLocation.fold(root) {
      current, item ->
        current.contents.getOrPut(item.name) { Objects.Directory(item.name) }
          as Objects.Directory
    }

    directory.contents[name] = Objects.File(name, size)
  }
}

enum class States(val command:String) {
  LIST("$ ls"),
  GO_UP("$ cd .."),
  GO_TO("$ cd"),
}

sealed class Objects(var size: Int) {
  data class Directory(val name: String, val contents: MutableMap<String, Objects> = mutableMapOf(), val totalSize:Int=0):Objects(totalSize)
  data class File(val name: String, val fileSize: Int):Objects(fileSize)
}

fun walkTree(spacing:Int, current: Objects.Directory) {
  val padding = " ".repeat(spacing*2)
  println("$padding-${current.name}[${current.size}]")
  current.contents
    .filterValues { it is Objects.File }
    .mapValues { it.value }
    .forEach { println("$padding${it.key}[${(it.value as Objects.File).size}]")}
  current.contents
    .filterValues { it is Objects.Directory }
    .mapValues { it.value }
    .forEach { walkTree(spacing+1, it.value as Objects.Directory)}
}
