

fun main() {
    val robotsVelocities =
        readInput("14")
            .map { st ->
                Regex("-?\\d+")
                    .findAll(st)
                    .map { f -> f.value.toInt() }
                    .toList()
            }

    val LIMITS = 100 to 102

    val positions =
        robotsVelocities.map {
            val (px, py, vx, vy) = it
            moveRobot(px to py, vx to vy, 100, LIMITS)
        }

    val part1 = positions.splitToQuadrants(LIMITS).safetyFactor()

    // I was just looking at printed outputs and figured that some concentration
    // of pixels happens at 89 + n * 721
    // found first tree at 89 + 53 * 721 which was too high
    // so I started checking for the line from the tree frame in the plot
    // and returned the first one

    val positionsTree =
        robotsVelocities.map {
            val (px, py, vx, vy) = it
            moveRobot(px to py, vx to vy, 7093, LIMITS)
        }

    val part2 = positionsTree.plotImage(LIMITS, 7093)

    println("Part 1 solution is: $part1")
    println("Part 2 solution is: $part2")

    assert(part1 == 228690000)
    assert(part2 == 7093)
}

fun moveRobot(
    position: Pair<Int, Int>,
    velocity: Pair<Int, Int>,
    steps: Int,
    limit: Pair<Int, Int>,
): Pair<Int, Int> {
    val posX = (limit.first + 1 + (position.first + steps * velocity.first) % (limit.first + 1)) % (limit.first + 1)
    val posY = (limit.second + 1 + (position.second + steps * velocity.second) % (limit.second + 1)) % (limit.second + 1)

    return posX to posY
}

private fun List<Pair<Int, Int>>.splitToQuadrants(limit: Pair<Int, Int>): List<Int> {
    val secondAndThird = this.filter { it.first > limit.first / 2 }
    val firstAndFourth = this.filter { it.first < limit.first / 2 }

    val first = this.filter { (it.first > limit.first / 2) and (it.second < limit.second / 2) }
    val fourth = this.filter { (it.first > limit.first / 2) and (it.second > limit.second / 2) }
    val second = this.filter { (it.first < limit.first / 2) and (it.second < limit.second / 2) }
    val third = this.filter { (it.first < limit.first / 2) and (it.second > limit.second / 2) }

    return listOf(first.size, second.size, third.size, fourth.size)
}

private fun List<Int>.safetyFactor() = this.fold(initial = 1) { a, b -> a * b }

private fun List<Pair<Int, Int>>.plotImage(
    limit: Pair<Int, Int>,
    steps: Int,
): Int {
    val uniqueRobots = this.toSet()

    val left = uniqueRobots.filter { it.first < limit.first / 2 }.map { it + (limit.first / 2 to 0) }
    val right = uniqueRobots.filter { it.first > limit.first / 2 }

    val lines =
        (0..limit.second).map { row ->
            (0..limit.first).joinToString("") { column ->
                if (column to row in uniqueRobots) "#" else "."
            }
        }

    if (lines.any { ".###############################." in it }) {
        println("part2 solution is $steps")
        lines.forEach { println(it) }
        return steps
    }

    return 0
}
