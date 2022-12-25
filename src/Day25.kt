fun main() {
    val digits = mapOf('2' to 2L, '1' to 1L, '0' to 0L, '-' to -1L, '=' to -2L)
    val revDigits = mapOf(2L to '2', 1L to '1', 0L to '0', -1L to '-', -2L to '=')

    fun fromFiveBalanced(line: String): Long {
        var power = 1L
        var value = 0L
        line.reversed().forEach {
            value += power * (digits[it] ?: error("Wrong char"))
            power *= 5L
        }
        return value
    }

    fun toFiveBalanced(value: Long): String {
        val result = StringBuilder()
        var valueCopy = value
        while (valueCopy > 0L) {
            var rawRec = valueCopy % 5L
            if (rawRec >= 3L) {
                rawRec -= 5L
                valueCopy += 5
            }
            result.append(revDigits[rawRec])
            valueCopy /= 5L
        }
        return result.reversed().toString()
    }

    fun part1(input: List<String>): String {
        return toFiveBalanced(input.sumOf { fromFiveBalanced(it)} )
    }

    fun part2(input: List<String>): String {
        return "Merry Christmas!!"
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day25_test")
    check(part1(testInput) == "2=-1=0")
//    check(part2(testInput) == TODO())

    val input = readInput("Day25")
    println(part1(input))
    println(part2(input))
}
