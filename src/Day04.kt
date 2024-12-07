fun main() {
    val letterMap =
        readInput("04")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.toString() }
            }.associate { (position, letter) -> position to letter }

    val part1 = letterMap.countXMASInAllDirections()
    val part2 = letterMap.countBigXMAS()

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 2543)
    assert(part2 == 1930)
}

private fun Map<Pair<Int, Int>, String>.countXMASInAllDirections() =
    listOf((1 to 0), (-1 to 0), (0 to 1), (0 to -1), (1 to 1), (-1 to 1), (1 to -1), (-1 to -1))
        .sumOf { direction ->
            this
                .count { (position, _) ->
                    (0..3).map { this[position + it * direction] }.joinToString("") == "XMAS"
                }
        }

private fun Map<Pair<Int, Int>, String>.countBigXMAS(): Int {
    val mainDiagonal = listOf(1 to 1, -1 to -1)
    val otherDiagonal = listOf(-1 to 1, 1 to -1)
    return this
        .filter { (_, letter) -> letter == "A" }
        .filter { (position, _) -> mainDiagonal.map { diagonal -> this[position.plus(diagonal)] }.areMS() }
        .count { (position, _) -> otherDiagonal.map { diagonal -> this[position.plus(diagonal)] }.areMS() }
}

private fun List<String?>.areMS() = this.containsAll(listOf("S", "M"))
