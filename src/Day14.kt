import kotlin.math.sign
import kotlin.math.min
import kotlin.math.max

private const val EMPTY = 0
private const val BLOCKED = 1
private val MOVES_SAND = listOf(0 to 1, -1 to 1, 1 to 1)

private operator fun <T> List<List<T>>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]
private operator fun <E> MutableList<MutableList<E>>.set(xy: Pair<Int, Int>, value: E) {
    this[xy.first][xy.second] = value
}

private operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first - other.first) to (this.second - other.second)
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first + other.first) to (this.second + other.second)
}

class Field(walls: List<List<Pair<Int, Int>>>, blockVoid: Boolean = false) {
    enum class MoveResultState { MOVED, BLOCKED, FREE }
    inner class MoveResult(val state: MoveResultState, val newPoint: Pair<Int, Int>? = null)

    val minY = min(0, walls.minOf { it.minOf { it2 -> it2.second } })
    val maxY = walls.maxOf { it.maxOf { it2 -> it2.second } } + if (blockVoid) 2 else 0
    val minX =
        min(walls.minOf { it.minOf { it2 -> it2.first } }, min(500, if (blockVoid) 500 - (maxY - minY + 2) else 500))
    val maxX =
        max(walls.maxOf { it.maxOf { it2 -> it2.first } }, max(500, if (blockVoid) 500 + (maxY - minY + 2) else 500))
    val field = MutableList(maxX - minX + 1) { MutableList(maxY - minY + 1) { EMPTY } }

    init {
        val walls2 = if (blockVoid) walls + listOf(listOf(Pair(minX, maxY), Pair(maxX, maxY))) else walls
        walls2.forEach {
            var currentPoint = it[0] - Pair(minX, minY)
            field[currentPoint] = BLOCKED
            it.forEach { it2 ->
                while (currentPoint != it2 - Pair(minX, minY)) {
                    currentPoint += Pair(
                        (it2.first - minX - currentPoint.first).sign, (it2.second - minY - currentPoint.second).sign
                    )
                    field[currentPoint] = BLOCKED
                }
            }
        }
    }

    private fun canMove(from: Pair<Int, Int>, dir: Pair<Int, Int>): MoveResult {
        val newPoint = from + dir
        if (newPoint.first !in (0..(maxX - minX)) || newPoint.second !in (0..(maxY - minY)))
            return MoveResult(MoveResultState.FREE)
        if (field[newPoint] == BLOCKED) return MoveResult(MoveResultState.BLOCKED)
        return MoveResult(MoveResultState.MOVED, newPoint)
    }

    fun dropSand(): Boolean {
        var sandPoint = Pair(500, 0) - Pair(minX, minY)
        if (field[sandPoint] == BLOCKED) return false
        field[sandPoint] = BLOCKED
        while (true) {
            var movedRes = false
            run moves@{
                MOVES_SAND.forEach { dir ->
                    val moved = canMove(sandPoint, dir)
                    if (moved.state == MoveResultState.FREE) {
                        return@dropSand false
                    }
                    if (moved.state == MoveResultState.MOVED) {
                        movedRes = true
                        field[sandPoint] = EMPTY
                        sandPoint = moved.newPoint!!
                        field[sandPoint] = BLOCKED
                        return@moves
                    }
                }
            }
            if (!movedRes) break
        }
        return true
    }
}

fun main() {
    fun howMuchSand(field: Field): Int {
        var answer = 0
        while (field.dropSand()) {
            answer++
        }
        return answer
    }

    fun parseWalls(input: List<String>) =
        input.map { it.split(" -> ").map { it2 -> it2.split(",") }.map { (x, y) -> Pair(x.toInt(), y.toInt()) } }

    fun part1(input: List<String>): Int {
        return howMuchSand(Field(parseWalls(input)))
    }

    fun part2(input: List<String>): Int {
        return howMuchSand(Field(parseWalls(input), true))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
