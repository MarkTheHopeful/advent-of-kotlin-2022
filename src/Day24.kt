private val directions = mutableListOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
private val charToDir = buildMap(4) { (0..3).forEach { put("^v<>"[it], directions[it]) } }

private data class Blizzard(val dir: Pair<Int, Int>, var pos: Pair<Int, Int>)

private class BlizzardField(map: List<String>) {
    val blizzards: MutableList<Blizzard> = mutableListOf()
    val occupiedPositions: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    val n: Int
    val m: Int

    init {
        n = map.size - 2
        m = map[0].length - 2
        map.drop(1).take(n).withIndex().onEach { (vx, line) ->
            line.drop(1).take(m).withIndex().onEach { (vy, c) ->
                if (c != '.') {
                    blizzards.add(Blizzard(charToDir[c] ?: error("Wrong char"), vx to vy))
                    occupiedPositions[vx to vy] = occupiedPositions.getOrDefault(vx to vy, 0) + 1 // With default
                }
            }
        }
    }

    fun iterateBlizzards() {
        blizzards.withIndex().forEach { (ind, value) ->
            val (dir, pos) = value
            occupiedPositions[pos] = (occupiedPositions[pos] ?: error("There must be at least one. I feel it.")) - 1
            var (nx, ny) = pos + dir
            nx = (nx + n) % n
            ny = (ny + m) % m
            blizzards[ind].pos = nx to ny
            occupiedPositions[nx to ny] = occupiedPositions.getOrDefault(nx to ny, 0) + 1
        }
    }

    fun isFree(pos: Pair<Int, Int>): Boolean {
        return (pos.first == -1 && pos.second == 0) || (pos.first == n && pos.second == m - 1) ||
                !(pos.first < 0 || pos.first >= n || pos.second < 0 || pos.second >= m ||
                        occupiedPositions.getOrDefault(pos, 0) > 0)
    }

    fun runBfs(start: Pair<Int, Int> = -1 to 0, final: Pair<Int, Int> = n to m - 1): Int {
        val q: MutableList<MutableSet<Pair<Int, Int>>> = mutableListOf(mutableSetOf(start), mutableSetOf())
        var iter = 0
        while (true) {
            iterateBlizzards()
            q[iter % 2].forEach {
                if (it == final) {
                    return iter
                }
                if (isFree(it)) {
                    q[(iter + 1) % 2].add(it)
                }
                for (dir in directions) {   // Maybe somehow build set right here
                    if (isFree(it + dir)) {
                        q[(iter + 1) % 2].add(it + dir)
                    }
                }
            }
            q[iter % 2].clear()
            if (q[0].isEmpty() && q[1].isEmpty()) {
                error("There is no way through this blizzard. Supplies and energy are running out. Goodbye.")
            }
            iter++
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return BlizzardField(input).runBfs()
    }

    fun part2(input: List<String>): Int {
        val field = BlizzardField(input)
        val start = -1 to 0
        val final = field.n to field.m - 1
        // And now we should add 1. Twice.
        return field.runBfs(start, final) + 1 + field.runBfs(final, start) + 1 + field.runBfs(start, final)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day24_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 54)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
