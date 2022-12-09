import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

val MOVES: Map<Char, Pair<Int, Int>> =
    mapOf(Pair('U', Pair(0, 1)), Pair('D', Pair(0, -1)), Pair('L', Pair(-1, 0)), Pair('R', Pair(1, 0)))

fun main() {
    fun kingDist(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        return max(abs(a.first - b.first), abs(a.second - b.second))
    }

    fun part1(input: List<String>): Int {
        val visitedPoints: MutableSet<Pair<Int, Int>> = mutableSetOf(Pair(0, 0))
        var currentHead = Pair(0, 0)
        var currentTail = Pair(0, 0)
        input.map { it.split(" ") }.map { (char, lenS) -> Pair(char[0], lenS.toInt()) }.forEach { (char, len) ->
            currentHead =
                Pair(currentHead.first + MOVES[char]!!.first * len, currentHead.second + MOVES[char]!!.second * len)
            while (kingDist(currentHead, currentTail) > 1) {
                currentTail = Pair(
                    currentTail.first + (currentHead.first - currentTail.first).sign,
                    currentTail.second + (currentHead.second - currentTail.second).sign
                )
                visitedPoints.add(currentTail)
            }
        }
        return visitedPoints.size
    }

    fun part2(input: List<String>): Int {
        val visitedPoints: MutableSet<Pair<Int, Int>> = mutableSetOf(Pair(0, 0))
        val currentParts = Array(10) { Pair(0, 0) }
        input.map { it.split(" ") }.map { (char, lenS) -> Pair(char[0], lenS.toInt()) }.forEach { (char, len) ->
            currentParts[0] = Pair(
                currentParts[0].first + MOVES[char]!!.first * len,
                currentParts[0].second + MOVES[char]!!.second * len
            )
            while (kingDist(currentParts[0], currentParts[1]) > 1) {
                for (i in 1 until currentParts.size) {
                    if (kingDist(currentParts[i - 1], currentParts[i]) > 1)
                        currentParts[i] = Pair(
                            currentParts[i].first + (currentParts[i - 1].first - currentParts[i].first).sign,
                            currentParts[i].second + (currentParts[i - 1].second - currentParts[i].second).sign
                        )
                    if (i == 9)
                        visitedPoints.add(currentParts[i])
                }
            }

        }
        return visitedPoints.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
//    check(part1(testInput) == 13)
    println(part1(testInput))
    println(part2(testInput))
//    check(part2(testInput) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
