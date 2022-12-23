private class ElvenField(map: List<String>) {
    val elfAmount: Int
    val elfPositions: MutableList<Pair<Int, Int>> = mutableListOf()
    val positionsToElf: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    val directions = mutableListOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    init {
        var counter = 0
        map.withIndex().forEach { (x, line) ->
            line.withIndex().forEach { (y, char) ->
                if (char == '#') {
                    elfPositions.add(x to y)
                    positionsToElf[x to y] = counter
                    counter++
                }
            }
        }
        elfAmount = counter
    }

    fun makeTurn(): Boolean {
        val propositions: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        for ((ind, elf) in elfPositions.withIndex()) {
            var atLeastOne = false
            for (x in (-1..1)) {
                for (y in (-1..1)) {
                    if (x == 0 && y == 0) continue
                    if ((elf + (x to y)) in positionsToElf) {
                        atLeastOne = true
                    }
                }
            }
            if (!atLeastOne) continue
            for (dir in directions) {
                if ((-1..1).any { c ->
                        val pos = elf + if (dir.first == 0) (c to dir.second) else (dir.first to c)
                        pos in positionsToElf
                    }) continue
                val pos = elf + dir
                if (pos in propositions) {
                    propositions[pos] = -1
                } else {
                    propositions[pos] = ind
                }
                break
            }
        }
        var moved = false
        propositions.forEach {
            if (it.value != -1) {
                moved = true
                val oldPos = elfPositions[it.value]
                elfPositions[it.value] = it.key
                positionsToElf.remove(oldPos)
                positionsToElf[it.key] = it.value
            }
        }
        val dir = directions.removeAt(0)
        directions.add(dir)
        return moved
    }

    fun getFreeSpace(): Int {
        return (elfPositions.maxOf { (x, _) -> x } - elfPositions.minOf { (x, _) -> x } + 1) *
                (elfPositions.maxOf { (_, y) -> y } - elfPositions.minOf { (_, y) -> y } + 1) - elfAmount
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val field = ElvenField(input)
        repeat(10) {
            field.makeTurn()
        }
        return field.getFreeSpace()
    }

    fun part2(input: List<String>): Int {
        val field = ElvenField(input)
        var cnt = 1
        while (field.makeTurn()) cnt++
        return cnt
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 110)
    check(part2(testInput) == 20)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
