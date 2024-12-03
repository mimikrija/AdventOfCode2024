fun main() {
    val rawInput = readInput("03")
    val oneLineInput = "do()" + rawInput + "don't()"


    val part1 = oneLineInput.calculateScore()


    val dosAndDonts = Regex("(?<=do\\(\\)).*?(?=don't\\(\\))")
    val reducedInput =
        dosAndDonts
            .findAll(oneLineInput)
            .toList()
            .joinToString { it.groupValues.joinToString() }


    val part2 = reducedInput.calculateScore()

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 189600467)
    assert(part2 == 107069718)
}

private fun String.calculateScore(): Int {
    val multiplyRegexGroup = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
    return multiplyRegexGroup.findAll(this).toList().sumOf {
        it.groupValues.let { groups ->
            groups.drop(1).map { it.toInt() }.reduce { bla, truc -> bla * truc }
        }
    }
}

