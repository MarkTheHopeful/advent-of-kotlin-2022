fun main() {
    fun priority(c: Char): Int = if (c in 'A'..'Z') c - 'A' + 27 else c - 'a' + 1

    fun countRepetitions(line: String): Int {
        val n = line.length
        val second = line.subSequence(n / 2, n).toSet()
        return line.subSequence(0, n / 2).toSet().filter { it in second }.sumOf { priority(it) }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { countRepetitions(it) }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { it.map { it2 -> it2.toSet() }.reduce(Set<Char>::intersect).sumOf { c -> priority(c) } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
