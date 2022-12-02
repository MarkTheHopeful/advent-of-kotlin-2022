enum class Shape(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3)
}

enum class Outcome(val score: Int) {
    WIN(6), DRAW(3), LOSE(0)
}

fun win(t: Shape): Shape {
    return when (t) {
        Shape.ROCK -> Shape.PAPER
        Shape.PAPER -> Shape.SCISSORS
        else -> Shape.ROCK
    }
}

fun outcome(my: Shape, other: Shape): Outcome {
    return when (my) {
        other -> Outcome.DRAW
        win(other) -> Outcome.WIN
        else -> Outcome.LOSE
    }
}

fun main() {
    val toShape = mapOf(
        Pair('A', Shape.ROCK),
        Pair('X', Shape.ROCK),
        Pair('B', Shape.PAPER),
        Pair('Y', Shape.PAPER),
        Pair('C', Shape.SCISSORS),
        Pair('Z', Shape.SCISSORS)
    )
    val toOutcome = mapOf(
        Pair('X', Outcome.LOSE), Pair('Y', Outcome.DRAW), Pair('Z', Outcome.WIN)
    )

    fun part1(input: List<String>): Int {
        return input.fold(0) { score, line ->
            score + outcome(
                toShape[line[2]]!!,
                toShape[line[0]]!!
            ).score + toShape[line[2]]!!.score
        }
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) {score, line ->
            val desired = toOutcome[line[2]]!!
            val opponent = toShape[line[0]]!!
            val myShape = when (desired) {
                Outcome.DRAW -> opponent
                Outcome.WIN -> win(opponent)
                Outcome.LOSE -> win(win(opponent))
            }
            score + myShape.score + desired.score
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
