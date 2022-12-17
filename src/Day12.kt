import java.util.*

private val dirs: List<Pair<Int, Int>> = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

fun main() {
    fun bfs(graphHeights: List<List<Int>>, start: List<Pair<Int, Int>>, finish: Pair<Int, Int>): Int {
        val n = graphHeights.size
        val m = graphHeights[0].size
        val q: Queue<Pair<Int, Pair<Int, Int>>> = LinkedList()
        val used = MutableList(n) { MutableList(m) { false } }
        q.addAll(start.map { Pair(0, it) })
        while (q.isNotEmpty()) {
            val (dist, xy) = q.remove()
            if (xy == finish) {
                return dist
            }
            if (used[xy]) continue
            used[xy] = true
            for (dir in dirs) {
                val nxy = xy + dir
                if (nxy.first !in 0 until n || nxy.second !in 0 until m || used[nxy] || graphHeights[nxy] > graphHeights[xy] + 1) continue
                q.add(Pair(dist + 1, nxy))
            }
        }
        return -1
    }

    fun findAllCharIn2D(input: List<String>, c: Char): List<Pair<Int, Int>> {
        val result: MutableList<Pair<Int, Int>> = mutableListOf()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (c == input[i][j]) {
                    result.add(i to j)
                }
            }
        }
        return result
    }

    fun toHeightMap(input: List<String>) = input.map {
        it.map { it2 ->
            when (it2) {
                'S' -> 0
                'E' -> 'z' - 'a'
                else -> it2 - 'a'
            }
        }
    }

    fun part1(input: List<String>): Int {
        return bfs(toHeightMap(input), findAllCharIn2D(input, 'S'), findAllCharIn2D(input, 'E')[0])
    }

    fun part2(input: List<String>): Int {
        return bfs(
            toHeightMap(input),
            findAllCharIn2D(input, 'a') + findAllCharIn2D(input, 'S'),
            findAllCharIn2D(input, 'E')[0]
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
