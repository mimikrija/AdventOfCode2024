import kotlin.math.abs

fun main() {

    val rawInput = readInput("01")
    val first = rawInput.map { it.split("  ").first().toInt() }.sorted()
    val second = rawInput.map{it.split("   ").last().toInt()}.sorted()
    val part1 = (first zip second).sumOf { abs(it.second - it.first) }
    val part2 = first.sumOf { inn -> inn * second.count { it -> it == inn } }
    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 765748)
    assert(part2 == 27732508)
}
