fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        var current = 1
        var cycleNum = 1
        input.forEach {
            cycleNum++
            if (it.startsWith("addx")) {
                if (cycleNum % 40 == 20 && cycleNum <= 220) {
                    answer += current * cycleNum
                }
                current += it.split(" ")[1].toInt()
                cycleNum++
            }
            if (cycleNum % 40 == 20 && cycleNum <= 220) {
                answer += current * cycleNum
            }
        }
        return answer
    }

    fun whatToPrint(cycleNum: Int, current: Int): Char {
        return if ((cycleNum - 1) % 40 in current - 1..current + 1) {
            '#'
        } else {
            '.'
        }
    }

    fun part2(input: List<String>): String {
        val screen = MutableList(6) { StringBuilder() }
        var current = 1
        var cycleNum = 0
        input.forEach {
            cycleNum++
            when {
                it.startsWith("addx") -> {
                    screen[cycleNum / 40].append(whatToPrint(cycleNum, current))
                    cycleNum++
                    screen[cycleNum / 40].append(whatToPrint(cycleNum, current))
                    current += it.split(" ")[1].toInt()
                }

                else -> {
                    screen[cycleNum / 40].append(whatToPrint(cycleNum, current))
                }
            }
        }
        return screen.joinToString("\n") { it.toString() }
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
