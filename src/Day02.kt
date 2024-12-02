import kotlin.math.abs

fun main() {
    val rawInput = readInput("02")

    val levels =
        rawInput
            .map { text ->
                text
                    .split(" ")
                    .map { it.toInt() }
            }

    val part1 = levels.count { safe(it) }
    val part2 = levels.count { safeDampener(it) }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 524)
    assert(part2 == 569)
}

private fun safe(nums: List<Int>) =
    if (nums.sorted() == nums || nums.sortedDescending() == nums) {
        nums
            .zipWithNext()
            .all { (abs(it.first - it.second) <= 3) and (abs(it.first - it.second) >= 1) }
    } else {
        false
    }

private fun safeDampener(nums: List<Int>): Boolean {
    for (i in nums.indices) {
        if (safe(nums.withoutItemAt(i))) return true
    }
    return false
}

fun <L> Iterable<L>.withoutItemAt(index: Int) = filterIndexed { i, _ -> i != index }
