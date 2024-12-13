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
                findRegionsPerLetter(plots).sumOf { it.area() * it.perimeter().size }
            }.sum()

    val part2 =
        plotsPerLetter
            .map { (_, plots) ->
                findRegionsPerLetter(plots).sumOf {
                    it.area() * it.sidesPerRegion()
                }
            }.sum()

    println("Part 1 solution is $part1")
    println("Part 1 solution is $part2")
    assert(part1 == 1465112)
    assert(part2 == 893790)
}

private fun findRegionsPerLetter(plots: List<Pair<Int, Int>>): List<Set<Pair<Int, Int>>> {
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
): Set<Pair<Int, Int>> {
    val visited = setOf(plot).toMutableSet()
    val frontier = ArrayDeque(listOf(plot))
    while (frontier.isNotEmpty()) {
        val current = frontier.removeFirst()
        val neighbours =
            setOf(Up, Down, Left, Right).map { current + it.direction }.filter { it in plots - visited }

        neighbours.forEach { frontier.add(it) }
        visited += neighbours
    }
    return visited
}

private fun Set<Pair<Int, Int>>.area() = this.size

private fun Set<Pair<Int, Int>>.perimeter() =
    this
        .flatMap { plot ->
            listOf(Up, Down, Left, Right)
                .map { plot + it.direction }
                .filterNot { r -> r in this }
        }

private fun Set<Pair<Int, Int>>.sidesPerRegion(): Int {
    val allDirections = listOf(Up, Down, Left, Right)
    val leftUp = listOf(Left, Up)
    val rightUp = listOf(Right, Up)
    val leftDown = listOf(Left, Down)
    val rightDown = listOf(Right, Down)

    val leftAndUpOutSideCorners =
        this
            .filter { position ->
                val leftAndUpNeighbours = leftUp.map { dir -> position + dir.direction }.filter { it !in this }.toSet()
                val downAndRightNeighbours =
                    rightDown.map { dir -> position + dir.direction }.filter { it in this }.toSet()
                (leftAndUpNeighbours.size == 2) and (downAndRightNeighbours.size <= 2)
            }

    val rightAndUpOutSideCorners =
        this
            .filter { position ->
                val leftAndUpNeighbours = rightUp.map { dir -> position + dir.direction }.filter { it !in this }.toSet()
                val downAndRightNeighbours =
                    leftDown.map { dir -> position + dir.direction }.filter { it in this }.toSet()
                (leftAndUpNeighbours.size == 2) and (downAndRightNeighbours.size <= 2)
            }

    val leftAndDownOutSideCorners =
        this
            .filter { position ->
                val leftAndUpNeighbours = leftDown.map { dir -> position + dir.direction }.filter { it !in this }.toSet()
                val downAndRightNeighbours =
                    rightUp.map { dir -> position + dir.direction }.filter { it in this }.toSet()
                (leftAndUpNeighbours.size == 2) and (downAndRightNeighbours.size <= 2)
            }

    val rightAndDownOutSideCorners =
        this
            .filter { position ->
                val leftAndUpNeighbours = rightDown.map { dir -> position + dir.direction }.filter { it !in this }.toSet()
                val downAndRightNeighbours =
                    leftUp.map { dir -> position + dir.direction }.filter { it in this }.toSet()
                (leftAndUpNeighbours.size == 2) and (downAndRightNeighbours.size <= 2)
            }

    val outSideCorners =
        listOf(
            leftAndUpOutSideCorners,
            leftAndDownOutSideCorners,
            rightAndUpOutSideCorners,
            rightAndDownOutSideCorners,
        ).sumOf { it.size }

    val cancelRightDown =
        rightAndDownOutSideCorners.count { position -> position + Down.direction + Right.direction in leftAndUpOutSideCorners } * 2
    val cancelRightUp =
        rightAndUpOutSideCorners.count { position -> position + Up.direction + Right.direction in leftAndDownOutSideCorners } * 2

    val insideCorners =
        this
            .perimeter()
            .toSet()
            .sumOf { position ->
                val allInsideNeighbours = allDirections.map { dir -> position + dir.direction }.filter { it in this }.toSet()
                if (allInsideNeighbours.size < 2) {
                    0
                } else {
                    val allPotentialCornerSituations =
                        listOf(leftUp, leftDown, rightDown, rightUp)
                            .map { list ->
                                list
                                    .map { dir ->
                                        position +
                                            dir.direction
                                    }.filter { it in this }
                                    .toSet()
                            }.filter { it.size == 2 }
                    allPotentialCornerSituations.count { (it intersect allInsideNeighbours) == it }
                }
            }

    return outSideCorners + insideCorners - cancelRightUp - cancelRightDown
}
