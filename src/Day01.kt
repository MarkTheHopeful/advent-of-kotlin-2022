import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(Pair(0, 0)) { maxAndSum, element ->
            if (element.isBlank()) Pair(max(maxAndSum.first, maxAndSum.second), 0)
            else Pair(maxAndSum.first, maxAndSum.second + element.toInt())
        }.first
    }

    fun part2(input: List<String>): Int {
        return input.fold(mutableListOf(0)) { list, element ->
            if (element.isBlank()) {
                list.add(0)
                list
            }
            else {
                list[list.size - 1] += element.toInt()
                list
            }
        }.sortedWith(reverseOrder()).take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
