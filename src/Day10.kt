

fun main() {
    val wholeMap =
        readInput("10")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.digitToInt() }
            }.associate { (position, height) -> position to height }

    val zeroes = wholeMap.filter { (_, height) -> height == 0 }.keys
    val nines = wholeMap.filter { (_, height) -> height == 9 }.keys

    val part1 = zeroes.sumOf { start -> reachableNines(wholeMap, start, nines).count() }
    val part2 = zeroes.sumOf { start -> uniquePathsToNines(wholeMap, start, nines) }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 760)
    assert(part2 == 1764)
}

private fun reachableNines(
    heightMap: Map<Pair<Int, Int>, Int>,
    current: Pair<Int, Int>,
    goals: Set<Pair<Int, Int>>,
    visited: Set<Pair<Int, Int>> = setOf(current),
): Set<Pair<Int, Int>> {
    if (current in goals) {
        return setOf(current)
    }
    return listOf(Up, Down, Left, Right)
        .map { direction -> current + direction.direction }
        .filter { it !in visited }
        .filter { it in heightMap }
        .filter { (heightMap[current]?.let { it1 -> heightMap[it]?.minus(it1) } ?: 0) == 1 }
        .flatMap { reachableNines(heightMap, it, goals, visited + it) }
        .toSet()
}

private fun uniquePathsToNines(
    heightMap: Map<Pair<Int, Int>, Int>,
    current: Pair<Int, Int>,
    goals: Set<Pair<Int, Int>>,
    visited: Set<Pair<Int, Int>> = setOf(current),
): Int {
    if (current in goals) {
        return 1
    }
    return listOf(Up, Down, Left, Right)
        .map { direction -> current + direction.direction }
        .filter { it !in visited }
        .filter { it in heightMap }
        .filter { (heightMap[current]?.let { it1 -> heightMap[it]?.minus(it1) } ?: 0) == 1 }
        .map { uniquePathsToNines(heightMap, it, goals, visited + it) }
        .sum()
}
