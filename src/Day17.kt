import kotlin.math.max

private const val EMPTY = 0
private const val BLOCKED = 1
private val DOWN = 0 to -1
private val SHIFT = mapOf('>' to (1 to 0), '<' to (-1 to 0))

private val MINUS = listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)
private val PLUS = listOf(0 to 1, 1 to 1, 1 to 0, 1 to 2, 2 to 1)
private val CORNER = listOf(0 to 0, 1 to 0, 2 to 0, 2 to 1, 2 to 2)
private val STAND = listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3)
private val SQUARE = listOf(0 to 0, 0 to 1, 1 to 1, 1 to 0)
private val SHAPES = listOf(MINUS, PLUS, CORNER, STAND, SQUARE)

private class Field2(private val wind: String, val maxY: Int = 2022 * 4) {
    enum class MoveResultState { MOVED, BLOCKED, FREE }
    inner class MoveResult(val state: MoveResultState, val newPoint: Pair<Int, Int>? = null)
    inner class ShapeMoveResult(val state: MoveResultState, val newShape: List<Pair<Int, Int>>? = null)

    val minY = 0
    val minX = -1
    val maxX = 7
    val field = MutableList(maxX - minX + 1) { MutableList(maxY - minY + 1) { EMPTY } }
    var currentMaxHeight = 0
    var fallen = 0
    var windInd = 0

    init {
        for (x in minX..maxX) {
            field[x - minX][0] = BLOCKED
        }
        for (y in minY..maxY) {
            field[0][y] = BLOCKED
            field[8][y] = BLOCKED
        }
    }

    private fun canMove(from: Pair<Int, Int>, dir: Pair<Int, Int>): MoveResult {
        val newPoint = from + dir
        if (field[newPoint] == BLOCKED) return MoveResult(MoveResultState.BLOCKED)
        return MoveResult(MoveResultState.MOVED, newPoint)
    }

    private fun shapeCanMove(from: List<Pair<Int, Int>>, dir: Pair<Int, Int>): ShapeMoveResult {
        return ShapeMoveResult(MoveResultState.MOVED, from.map {this.canMove(it, dir).newPoint ?: return ShapeMoveResult(MoveResultState.BLOCKED)})
    }

    fun dropNextForm() {
        var figure = SHAPES[fallen % SHAPES.size].map {(x, y) -> x + 3 to currentMaxHeight + y + 3 + 1}
        figure.forEach { assert(field[it] != BLOCKED); field[it] = BLOCKED }
        while (true) {
            figure.forEach { assert(field[it] == BLOCKED); field[it] = EMPTY }
            val nextMove = shapeCanMove(figure, SHIFT[wind[windInd]]!!)
            windInd = (windInd + 1) % wind.length
            figure = nextMove.newShape ?: figure
            val afterDown = shapeCanMove(figure, DOWN)
            if (afterDown.state == MoveResultState.BLOCKED) {
                figure.forEach { assert(field[it] != BLOCKED); field[it] = BLOCKED }
                currentMaxHeight = max(currentMaxHeight, figure.maxOf { (_, y) -> y })
                break
            }
            figure = afterDown.newShape!!
            figure.forEach { assert(field[it] != BLOCKED); field[it] = BLOCKED }
        }
        fallen++
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val field = Field2(input[0])
        repeat(2022) {
            field.dropNextForm()
        }
        return field.currentMaxHeight
    }

    fun findRepetition(numbers: List<Long>): Int {
        for (k in 1..(numbers.size / 3)) {
            var success = true
            for (pos in numbers.lastIndex downTo(2*k)) {
                if (numbers[pos] != numbers[pos - k]) {
                    success = false
                    break
                }
            }
            if (success) {
                return k
            }
        }
        return -1
    }

    fun part2(input: List<String>): Long {
        val ROCKS = 1000000000000
        val iterationsAmount = input[0].length * 2
        val innerLen = 2 * 3 * 5 * 7// * SHAPES.size
        val tempResults = mutableListOf<Long>()
        val field30k = Field2(input[0], innerLen * 4 * iterationsAmount)
        var prevHeight = 0
        repeat(iterationsAmount) {
            repeat(innerLen) {
                field30k.dropNextForm()
            }
            tempResults.add((field30k.currentMaxHeight - prevHeight).toLong())
            prevHeight = field30k.currentMaxHeight
        }
        val cycleLen = findRepetition(tempResults)
        if (cycleLen == -1) {
            error("Cry")
        }
        val cycleHeight = tempResults.takeLast(cycleLen).sum()
        val dropsToCycle = (cycleLen * innerLen).toLong()
        val remainder = ROCKS % dropsToCycle
        val fieldLast = Field2(input[0], (remainder + 3 * dropsToCycle).toInt() * 4)
        repeat((remainder + 3 * dropsToCycle).toInt()) {
            fieldLast.dropNextForm()
        }
        return fieldLast.currentMaxHeight.toLong() + cycleHeight * (ROCKS / dropsToCycle - 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 3068)
    check(part2(testInput) == 1514285714288)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
