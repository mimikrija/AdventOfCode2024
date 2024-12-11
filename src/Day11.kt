

fun main() {
    val allStones =
        readInput("11")
            .first()
            .toString()
            .split(" ")
            .map { it.toLong() }

    val first25 =
        handleStones(counts = allStones.associateWith { 1.toLong() }, 25)

    val part1 = first25.values.sum()
    println("Part 1 solution is $part1")

    val part2 =
        handleStones(counts = first25, 50).values.sum()

    println("Part 2 solution is $part2")

    assert(part1 == 203953.toLong())
    assert(part2 == 242090118578155)
}

private fun splitInHalf(number: Long): List<Long> {
    val stringNum = number.toString()
    val length = stringNum.length
    if (length % 2 == 1) return listOf(number)
    val left = stringNum.slice(0..<length / 2)
    val right = stringNum.slice(length / 2..<length)

    return listOf(left, right).map { it.toLong() }
}

private fun Long.applyRules(): List<Long> {
    if (this == 0.toLong()) return listOf(1)

    val split = splitInHalf(this)
    if (split.size > 1) return split

    return listOf(2024 * this)
}

private fun handleStones(
    counts: Map<Long, Long>,
    blinks: Int,
    blink: Int = 0,
): Map<Long, Long> {
    if (blink == blinks) return counts

    val next = mutableMapOf<Long, Long>()
    counts.forEach { (stone, quantity) ->
        stone.applyRules().forEach {
            next[it] = next.getOrDefault(it, 0L) + quantity
        }
    }

    return handleStones(
        next.toMap(),
        blinks,
        blink + 1,
    )
}

private fun merge(
    a: Map<Long, Long>,
    b: Map<Long, Long>,
): Map<Long, Long> = (a.keys + b.keys).associateWith { key -> (a[key] ?: 0) + (b[key] ?: 0) }
