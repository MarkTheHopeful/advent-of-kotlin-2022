fun main() {
    fun readCrates(input: List<String>): List<MutableList<Char>> {
        val crates: List<MutableList<Char>> = List((input.last().length + 2) / 4) { mutableListOf() }
        for (line in input) {
            line.chunked(4).forEachIndexed { index, part -> if (part[1].isLetter()) crates[index].add(part[1]) }
        }
        for (i in crates.indices) {
            crates[i].reverse()
        }
        return crates
    }

    fun workOnCrates(input: List<String>, moveOperation: (List<MutableList<Char>>, Int, Int, Int) -> Unit): String {
        val (cratesLines, commandLines) = input.joinToString("/n").split("/n/n").map { it.split("/n") }
        val crates = readCrates(cratesLines)

        for (command in commandLines) {
            val (amount, from, to) = command.split(" ").mapNotNull(String::toIntOrNull)
            moveOperation(crates, amount, from - 1, to - 1)
        }

        return crates.map { x -> x.lastOrNull() ?: " " }.joinToString("", "", "")
    }

    fun part1(input: List<String>): String {
        return workOnCrates(input) { crates: List<MutableList<Char>>, amount, from, to ->
            repeat(amount) {
                crates[to].add(crates[from].removeLast())
            }
        }
    }

    fun part2(input: List<String>): String {
        return workOnCrates(input) { crates: List<MutableList<Char>>, amount, from, to ->
            val tail = crates[from].takeLast(amount)
            repeat(amount) {
                crates[from].removeLast()
            }
            crates[to].addAll(tail)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
