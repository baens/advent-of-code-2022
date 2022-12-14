#!/usr/bin/env kotlin

import java.io.File

val numberMatcher = """\d+""".toRegex()
val operationRegex = """^.*new = old (.) (\d|\w+).*$""".toRegex()
val divisorRegex = """^.*divisible by (\d+).*${'$'}""".toRegex()

val monkeys = File("./test")
  .readLines()
  .filter { it.isNotBlank() }
  .chunked(6)
  .map{
    val (rawStartingItems, rawOperator, rawDivisor, rawTrue, rawFalse) = it.drop(1)

    val startingItems = parseStartingItems(rawStartingItems)
    val operation = parseOperation(rawOperator)
    val divisor = parseDivisor(rawDivisor)
    val trueIndex = parseTrue(rawTrue)
    val falseIndex = parseFalse(rawFalse)

    Monkey(startingItems.toMutableList(), operation, divisor, trueIndex, falseIndex)
  }

println(monkeys)

class Monkey(
  val initialList:MutableList<Int>,
  val operation:(Int)->Int,
  val divisor:Int,
  val trueChoice:Int,
  val falseChoice:Int
)

operator fun <T> List<T>.component6() = this[5]

fun parseStartingItems(raw: String): List<Int> =
  numberMatcher.findAll(raw).map { it.value.toInt() }.toList()

fun parseOperation(raw: String): (Int) -> Int {
  val parsed = operationRegex.matchEntire(raw) ?: throw Exception("I can't match operation line [$raw]")
  val (_, operation, rawX) = parsed.groupValues
  return if(rawX == "old") {
    when(operation) {
      "*" -> {i:Int -> i * i}
      "+" -> {i:Int -> i + i}
      else -> throw Exception("Unknown operation $operation")
    }
  } else {
    val x = rawX.toInt()
    when(operation) {
      "*" -> {i:Int -> i * x}
      "+" -> {i:Int -> i + x}
      else -> throw Exception("Unknown operation $operation")
    }
  }
}

fun parseDivisor(raw:String):Int = numberMatcher.find(raw)?.value?.toInt() ?: throw Exception("no match for divisor [$raw]")

fun parseTrue(raw:String):Int = numberMatcher.find(raw)?.value?.toInt() ?: throw Exception("no match for true [$raw]")

fun parseFalse(raw:String):Int = numberMatcher.find(raw)?.value?.toInt() ?: throw Exception("no match for false [$raw]")
