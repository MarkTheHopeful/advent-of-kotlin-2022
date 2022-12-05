fun main() {
    fun readCrates(input: List<String>): MutableList<MutableList<Char>> {
        val crates: MutableList<MutableList<Char>> = mutableListOf()
        for (line in input) {
            if (line.isBlank()) {
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
        return crates
    }

    fun workOnCrates(
        input: List<String>,
        moveOperation: (MutableList<MutableList<Char>>, Int, Int, Int) -> Unit
    ): String {
        val crates = readCrates(input)

        for (command in input.subList(input.indexOfFirst { s -> s.isBlank() } + 1, input.size)) {
            val (amount, from, to) = command.split(" ").filterIndexed { ind, _ -> ind % 2 == 1 }.map { x -> x.toInt() }
            moveOperation(crates, amount, from - 1, to - 1)
        }

        return crates.map { x -> x.lastOrNull() ?: " " }.joinToString("", "", "")
    }

    fun part1(input: List<String>): String {
        return workOnCrates(input) { crates: MutableList<MutableList<Char>>, amount, from, to ->
            repeat(amount) {
                crates[to].add(crates[from].removeLast())
            }
        }
    }

    fun part2(input: List<String>): String {
        return workOnCrates(input) { crates: MutableList<MutableList<Char>>, amount, from, to ->
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
