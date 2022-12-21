private sealed interface MathMonkey {
    fun initLeftRight(information: Map<String, MathMonkey>)
    fun isConcrete(): Boolean
    fun eval(): Long
}

private class JustYell(val name: String, val value: Long) : MathMonkey {
    override fun initLeftRight(information: Map<String, MathMonkey>) {}
    override fun isConcrete(): Boolean = true
    override fun eval(): Long = value
}

private class Human(val name: String, val value: Long) : MathMonkey {
    override fun initLeftRight(information: Map<String, MathMonkey>) {}
    override fun isConcrete(): Boolean = false
    override fun eval(): Long = value
}

private class WithChildren(
    val name: String,
    val leftName: String,
    val rightName: String,
    val f: ((Long, Long) -> Long),
    val revFL: ((Long, Long) -> Long),
    val revFR: ((Long, Long) -> Long)
) : MathMonkey {
    var left: MathMonkey? = null
    var right: MathMonkey? = null
    var concrete: Boolean? = null
    var storedValue: Long? = null
    override fun initLeftRight(information: Map<String, MathMonkey>) {
        left = information[leftName]
        right = information[rightName]
    }

    fun getLeftOrFail(): MathMonkey = left ?: error("Child not initialized")
    fun getRightOrFail(): MathMonkey = right ?: error("Child not initialized")

    override fun isConcrete(): Boolean {
        if (concrete != null) return concrete!!
        concrete = getLeftOrFail().isConcrete() && getRightOrFail().isConcrete()
        return concrete!!
    }

    override fun eval(): Long {
        return storedValue ?: if (isConcrete()) {
            storedValue = f(getLeftOrFail().eval(), getRightOrFail().eval())
            storedValue!!
        } else {
            f(getLeftOrFail().eval(), getRightOrFail().eval())
        }
    }

    fun whatNeedFromNotConcreteToEqual(needed: Long): Pair<MathMonkey, Long> {
        assert(!isConcrete())
        if (getLeftOrFail().isConcrete()) {
            assert(!getRightOrFail().isConcrete())
            return getRightOrFail() to revFR(needed, getLeftOrFail().eval())
        }
        assert(getRightOrFail().isConcrete())
        return getLeftOrFail() to revFL(needed, getRightOrFail().eval())
    }
}

fun main() {
    val mathMap: Map<Char, Pair<MathOp, Pair<MathOp, MathOp>>> = mapOf(
        '/' to Pair(
            { x, y -> x / y },
            Pair({ result: Long, known: Long -> result * known }, { result: Long, known: Long -> known / result })
        ),
        '*' to Pair(
            { x, y -> x * y },
            Pair({ result: Long, known: Long -> result / known }, { result: Long, known: Long -> result / known })
        ),
        '+' to Pair(
            { x, y -> x + y },
            Pair({ result: Long, known: Long -> result - known }, { result: Long, known: Long -> result - known })
        ),
        '-' to Pair(
            { x, y -> x - y },
            Pair({ result: Long, known: Long -> result + known }, { result: Long, known: Long -> known - result })
        ),
    )

    fun getMonkeyMap(input: List<String>): Map<String, MathMonkey> {
        val monkeyMap: MutableMap<String, MathMonkey> = mutableMapOf()
        input.map { it.split(':') }.forEach { (name, def) ->
            val parts = def.trim().split(' ')
            monkeyMap[name] = if (parts.size == 1) {
                if (name == "humn") {
                    Human(name, parts[0].toLong())
                } else {
                    JustYell(name, parts[0].toLong())
                }
            } else {
                val (f, pairRevFLR) = mathMap[parts[1][0]]!!
                WithChildren(name, parts[0], parts[2], f, pairRevFLR.first, pairRevFLR.second)
            }
        }
        monkeyMap.forEach {
            it.value.initLeftRight(monkeyMap)
        }
        return monkeyMap
    }

    fun part1(input: List<String>): Long {
        val monkeyMap = getMonkeyMap(input)
        return monkeyMap["root"]?.eval() ?: error("No root?")
    }

    fun part2(input: List<String>): Long {
        val monkeyMap = getMonkeyMap(input)
        val root: WithChildren = (monkeyMap["root"] ?: error("No root?")) as WithChildren
        val left = root.getLeftOrFail()
        val right = root.getRightOrFail()
        var currentValueNeeded = if (left.isConcrete()) {
            left.eval()
        } else {
            assert(right.isConcrete())
            right.eval()
        }
        var currentNode = if (left.isConcrete()) right else left
        while (currentNode !is Human) {
            require(currentNode is WithChildren)
            val nodeAndValue = currentNode.whatNeedFromNotConcreteToEqual(currentValueNeeded)
            currentNode = nodeAndValue.first
            currentValueNeeded = nodeAndValue.second
        }
        return currentValueNeeded
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}

private typealias MathOp = (Long, Long) -> Long
