import java.util.*
import kotlin.math.abs

private typealias Point = List<Int>

fun main() {
    fun areConnected(a: Point, b: Point): Boolean {
        return a.withIndex().count { it.value != b[it.index] } == 1 && a.withIndex()
            .count { abs(it.value - b[it.index]) == 1 } == 1
    }

    fun countOutsides(cubes: Set<Point>) = 6 * cubes.size - cubes.sumOf {
        cubes.count { it2 ->
            areConnected(it, it2)
        }
    }

    fun parseCubes(input: List<String>) = input.map { it.split(',').map { it2 -> it2.toInt() } }.toSet()

    fun part1(input: List<String>): Int {
        val cubes = parseCubes(input)
        return countOutsides(cubes)
    }

    fun walkBfs(
        cubes: Set<Point>,
        pointStart: Point,
        used: MutableMap<Point, Int>,
        bounds: Point,
        alreadyExternal: MutableSet<Point>,
        iter: Int
    ): Boolean {
        val q: Queue<Point> = LinkedList()
        q.add(pointStart)
        used[pointStart] = iter
        var reachedTheEnd = false
        while (q.isNotEmpty()) {
            val point = q.remove()
            if (point.any { it == 0 } || point in alreadyExternal) {
                reachedTheEnd = true
            }
            for (i in point.indices) {
                for (j in -1..1 step 2) {
                    val newPoint = List(point.size) { point[it] + (if (it == i) j else 0) }
                    if (newPoint in used || newPoint in cubes) continue
                    if (newPoint.withIndex().any { it.value < 0 || it.value > bounds[it.index] }) continue
                    q.add(newPoint)
                    used[newPoint] = iter
                }
            }
        }
        return reachedTheEnd
    }

    fun getAllInternalPoints(cubes: Set<Point>): Set<Point> {
        // 0,0,0 is definitely external
        val bounds = List(cubes.first().size) { cubes.maxOf { it2 -> it2[it] + 1 } }
        val foundInternal = mutableSetOf<Point>()
        val foundExternal = mutableSetOf<Point>()
        val used = mutableMapOf<Point, Int>()
        var iter = 0
        for (x in 0..bounds[0]) {
            for (y in 0..bounds[1]) {
                for (z in 0..bounds[2]) {
                    val point = listOf(x, y, z)
                    if (point in used || point in cubes) continue
                    if (walkBfs(cubes, point, used, bounds, foundExternal, iter)) {
                        foundExternal.addAll(used.filter { it.value == iter }.keys)
                    } else {
                        foundInternal.addAll(used.filter { it.value == iter }.keys)
                    }
                    iter++
                }
            }
        }
        return foundInternal
    }

    fun part2(input: List<String>): Int {
        val cubes = parseCubes(input).toMutableSet()
        cubes.addAll(getAllInternalPoints(cubes))
        return countOutsides(cubes)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
