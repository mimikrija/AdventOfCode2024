fun main() {
    val wholeMap =
        readInput("10")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.digitToInt() }
            }.associate { (position, height) -> position to height }

    val zeroes = wholeMap.filter { (_, height) -> height == 0 }.keys

    val part1 = zeroes.sumOf { start -> reachableNines(wholeMap, start).distinct().count() }
    val part2 = zeroes.sumOf { start -> reachableNines(wholeMap, start).count() }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 760)
    assert(part2 == 1764)
}

private fun reachableNines(
    heightMap: Map<Pair<Int, Int>, Int>,
    current: Pair<Int, Int>,
    height: Int = 0,
): List<Pair<Int, Int>> {
    if (height == 9) {
        return listOf(current)
    }

    return listOf(Up, Down, Left, Right)
        .map { direction -> current + direction.direction }
        .filter { it in heightMap }
        .filter { heightMap[it] == heightMap[current]?.plus(1) }
        .flatMap { reachableNines(heightMap, it, height + 1) }
}
