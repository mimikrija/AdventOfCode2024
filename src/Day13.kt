

fun main() {
    val buttonBehavior =
        readInputNoLines("13")
            .split("\n\n")
            .map { it.split("\n").map { st -> Regex("\\d+").findAll(st).map { f -> f.value.toLong() }.toList() } }

    val part1 =
        buttonBehavior.sumOf { l ->
            val (a, b, p) = l.map { it.first() to it.last() }
            claw(a, b, p)
        }

    val part2 =
        buttonBehavior.sumOf { l ->
            val (a, b, p) = l.map { it.first() to it.last() }
            val bigPrize = p.first + 10000000000000 to p.second + 10000000000000
            claw(a, b, bigPrize, isPart2 = true)
        }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 34393L)
    assert(part2 == 83551068361379)
}

private fun claw(
    buttonA: Pair<Long, Long>,
    buttonB: Pair<Long, Long>,
    prize: Pair<Long, Long>,
    isPart2: Boolean = false,
): Long {
    val det = buttonA.first * buttonB.second - buttonB.first * buttonA.second
    val detA = prize.first * buttonB.second - buttonB.first * prize.second
    val detB = buttonA.first * prize.second - prize.first * buttonA.second

    // filter invalid (or non-unique) solutions
    if (det == 0L) return 0

    val determinants = listOf(detA, detB)

    // filter non-integer solutions
    if (determinants.any { it % det != 0L }) return 0

    val counts =
        listOf(detA, detB)
            .map { it / det }

    // filter condition pt1
    if (counts.any { it !in 0..100 } and !isPart2) return 0

    // filter condition pt2
    if (counts.any { it < 0 } and isPart2) return 0

    return counts.first() * 3 + counts.last()
}
