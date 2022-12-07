import kotlin.math.min

class Node(private val parent: Node?, var size: Int, private val type: TYPE) {
    enum class TYPE {
        FILE, DIR
    }

    var children: MutableMap<String, Node> = mutableMapOf()

    fun getRealParent(): Node {
        return parent ?: this
    }

    fun recalculateSize(): Int {
        if (type == TYPE.DIR) {
            size = 0
        }
        for (child in children) {
            size += child.value.recalculateSize()
        }
        return size
    }

    fun sumOfNotGreaterThan(value: Int): Int {
        return (if (size <= value && type == TYPE.DIR) size else 0) + children.values.sumOf { node ->
            node.sumOfNotGreaterThan(
                value
            )
        }
    }

    fun smallestNotLessThan(value: Int): Int {
        return min(
            (if (size >= value && type == TYPE.DIR) size else Int.MAX_VALUE),
            children.values.minOfOrNull { node ->
                node.smallestNotLessThan(
                    value
                )
            } ?: Int.MAX_VALUE)
    }
}

fun main() {
    fun createFilesystem(input: List<String>): Node {
        val root = Node(null, 0, Node.TYPE.DIR)
        var currentNode = root
        input.forEach {
            when {
                it.startsWith("$ ls") -> {}
                it.startsWith("$ cd") -> {
                    currentNode = when (val newDir = it.split(" ")[2]) {
                        ".." -> currentNode.getRealParent()
                        "/" -> root
                        else -> currentNode.children[newDir] ?: error("Child not registered")
                    }
                }

                else -> {
                    val (typeOrSize, name) = it.split(" ")
                    if (name !in currentNode.children) {
                        currentNode.children[name] =
                            if (typeOrSize == "dir") Node(currentNode, 0, Node.TYPE.DIR) else
                                Node(currentNode, typeOrSize.toInt(), Node.TYPE.FILE)
                    }
                }
            }
        }
        root.recalculateSize()
        return root
    }

    fun part1(input: List<String>): Int {
        val root = createFilesystem(input)
        return root.sumOfNotGreaterThan(100000)
    }

    fun part2(input: List<String>): Int {
        val root = createFilesystem(input)
        return root.smallestNotLessThan(30000000 - (70000000 - root.size))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
