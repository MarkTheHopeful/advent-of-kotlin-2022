import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

operator fun <T> List<List<T>>.get(pair: Pair<Int, Int>) = this[pair.first][pair.second]
operator fun <E> MutableList<MutableList<E>>.set(xy: Pair<Int, Int>, value: E) {
    this[xy.first][xy.second] = value
}

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first - other.first) to (this.second - other.second)
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (this.first + other.first) to (this.second + other.second)
}

operator fun Pair<Int, Int>.times(other: Int): Pair<Int, Int> {
    return (this.first * other) to (this.second * other)
}
