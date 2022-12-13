import kotlin.math.min

class WeirdLists(private val intValue: Int? = null) : Comparable<WeirdLists> {
    constructor(list: List<WeirdLists>): this(null) {
        container = list as MutableList<WeirdLists>
    }
    var container: MutableList<WeirdLists> = mutableListOf()
    override fun compareTo(other: WeirdLists): Int {
        return when {
            intValue != null && other.intValue != null -> intValue.compareTo(other.intValue)
            intValue == null && other.intValue == null -> {
                var i = 0
                while (i < min(container.size, other.container.size)) {
                    val compRes = container[i].compareTo(other.container[i])
                    if (compRes != 0) {
                        return compRes
                    }
                    i++
                }
                return when {
                    container.size == other.container.size -> 0
                    i == container.size -> -1
                    else -> 1
                }
            }
            intValue != null -> WeirdLists(listOf(this)).compareTo(other)
            else -> this.compareTo(WeirdLists(listOf(other)))
        }
    }
}

fun main() {
    fun parseThing(line: String, startPos: Int): Pair<WeirdLists, Int> {
        if (line[startPos].isDigit()) {
            val lastPos = line.substring(startPos).indexOfFirst { !it.isDigit() } + startPos
            return Pair(WeirdLists(line.substring(startPos, lastPos).toInt()), lastPos)
        }
        if (line[startPos] == '[') {
            if (line[startPos + 1] == ']') {
                return Pair(WeirdLists(), startPos + 2)
            }
            val root = WeirdLists()
            val (curVal, nextPos) = parseThing(line, startPos + 1)
            root.container.add(curVal)
            var curPos = nextPos
            while (line[curPos] == ',') {
                val (newVal, newPos) = parseThing(line, curPos + 1)
                root.container.add(newVal)
                curPos = newPos
            }
            assert(line[curPos] == ']')
            return Pair(root, curPos + 1)
        }
        error("Weird start")
    }

    fun part1(input: List<String>): Int {
        val groupedLines = input.joinToString("/n").split("/n/n").map { it.split("/n") }
            .map { (firstLine, secondLine) -> Pair(parseThing(firstLine, 0).first, parseThing(secondLine, 0).first) }
        return groupedLines.withIndex().filter { (_, pair) -> pair.first < pair.second }.sumOf { it.index + 1}
    }

    fun part2(input: List<String>): Int {
        val flattenedLines = input.joinToString("/n").split("/n/n").map { it.split("/n") }.flatten()
            .map { line -> parseThing(line, 0).first}.toMutableList()
        val withTwo = WeirdLists(listOf(WeirdLists(listOf(WeirdLists(2)))))
        val withSix = WeirdLists(listOf(WeirdLists(listOf(WeirdLists(6)))))
        flattenedLines.add(withTwo)
        flattenedLines.add(withSix)
        flattenedLines.sort()
        return (flattenedLines.indexOf(withTwo) + 1) * (flattenedLines.indexOf(withSix) + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)
//    println(part1(testInput))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
