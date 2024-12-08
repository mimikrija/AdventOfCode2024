fun main() {
    val wholeMap =
        readInput("08")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.toString() }
            }.associate { (position, letter) -> position to letter }

    val available = wholeMap.map { it.key}.toSet()
    val antennaTypes = wholeMap.filter { (k, v) -> v != "."}.map { (k, v) -> v }.toSet()

    val antennas = antennaTypes.map {
        val positions = wholeMap.filter { (k, v) -> v == it }.map { bla -> bla.key }
        it to positions}
        .associate { it.first to it.second}

    val part1 = antennas
        .flatMap {
        (_, positions) -> generateAntiNodes(positions, available = available) }.toSet().size

    val part2 = antennas
        .flatMap {
                (_, positions) -> generateAntiNodesPt2(positions, available = available) }.toSet().size

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 289)
    assert(part2 == 1030)

}

private fun generateAntiNodes(
    antennas: List<Pair<Int, Int>>,
    available: Set<Pair<Int, Int>>) = getCombinations(antennas).flatMap {
            (a, b) ->
            val dist = b - a
            listOf(b + dist, a - dist)
        }.toSet() intersect available

private fun generateAntiNodesPt2(
    antennas: List<Pair<Int, Int>>,
    available: Set<Pair<Int, Int>>) = getCombinations(antennas).flatMap {
        (a, b) ->
    val dist = b - a
    (0..100).flatMap { listOf(a + it*dist, a - it*dist) }
    }.toSet() intersect available
