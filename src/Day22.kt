fun main() {
    val numbers = readInput("22")
            .takeWhile { it.isNotEmpty() }
            .map { it.toLong() }
    
    var pt1 = numbers
        for (i in 1..2000) {

           pt1 = pt1
                .map { secretNumber -> ((secretNumber * 64) xor secretNumber) % 16777216 }
                .map { secretNumber -> (secretNumber.div(32) xor secretNumber) % 16777216 }
                .map { secretNumber -> ((secretNumber * 2048) xor secretNumber) % 16777216 }
        }
    val part1 = pt1.sum()

    println("Part 1 solution is $part1")
    assert(part1 == 16999668565)
}

