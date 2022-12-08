fun main() {
    fun toRightFrom(input: List<String>, i: Int, j: Int) = (j + 1 until input[0].length).map { input[i][it] }
    fun toLeftFrom(input: List<String>, i: Int, j: Int) = (j - 1 downTo 0).map { input[i][it] }
    fun toDownFrom(input: List<String>, i: Int, j: Int) = (i + 1 until input.size).map { input[it][j] }
    fun toUpFrom(input: List<String>, i: Int, j: Int) = (i - 1 downTo 0).map { input[it][j] }

    fun part1(input: List<String>): Int {
        return input.withIndex().sumOf { (i, line) ->
            line.withIndex().count { (j, char) ->
                toRightFrom(input, i, j).all { it < char } || toLeftFrom(input, i, j).all { it < char } ||
                        toDownFrom(input, i, j).all { it < char } || toUpFrom(input, i, j).all { it < char }
            }
        }
    }

    fun <E> lenOfSuccessfulPrefix(list: List<E>, predicate: (E) -> Boolean): Int {
        return (list.indexOfFirst {!predicate(it)}.takeIf {it >= 0}?.inc() ?: list.size)
    }

    fun part2(input: List<String>): Int {
        return input.withIndex().maxOf { (i, line) ->
            line.withIndex().maxOf { (j, char) ->
                lenOfSuccessfulPrefix(toRightFrom(input, i, j)) { it < char } *
                        lenOfSuccessfulPrefix(toLeftFrom(input, i, j)) { it < char } *
                        lenOfSuccessfulPrefix(toDownFrom(input, i, j)) { it < char } *
                        lenOfSuccessfulPrefix(toUpFrom(input, i, j)) { it < char }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
