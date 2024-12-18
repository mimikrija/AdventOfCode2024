

fun main() {
    val wholeMap =
        readInput("06")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.toString() }
            }.associate { (position, letter) -> position to letter }

    val obstructions =
        wholeMap
            .filter { (_, letter) -> letter == "#" }
            .map { it.key }
            .toSet()

    val availablePositions = wholeMap.map { it.key }.toSet() - obstructions

    val start = wholeMap.filter { (_, letter) -> letter == "^" }.map { it.key }.first()

    val part1 = countVisits(start, visited = setOf(start), available = availablePositions, obstructions = obstructions)

    println("Part 1 solution is $part1")

    assert(part1 == 4433)
}

private fun countVisits(
    current: Pair<Int, Int>,
    direction: Direction = Up,
    visited: Set<Pair<Int, Int>>,
    available: Set<Pair<Int, Int>>,
    obstructions: Set<Pair<Int, Int>>,
): Int {
    if (visited == available) return visited.size
    return when (val nextPosition = current + direction.direction) {
        in obstructions ->
            countVisits(current, direction.rotateClockwise(), visited, available, obstructions)
        in available ->
            countVisits(nextPosition, direction, visited union setOf(nextPosition), available, obstructions)
        else -> visited.size
    }
}
