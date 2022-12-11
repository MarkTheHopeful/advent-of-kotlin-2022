class Monkey(val items: MutableList<Long>, val operation: (Long, Long?) -> Long, val testAndThrow: (Long) -> Int, val modulo: Long) {

}

fun main() {
    fun constructOperation(opToken: Char, value: Long?, divideBy: Long = 3): (Long, Long?) -> Long {
        return if (opToken == '*') {
            it: Long, modulo: Long? -> ((it * (value ?: it)) / divideBy) % (modulo ?: Long.MAX_VALUE)
        } else {
            it: Long, modulo: Long? -> ((it + (value ?: it)) / divideBy) % (modulo ?: Long.MAX_VALUE)
        }
    }

    fun constructTestAndThrow(testValue: Long, ifTrue: Int, ifFalse: Int): (Long) -> Int {
        return {it: Long -> if (it % testValue == 0L) ifTrue else ifFalse}
    }

    fun parseMonkey(monkeyDesc: List<String>, shouldDivide: Boolean = true): Monkey {
        val items = monkeyDesc[1].split(" ").drop(4).map{it.removePrefix(",").removeSuffix(",").toInt()}
        val operationTokens = monkeyDesc[2].split(" ").drop(6).also {println(it)}
        val operation = constructOperation(operationTokens[0][0], operationTokens[1].toLongOrNull(), if (shouldDivide) 3 else 1)
        val testValue = monkeyDesc[3].split(" ").takeLast(1)[0].toLong()
        val ifTrue = monkeyDesc[4].split(" ").takeLast(1)[0].toInt()
        val ifFalse = monkeyDesc[5].split(" ").takeLast(1)[0].toInt()
        val testAndThrow = constructTestAndThrow(testValue, ifTrue, ifFalse)
        return Monkey(items as MutableList<Long>, operation, testAndThrow, testValue)
    }

    fun part1(input: List<String>): Int {
        val monks = input.chunked(7).map(::parseMonkey)
        val counters = MutableList(monks.size) {0}
        repeat(20) {
            monks.withIndex().forEach {(i, monkey) ->
                monkey.items.forEach {
                    counters[i]++
                    val newWorry = monkey.operation(it, null)
                    monks[monkey.testAndThrow(newWorry)].items.add(newWorry)
                }
                monkey.items.clear()
            }
        }
        return counters.sorted().takeLast(2).fold(1, Int::times)
    }
    fun part2(input: List<String>): Long {
        val monks = input.chunked(7).map{parseMonkey(it, false)}
        val counters = MutableList(monks.size) {0L}
        val modulo: Long = monks.map {it.modulo}.fold(1, Long::times)
        repeat(10000) {
            monks.withIndex().forEach {(i, monkey) ->
                monkey.items.forEach {
                    counters[i]++
                    val newWorry = monkey.operation(it, modulo)
                    monks[monkey.testAndThrow(newWorry)].items.add(newWorry)
                }
                monkey.items.clear()
            }
        }
        return counters.sorted().takeLast(2).fold(1, Long::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
