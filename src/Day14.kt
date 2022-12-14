import kotlin.math.sign
import kotlin.math.min
import kotlin.math.max

const val EMPTY = 0
const val BLOCKED = 1
val MOVES_SAND = listOf(0 to 1, -1 to 1, 1 to 1)

class Field(walls: List<List<Pair<Int, Int>>>, blockVoid: Boolean = false) {
    enum class MoveResultState { MOVED, BLOCKED, FREE }
    inner class MoveResult(val state: MoveResultState, val newPoint: Pair<Int, Int>? = null)

    val minY = min(0, walls.minOf { it.minOf { it2 -> it2.second } })
    val maxY = walls.maxOf { it.maxOf { it2 -> it2.second } } + if (blockVoid) 2 else 0
    val minX = min(walls.minOf { it.minOf { it2 -> it2.first } }, min(500, if (blockVoid) 500 - (maxY - minY + 2) else 500))
    val maxX = max(walls.maxOf { it.maxOf { it2 -> it2.first } }, max(500, if (blockVoid) 500 + (maxY - minY + 2) else 500))
    val field = MutableList(maxX - minX + 1) { MutableList(maxY - minY + 1) { EMPTY } }

    init {
        val walls2 = if (blockVoid) walls + listOf(listOf(Pair(minX, maxY), Pair(maxX, maxY))) else walls
        walls2.forEach {
            var currentPoint = Pair(it[0].first - minX, it[0].second - minY)
            field[currentPoint.first][currentPoint.second] = BLOCKED
            it.forEach { it2 ->
                while (currentPoint != Pair(it2.first - minX, it2.second - minY)) {
                    currentPoint = Pair(
                        currentPoint.first + (it2.first - minX - currentPoint.first).sign,
                        currentPoint.second + (it2.second - minY - currentPoint.second).sign
                    )
                    field[currentPoint.first][currentPoint.second] = BLOCKED
                }
            }
        }
    }

    fun moveIfPossible(from: Pair<Int, Int>, dir: Pair<Int, Int>): MoveResult {
        val newPoint = Pair(from.first + dir.first, from.second + dir.second)
        if (newPoint.first !in (0..(maxX - minX)) || newPoint.second !in (0..(maxY - minY))) return MoveResult(MoveResultState.FREE)
        if (field[newPoint.first][newPoint.second] == BLOCKED) return MoveResult(MoveResultState.BLOCKED)
        return MoveResult(MoveResultState.MOVED, newPoint)
    }

    fun dropSand(): Boolean {
        var sandPoint = Pair(500 - minX, 0 - minY)
        if (field[sandPoint.first][sandPoint.second] == BLOCKED) return false
        field[sandPoint.first][sandPoint.second] = BLOCKED
        while (true) {
            var movedRes = false
            MOVES_SAND.forEach {dir ->
                if (movedRes) return@forEach
                val moved = moveIfPossible(sandPoint, dir)
                if (moved.state == MoveResultState.FREE) {
                    return@dropSand false
                }
                if (moved.state == MoveResultState.MOVED) {
                    movedRes = true
                    field[sandPoint.first][sandPoint.second] = EMPTY
                    sandPoint = moved.newPoint!!
                    field[sandPoint.first][sandPoint.second] = BLOCKED
                    return@forEach
                }
            }
            if (!movedRes) break
        }
        return true
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val walls =
            input.map { it.split(" -> ").map { it2 -> it2.split(",") }.map { (x, y) -> Pair(x.toInt(), y.toInt()) } }
        val field = Field(walls)
        var answer = 0
        while (field.dropSand()) {
            answer++
        }
        return answer
    }

    fun part2(input: List<String>): Int {
        val walls =
            input.map { it.split(" -> ").map { it2 -> it2.split(",") }.map { (x, y) -> Pair(x.toInt(), y.toInt()) } }
        val field = Field(walls, true)
        var answer = 0
        while (field.dropSand()) {
            answer++
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
