import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

private val MOVES: Map<Char, Pair<Int, Int>> =
    mapOf(Pair('U', Pair(0, 1)), Pair('D', Pair(0, -1)), Pair('L', Pair(-1, 0)), Pair('R', Pair(1, 0)))

fun main() {
    fun kingDist(a: Pair<Int, Int>, b: Pair<Int, Int>): Int {
        return max(abs(a.first - b.first), abs(a.second - b.second))
    }

    fun moveOneStep(from: Pair<Int, Int>, to: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(from.first + (to.first - from.first).sign, from.second + (to.second - from.second).sign)
    }

    fun applyMove(a: Pair<Int, Int>, dir: Char, len: Int) =
        Pair(a.first + MOVES[dir]!!.first * len, a.second + MOVES[dir]!!.second * len)

    fun walkAsRope(input: List<String>, ropeLen: Int): Int {
        val visitedPoints: MutableSet<Pair<Int, Int>> = mutableSetOf(Pair(0, 0))
        val currentParts = Array(ropeLen) { Pair(0, 0) }
        input.map { it.split(" ") }.map { (charS, lenS) -> Pair(charS[0], lenS.toInt()) }.forEach { (char, len) ->
            currentParts[0] = applyMove(currentParts[0], char, len)
            while (kingDist(currentParts[0], currentParts[1]) > 1) {
                for (i in 1 until currentParts.size) {
                    if (kingDist(currentParts[i - 1], currentParts[i]) > 1)
                        currentParts[i] = moveOneStep(currentParts[i], currentParts[i - 1])
                    if (i == currentParts.lastIndex) visitedPoints.add(currentParts[i])
                }
            }
        }
        return visitedPoints.size
    }

    fun part1(input: List<String>): Int {
        return walkAsRope(input, 2)
    }

    fun part2(input: List<String>): Int {
        return walkAsRope(input, 10)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 88)
    check(part2(testInput) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
