import kotlin.math.abs

fun main() {

    val rawInput = readInput("01")
    val leftAndRight = rawInput
        .map { text -> text.split("   ")
                .map { it.toInt() } }
        .associate { it.first() to it.last() }
    // this works under the assumption there are no duplicate values in first column,
    // which is true (but not true for the example input)
    // question is there a way to obtain the same result with non-unique first-column values?
    val part1 = leftAndRight.keys
        .sorted()
        .zip(leftAndRight.values.sorted())
        .sumOf { abs(it.first - it.second) }

    val part2 = leftAndRight.keys
        .sumOf { left -> left * leftAndRight.values.count{right -> right == left}  }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 765748)
    assert(part2 == 27732508)
}
