import kotlin.math.abs

fun main() {
    val rawInput = readInput("01")

    val leftAndRight =
        rawInput
            .map { text ->
                text
                    .split("   ")
                    .map { it.toInt() }
            }.map { it.first() to it.last() }

    val part1 =
        (leftAndRight.map { it.first }.sorted() zip leftAndRight.map { it.second }.sorted())
            .sumOf { abs(it.first - it.second) }

    val counts = leftAndRight.map { it.second }.groupingBy { it }.eachCount()
    val part2 = leftAndRight.map { it.first }.sumOf { it * (counts[it] ?: 0) }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 765748)
    assert(part2 == 27732508)
}
