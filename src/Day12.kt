

fun main() {
    val gardenMap =
        readInput("12")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> column to row to letter.toString() }
            }.associate { (position, letter) -> position to letter }

    val plotsPerLetter =
        buildMap<String, List<Pair<Int, Int>>> {
            gardenMap
                .forEach { (plot, letter) ->
                    put(letter, getOrDefault(letter, emptyList<Pair<Int, Int>>()) + plot)
                }
        }

    val part1 =
        plotsPerLetter
            .map { (plotName, plots) ->
                findRegionsPerLetter(plotName, plots).sumOf { it.area() * it.perimeter() }
            }.sum()

    println("Part 1 solution is $part1")
}

private fun findRegionsPerLetter(
    name: String,
    plots: List<Pair<Int, Int>>,
): List<Set<Pair<Int, Int>>> {
    val plotsToSplit = plots.toMutableSet()

    val plotsToSkip = emptySet<Pair<Int, Int>>().toMutableSet()

    val results = emptyList<Set<Pair<Int, Int>>>().toMutableList()

    plotsToSplit.forEach { plot ->
        if (plot !in plotsToSkip) {
            val foundArea = findSingleRegion(plot, plotsToSplit)
            plotsToSkip += foundArea
            results += foundArea
        }
    }

    return results
}

private fun findSingleRegion(
    plot: Pair<Int, Int>,
    plots: Set<Pair<Int, Int>>,
    visited: Set<Pair<Int, Int>> = setOf(plot),
): Set<Pair<Int, Int>> {
    val neighbours = setOf(Up, Down, Left, Right).map { plot + it.direction }.filter { it in plots - visited - plot }

    if (neighbours.isEmpty()) return visited

    return neighbours
        .flatMap { findSingleRegion(it, plots - visited - plot, visited + neighbours + plot) }
        .toSet()
}

private fun Set<Pair<Int, Int>>.area() = this.size

private fun Set<Pair<Int, Int>>.perimeter(): Int =
    this
        .flatMap { plot ->
            listOf(Up, Down, Left, Right)
                .map { plot + it.direction }
                .filterNot { r -> r in this }
        }.size