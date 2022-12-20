import kotlin.math.sign

private fun <T: Number> MutableList<IndexedValue<T>>.mixOn(number: IndexedValue<T>) {
    var pos = this.indexOf(number)  // O(n)
    val n = this.size
    var shift = (number.value.toLong() % (n - 1)).toInt()
    while (shift != 0) {    // O(n). Awful!
        val newPos = ((pos + shift.sign) % n + n) % n
        val tmp = this[newPos]
        this[newPos] = this[pos]
        this[pos] = tmp
        pos = newPos
        shift -= shift.sign
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val order = input.map { it.toInt() }
        val data = order.withIndex().toMutableList()
        for (num in order.withIndex()) {
            data.mixOn(num)
        }
        val zero = data.indexOf(IndexedValue(order.indexOf(0), 0))
        return data[(zero + 1000) % data.size].value + data[(zero + 2000) % data.size].value + data[(zero + 3000) % data.size].value
    }

    fun part2(input: List<String>): Long {
        val order = input.map { it.toLong() * 811589153 }
        val data = order.withIndex().toMutableList()
        repeat(10) {
            for (num in order.withIndex()) {
                data.mixOn(num)
            }
        }
        val zero = data.indexOf(IndexedValue(order.indexOf(0), 0))
        return data[(zero + 1000) % data.size].value + data[(zero + 2000) % data.size].value + data[(zero + 3000) % data.size].value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 1623178306L)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
