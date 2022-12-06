fun main() {
    fun firstUniqueSubstringLenK(input: String, len: Int): Int =
        input.windowed(len).indexOfFirst { it.toSet().size == len } + len

    fun part1(input: List<String>): Int {
        return firstUniqueSubstringLenK(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return firstUniqueSubstringLenK(input[0], 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 23)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
