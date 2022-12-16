#!/usr/bin/env kotlin

import java.io.File

val numberMatcher = """\d+""".toRegex()
val operationRegex = """^.*new = old (.) (\d+|\w+).*$""".toRegex()
val divisorRegex = """^.*divisible by (\d+).*${'$'}""".toRegex()

val monkeys = File("./input")
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

val modulo = monkeys.map { it.divisor }.reduce { acc, item -> acc * item }

repeat(10000) {
  for(monkey in monkeys) {
    monkey.inspection(modulo)
      .forEach { (i, items) ->  monkeys[i].addItems(items) }
  }
}

val result = monkeys.map {it.inspectItems}.sortedDescending().take(2).reduce { acc, item -> acc * item }
println(result)

class Monkey(
  private var heldItems:MutableList<Long>,
  val operation:(Long)->Long,
  val divisor:Long,
  private val trueChoice:Int,
  private val falseChoice:Int
) {

  var inspectItems:Long = 0

  fun inspection(modulo:Long):Map<Int,List<Long>> {
    val passedItems = heldItems.map {
      val item = operation(it) % modulo
      val nextMonkey = if (item % divisor == 0L) trueChoice else falseChoice
      inspectItems += 1
      nextMonkey to item
    }.groupBy({ it.first }, { it.second })

    heldItems = mutableListOf()

    return passedItems
  }

  fun addItems(items:List<Long>) =heldItems.addAll(items)
  override fun toString(): String {
    return "Monkey(heldItems=$heldItems,divisor=$divisor,true=$trueChoice,false=$falseChoice,inspections=$inspectItems)"
  }
}

operator fun <T> List<T>.component6() = this[5]

fun parseStartingItems(raw: String): List<Long> =
  numberMatcher.findAll(raw).map { it.value.toLong() }.toList()

fun parseOperation(raw: String): (Long) -> Long {
  val parsed = operationRegex.matchEntire(raw) ?: throw Exception("I can't match operation line [$raw]")
  val (_, operation, rawX) = parsed.groupValues
  return if(rawX == "old") {
    when(operation) {
      "*" -> {i:Long -> i * i}
      "+" -> {i:Long -> i + i}
      else -> throw Exception("Unknown operation $operation")
    }
  } else {
    val x = rawX.toLong()
    when(operation) {
      "*" -> {i:Long -> i * x}
      "+" -> {i:Long -> i + x}
      else -> throw Exception("Unknown operation $operation")
    }
  }
}

fun parseDivisor(raw:String):Long = numberMatcher.find(raw)?.value?.toLong() ?: throw Exception("no match for divisor [$raw]")

fun parseTrue(raw:String):Int = numberMatcher.find(raw)?.value?.toInt() ?: throw Exception("no match for true [$raw]")

fun parseFalse(raw:String):Int = numberMatcher.find(raw)?.value?.toInt() ?: throw Exception("no match for false [$raw]")

fun gcd(x:Long, y:Long):Long = if (y==0L) x else gcd(y, x % x)

fun Iterable<Long>.lcm(): Long = this.fold(1L) { x, y -> x * (y / gcd(x,y)) }
