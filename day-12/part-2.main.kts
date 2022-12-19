#!/usr/bin/env kotlin
import java.io.File

typealias Point = Pair<Int,Int>

val END = -2
val validMoves = listOf(Point(0,-1), Point(1,0), Point(0,1), Point(-1,0))

var heights = ('a'..'z')
  .mapIndexed { index, item -> index to item.toString()}
  .plus(0 to "S" )
  .plus(END to "E" )
  .associateBy(Pair<Int,String>::second,Pair<Int,String>::first)

val map = File("./input").readText().createMap()
val endLocation = map.findEndLocation()

val shortestPathLength = map
  .flatMapIndexed { y, row -> row.mapIndexed { x, item -> (x to y) to item }}
  .filter { it.second == heights["a"] }
  .map { it.first }
  .minOf { runCatching { search(it).size }.getOrDefault(Int.MAX_VALUE) }

println(shortestPathLength - 1)

fun search(startLocation: Point):List<Point> {
  val openVertices = mutableSetOf(startLocation)
  val closedVertices = mutableSetOf<Point>()
  val costFromStart = mutableMapOf(startLocation to 0)
  val estimatedTotalCost = mutableMapOf(startLocation to (startLocation distanceTo endLocation))
  val cameFrom = mutableMapOf<Point,Point>()
  while(openVertices.size > 0) {
    val currentLocation = openVertices.minByOrNull { estimatedTotalCost[it] ?: throw Exception("No estimated cost for [$it]") } ?: throw Exception("No open vertices")

    if(currentLocation == endLocation) {
      return sequence {
        var c:Point? = currentLocation
        yield(c as Point)
        while(cameFrom[c].also { c = it } != null) {
          yield(c as Point)
        }
      }.toList().reversed()
    }

    openVertices.remove(currentLocation)
    closedVertices.add(currentLocation)

    val nextMoves = validMoves
      .map { currentLocation.first + it.first to currentLocation.second + it.second}
      .filterNot { closedVertices.contains(it) }

    val currentHeight = map[currentLocation.second][currentLocation.first]

    val currentCost = costFromStart[currentLocation] ?: throw Exception("No cost for current location")

    for(nextLocation in nextMoves) {
      val nextHeight = when(val height = map.getOrNull(nextLocation.second)?.getOrNull(nextLocation.first)) {
        END -> heights["z"] ?: throw Exception("Cant find end height")
        else -> height
      }

      if(nextHeight == null || nextHeight - currentHeight > 1) continue

      val score = currentCost + 1
      if(score >= costFromStart.getOrDefault(nextLocation, Int.MAX_VALUE)) continue

      openVertices.add(nextLocation)

      cameFrom[nextLocation] = currentLocation
      costFromStart[nextLocation] = score
      estimatedTotalCost[nextLocation] = score + (nextLocation distanceTo endLocation)
    }
  }
  throw Exception("No path found")
}

fun String.createMap(): List<List<Int>> = this.split("\n")
  .map { line -> line.trim()
    .split("")
    .filter(String::isNotBlank)
    .map { heights[it] ?: throw Exception("Unknown Item [$it]")}
    .toList()
  }

fun List<List<Int>>.findEndLocation(): Point = this.findItem(END)

fun List<List<Int>>.findItem(itemToFind:Int): Point = this.asSequence().flatMapIndexed { y, row -> row.mapIndexed { x, item -> item to (x to y)}}.first { it.first == itemToFind }.second

infix fun Point.distanceTo(to:Point): Int {
  val dx = kotlin.math.abs(this.first - to.first)
  val dy = kotlin.math.abs(this.second - to.second)
  return dx + dy
}
