import java.util.*
import kotlin.math.max
import kotlin.math.min

private data class Blueprint(val totalCosts: List<List<Int>>)
private data class State(val robotBuilt: List<Int>, var resources: List<Int>)

private const val SHIFT_1: Long = 25
private const val SHIFT_2: Long = 340
private const val BASE: Long = SHIFT_1 * SHIFT_1 * SHIFT_1 * SHIFT_1

private fun State.encode(): Long {
    return 1L * robotBuilt[0] + robotBuilt[1] * SHIFT_1 + robotBuilt[2] * SHIFT_1 * SHIFT_1 + robotBuilt[3] * SHIFT_1 * SHIFT_1 * SHIFT_1 +
            BASE * resources[0] + BASE * SHIFT_2 * resources[1] + BASE * SHIFT_2 * SHIFT_2 * resources[2]
}

fun main() {
    fun parseBlueprint(line: String): Blueprint {
        val rawValues =
            line.split('.').map { it.split(' ').takeLast(5).mapNotNull { it2 -> it2.toIntOrNull() } }.dropLast(1)
        return Blueprint(
            listOf(
                listOf(rawValues[0][0], 0, 0),
                listOf(rawValues[1][0], 0, 0),
                listOf(rawValues[2][0], rawValues[2][1], 0),
                listOf(rawValues[3][0], 0, rawValues[3][1])
            )
        )
    }

    fun maximizeGeodes(
        blueprint: Blueprint, time: Int
    ): Int {
        val geodeMemory = mutableSetOf<Long>()
        val q: Queue<Pair<State, Pair<Int, Int>>> = LinkedList()
        q.add(State(listOf(1, 0, 0, 0), listOf(0, 0, 0)) to (0 to time))
        var maxAnswer = 0
        var state: State
        var stepsLeft: Int
        var realGeodes: Int
        var newState: State
        var failedOnce: Boolean
        var bestResult = 0
        var bestProductionResult = 0
        while (q.isNotEmpty()) {
            state = q.first().first
            stepsLeft = q.first().second.second
            realGeodes = q.remove().second.first + state.robotBuilt[3]
            bestResult = max(bestResult, realGeodes)
            if (realGeodes + min(2, stepsLeft) < bestResult) continue // zero proof
            bestProductionResult = max(bestProductionResult, state.robotBuilt[3])
            if (stepsLeft == 0) {
                maxAnswer = max(realGeodes - state.robotBuilt[3], maxAnswer)
                continue
            }
            failedOnce = false
            for (whichBuild in 0..4) {
                newState = if (whichBuild == 4) {
                    if (!failedOnce) continue
                    State(
                        state.robotBuilt,
                        state.resources.withIndex().map { it.value + state.robotBuilt[it.index] }
                    )
                } else {
                    if (state.resources.withIndex().any { it.value < blueprint.totalCosts[whichBuild][it.index] }) {
                        failedOnce = true; continue
                    }
                    State(
                        state.robotBuilt.withIndex().map { it.value + if (it.index == whichBuild) 1 else 0 },
                        state.resources.withIndex()
                            .map { it.value + state.robotBuilt[it.index] - blueprint.totalCosts[whichBuild][it.index] }
                    )
                }

                if (newState.encode() !in geodeMemory) {
                    geodeMemory.add(newState.encode())
                    q.add(newState to (realGeodes to stepsLeft - 1))
                }
            }
        }
        return maxAnswer
    }

    fun part1(input: List<String>): Int {
        val blueprints = input.map(::parseBlueprint)
        return blueprints.withIndex()
            .sumOf { (it.index + 1) * maximizeGeodes(it.value, 24)}
    }

    fun part2(input: List<String>): Int {
        val blueprints = input.map(::parseBlueprint)
        return blueprints.take(3).fold(1) { acc, value -> acc * maximizeGeodes(value, 32) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 62 * 56)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
