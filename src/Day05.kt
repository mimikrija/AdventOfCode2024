import java.util.Collections.swap

fun main() {

    val instructions = readInput("05")

    val firstInstructions = instructions
        .takeWhile { it.isNotEmpty() }

    val secondInstructions = (instructions - firstInstructions)
        .drop(1) // get rid of the empty line

    val pageOrderingRules = firstInstructions
        .map { line -> line.split('|').map { it.toInt() } }
        .map {it.first() to it.last()}

    val pagesToProduce = secondInstructions
        .map { line -> line.split(',')
            .map { it.toInt() } }

    val ordered = pagesToProduce
        .filter { page ->
            isOrdered(pageOrderingRules, page)
        }

    val unordered = (pagesToProduce - ordered.toSet())
        .map {  page -> order(pageOrderingRules, page) }



    val part1 = ordered.sumOf { it[it.size / 2] }
    val part2 = unordered.sumOf { it[it.size / 2] }

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")

    assert(part1 == 6505)
    assert(part2 == 1930)
}

private fun order(
    pageOrderingRules: List<Pair<Int, Int>>,
    page: List<Int>,
): List<Int> {
    var currentPage = page
    while (!isOrdered(pageOrderingRules, currentPage)) {
        pageOrderingRules.map { (left, right) ->
            if ((left in currentPage) and (right in currentPage) and (currentPage.indexOf(left) > currentPage.indexOf(right))) {
                swap(currentPage, currentPage.indexOf(left), currentPage.indexOf(right))
            }
        }
    }
    return currentPage
}

private fun byOrderingRules(orderingRules: List<Pair<Int, Int>>) =
    java.util.Comparator { first: Int, second: Int ->
        when {
            first to second in orderingRules -> 1
            second to first in orderingRules -> -1
            else -> 1
        }
    }

private fun List<Int>.isSorted(orderingRules: List<Pair<Int, Int>>) =
    run {
        val sorted = this.sortedWith(byOrderingRules(orderingRules))
        (this zip sorted).all { (first, second) -> first == second } to sorted
    }

private fun isOrdered(
    pageOrderingRules: List<Pair<Int, Int>>,
    page: List<Int>,
) = pageOrderingRules.all { (left, right) ->
    if ((left in page) and (right in page)) {
        page.indexOf(left) < page.indexOf(right)
    } else {
        true
    }
}
