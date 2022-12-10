fun main() {
    fun toValues(input: List<String>): List<Int> {
        val current = mutableListOf(1)
        input.forEach {
            current.add(current.last())
            if (it.startsWith("addx")) {
                current.add(current.last() + it.split(" ")[1].toInt())
            }
        }
        return current
    }

    fun part1(input: List<String>): Int {
        return toValues(input).withIndex().filter { (it.index + 1) % 40 == 20 && it.index <= 220 }
            .sumOf { (it.index + 1) * it.value }
    }

    fun whatToPrint(cycleNum: Int, current: Int): Char {
        return if ((cycleNum - 1) % 40 in current - 1..current + 1) {
            '#'
        } else {
            '.'
        }
    }

    fun part2(input: List<String>): String {
        return toValues(input).dropLast(1).asSequence().withIndex().map {
            whatToPrint(it.index + 1, it.value)
        }.chunked(40).map { it.joinToString("") }.joinToString("\n")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
                                 ##..##..##..##..##..##..##..##..##..##..
                                 ###...###...###...###...###...###...###.
                                 ####....####....####....####....####....
                                 #####.....#####.....#####.....#####.....
                                 ######......######......######......####
                                 #######.......#######.......#######.....""".trimIndent()
    )

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
