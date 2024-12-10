fun main() {
    val wholeMap =
        readInput("10")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.digitToInt() }
            }.associate { (position, height) -> position to height }

    val zeroes = wholeMap.filter { (_, height) -> height == 0 }.keys
    val nines = wholeMap.filter { (_, height) -> height == 9 }.keys

    val bothParts =
        zeroes
            .map { start ->
                nines.map { nine ->
                    reachableNines(wholeMap, start, nine)
                }
            }.map { result -> result.count { it != 0 } to result.sum() }

    val part1 = bothParts.sumOf { it.first }
    val part2 = bothParts.sumOf { it.second }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 760)
    assert(part2 == 1764)
}

private fun reachableNines(
    heightMap: Map<Pair<Int, Int>, Int>,
    current: Pair<Int, Int>,
    goal: Pair<Int, Int>,
    visited: Set<Pair<Int, Int>> = setOf(current),
): Int {
    if (current == goal) {
        return 1
    }
    return listOf(Up, Down, Left, Right)
        .asSequence()
        .map { direction -> current + direction.direction }
        .filter { it !in visited }
        .filter { it in heightMap }
        .filter { (heightMap[current]?.let { it1 -> heightMap[it]?.minus(it1) } ?: 0) == 1 }
        .sumOf { reachableNines(heightMap, it, goal, visited + it) }
}
