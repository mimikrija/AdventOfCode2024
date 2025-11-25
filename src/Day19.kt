fun main() {
    val instructions = readInput("19")

    val towels =
        instructions
            .takeWhile { it.isNotEmpty() }
            .first()
            .split(", ")
            .toSet()
    

    val towelCombinations =
        instructions
            .drop(2) // get rid of the empty line
    
    

    val combos = towelCombinations
        .map { combos(it, towels) }
    
    
    val part1 = combos.count { it > 0 }
    val part2 = combos.sumOf { it }


    println("Part 1 solution is $part1")
    println("Part 1 solution is $part2")

    assert(part1 == 213)
    assert(part2 == 1016700771200474)
    
}

private fun combos(
    combinationToCheck: String,
    towels: Set<String>,
    memo: MutableMap<String, Long> = mutableMapOf(),
): Long {
    if (combinationToCheck == "") return 1
    
    if (memo.containsKey(combinationToCheck)) return memo[combinationToCheck]!!
    
    val totalCombinations = towels.sumOf { towel ->
        if (combinationToCheck.indexOf(towel) == 0) {
            combos(combinationToCheck.substringAfter(towel), towels, memo)
        } else {
            0
        }
    }
    
    memo[combinationToCheck] = totalCombinations
    return totalCombinations

}
