private enum class Shape(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3)
}

private enum class Outcome(val score: Int) {
    WIN(6), DRAW(3), LOSE(0)
}

private val Char.shape: Shape
    get() = when (this) {
        'A', 'X' -> Shape.ROCK
        'B', 'Y' -> Shape.PAPER
        'C', 'Z' -> Shape.SCISSORS
        else -> error("AX BY CZ")
    }

private val Char.outcome: Outcome
    get() = when (this) {
        'X' -> Outcome.LOSE
        'Y' -> Outcome.DRAW
        'Z' -> Outcome.WIN
        else -> error("XYZ")
    }

private fun win(t: Shape): Shape {
    return when (t) {
        Shape.ROCK -> Shape.PAPER
        Shape.PAPER -> Shape.SCISSORS
        else -> Shape.ROCK
    }
}

private fun outcome(my: Shape, other: Shape): Outcome {
    return when (my) {
        other -> Outcome.DRAW
        win(other) -> Outcome.WIN
        else -> Outcome.LOSE
    }
}

private fun applyOutcomeFor(outcome: Outcome, opponent: Shape): Shape = when (outcome) {
    Outcome.DRAW -> opponent
    Outcome.WIN -> win(opponent)
    Outcome.LOSE -> win(win(opponent))
}

fun main() {
    fun part1(input: List<String>): Int = input.map { line -> Pair(line[0].shape, line[2].shape) }
        .sumOf { (opponent, me) -> me.score + outcome(me, opponent).score }


    fun part2(input: List<String>): Int = input.map { line -> Pair(line[0].shape, line[2].outcome) }
        .sumOf { (opponent, outcome) -> outcome.score + applyOutcomeFor(outcome, opponent).score }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
