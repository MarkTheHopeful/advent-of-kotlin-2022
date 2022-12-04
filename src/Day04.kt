fun main() {
    fun fullyIn(range1: IntRange, range2: IntRange): Boolean {
        return (range1.first in range2 && range1.last in range2)
    }

    fun overlap(range1: IntRange, range2: IntRange): Boolean {
        return (range1.first in range2 || range1.last in range2)
    }

    fun withRangesParsed(input: List<String>, function: (IntRange, IntRange) -> Boolean): Int {
        return input.count {
            it.isNotBlank() && it.split(",").map { it2 -> it2.split('-').let { (a, b) -> a.toInt()..b.toInt() } }
                .let { (r1, r2) -> function(r1, r2) || function(r2, r1) }
        }
    }

    fun part1(input: List<String>): Int {
        return withRangesParsed(input, ::fullyIn)
    }

    fun part2(input: List<String>): Int {
        return withRangesParsed(input, ::overlap)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
