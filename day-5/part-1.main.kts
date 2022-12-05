#!/usr/bin/env kotlin


import java.io.File

val groupMatch = """\[(\w)\]""".toRegex()
val operationsMatch = """move (\w+) from (\w) to (\w)""".toRegex()

val contents = File("./input").readText()

val (stacks, operations) = contents.split("\n\n")

val parsedStacks = parseStacks(stacks)
val result = runOperations(parsedStacks, operations)

println(result.joinToString(""))

fun parseStacks(rawStacks:String):MutableMap<Int, MutableList<String>> =
        rawStacks.split("\n")
            .reversed()
            .asSequence()
            .drop(1)
            .map { line ->
                line.chunked(4)
                        .map {
                            groupMatch.find(it)?.groups?.get(1)?.value
                        }
            }
            .map {
                it.mapIndexedNotNull { index, item ->
                    if (item != null)
                        Pair(index+1, item)
                    else
                        null
                }
            }
            .flatten()
            .groupBy { it.first }
            .mapValues { mapItem -> mapItem.value.map { it.second }.toMutableList()}
            .toMutableMap()

fun runOperations(stacks:MutableMap<Int,MutableList<String>>, operations:String):List<String> {
    val parsedOperations = operations
            .split("\n")
            .mapNotNull {
                val (number, startColumn, endColumn) = operationsMatch.find(it)?.groupValues?.drop(1) ?: return@mapNotNull null
                Operation(number.toInt(), startColumn.toInt(), endColumn.toInt())
            }


    for(operation in parsedOperations) {
        val objects = (1..operation.number).map { stacks[operation.startColumn]!!.removeLast() }
        stacks[operation.endColumn]!!.addAll(objects)
    }

    return stacks.mapNotNull { it.value.lastOrNull() }
}

data class Operation(val number:Int,val startColumn:Int, val endColumn:Int)