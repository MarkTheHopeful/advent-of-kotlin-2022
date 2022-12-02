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
    if (my == other) return Outcome.DRAW
    if (my == win(other)) return Outcome.WIN
    return Outcome.LOSE
}

fun main() {
    val toTurn = mapOf(
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
        var totalScore = 0
        for (line in input) {
            totalScore += outcome(toTurn[line[2]]!!, toTurn[line[0]]!!).score + toTurn[line[2]]!!.score
        }
        return totalScore
    }

    fun part2(input: List<String>): Int {
        var totalScore = 0
        for (line in input) {
            val desired = toOutcome[line[2]]!!
            val opponent = toTurn[line[0]]!!
            totalScore += desired.score
            val myTurn = when (desired) {
                Outcome.DRAW -> opponent
                Outcome.WIN -> win(opponent)
                Outcome.LOSE -> win(win(opponent))
            }
            totalScore += myTurn.score
        }
        return totalScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
