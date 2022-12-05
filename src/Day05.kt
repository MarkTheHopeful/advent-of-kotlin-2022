fun main() {
    fun readCrates(input: List<String>): Pair<MutableList<MutableList<Char>>, Int> {
        val crates: MutableList<MutableList<Char>> = mutableListOf()
        var blankLineInd = input.size
        for ((index, line) in input.withIndex()) {
            if (line.isBlank()) {
                blankLineInd = index
                break
            }
            val blocks = (line.length + 1) / 4
            while (blocks > crates.size) {
                crates.add(mutableListOf())
            }
            for (i in 1..line.length step 4) {
                if (line[i].isLetter()) {
                    crates[i / 4].add(line[i])
                }
            }
        }
        for (i in crates.indices) {
            crates[i].reverse()
        }
        return Pair(crates, blankLineInd)
    }

    fun part1(input: List<String>): String {
        val (crates, blankLineInd) = readCrates(input)

        for (command in input.subList(blankLineInd + 1, input.size)) {
            val (amount, from, to) = command.split(" ").filterIndexed { ind, _ -> ind % 2 == 1 }.map { x -> x.toInt() }
            repeat (amount) {
                crates[to - 1].add(crates[from - 1].removeLast())
            }
        }

        return crates.map {x -> x.lastOrNull() ?: " "}.joinToString("", "", "")
    }

    fun part2(input: List<String>): String {
        val (crates, blankLineInd) = readCrates(input)

        for (command in input.subList(blankLineInd + 1, input.size)) {
            val (amount, from, to) = command.split(" ").filterIndexed { ind, _ -> ind % 2 == 1 }.map { x -> x.toInt() }
            val tail = crates[from - 1].takeLast(amount)
            repeat (amount) {
                crates[from - 1].removeLast()
            }
            crates[to - 1].addAll(tail)
        }

        return crates.map {x -> x.lastOrNull() ?: " "}.joinToString("", "", "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
