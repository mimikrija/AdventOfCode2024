import kotlin.collections.map

fun main() {
    val numbers = readInput("22")
            .takeWhile { it.isNotEmpty() }
            .map { it.toLong() }
    
    val sequences = numbers
        .map { secretNumber ->
            val sequence = mutableListOf(secretNumber)
            var newNumber = secretNumber
            (1 .. 2000).forEach{ _ -> 
                newNumber = newNumber.transform()
                sequence.add(newNumber)
            }
            sequence
    }


    val part1 = sequences.sumOf { it.last() }

    val prices = sequences.map {sequence -> sequence.map{it % 10}}

    val sequenceToPriceMap = prices.map{ 
        priceList -> 
        val diffs = priceList
            .zipWithNext()
            .map{it.second - it.first}
        diffs.windowed(4).zip(priceList.slice(4 until priceList.size))
            .reversed() // TAKE THE FIRST OCCURENCE OF A SEQUENCE (is there a better way..?)
            .associate { it }
    }

    val part2 = sequenceToPriceMap
        // for each possible sequence
        .flatMap { it.keys }.toSet()
        .maxOf { sequence ->
            sequenceToPriceMap
                // find matching price in buyers list (if exists)
                .mapNotNull { it[sequence] }
                // add all the prices
                .sum()
    } // find max per sequence
        .toInt()

    println("Part 1 solution is $part1")
   
    println("Part 1 solution is $part2") 
    assert(part1 == 16999668565)
    assert(part2 == 1898)
}

private fun Long.transform(): Long {
    val first = ((this * 64) xor this) % 16777216
    val second = (first.div(32) xor first) % 16777216
    return  ((second * 2048) xor second) % 16777216
}

