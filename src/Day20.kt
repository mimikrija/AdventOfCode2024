import minus

fun main() {
    val wholeMap =
        readInput("20")
            .flatMapIndexed { row, line ->
                line
                    .mapIndexed { column, letter -> Coordinate(column, row) to letter.toString() }
            }.associate { (position, letter) -> position to letter }
 
    val start = wholeMap.filter { (_, letter) -> letter == "S" }.map { it.key }.first()
    val end = wholeMap.filter { (_, letter) -> letter == "E" }.map { it.key }.first()
    
    val availablePositions = wholeMap
        .filter { (_, letter) -> letter != "#"}
        .map { it.key }
    
    
    val cameFromPath = shortestPathAvailable(availablePositions, start, end)
    val fullPath = cameFromPath.constructPath(start, end)!!

    
    val part1 = fullPath.flatMap { firstPoint -> fullPath
        .map { secondPoint -> firstPoint to secondPoint }
        .filter { (firstPoint, secondPoint) -> firstPoint.manhattanDistanceTo(secondPoint) == 2 }
        .mapNotNull { pair -> bypassGain( fullPath, pair.first, pair.second) }
    }
        .filter { it >= 100 }
        .groupingBy { it }
        .eachCount()
        .map { it.value }
        .sum()
    

    val part2 = fullPath.flatMap { firstPoint -> fullPath
        .map { secondPoint -> firstPoint to secondPoint }
        .filter { (firstPoint, secondPoint) -> firstPoint.manhattanDistanceTo(secondPoint) <= 20 }
        .mapNotNull { pair -> bypassGain( fullPath, pair.first, pair.second) }
    }
        .filter { it >=100 }
        .groupingBy { it }
        .eachCount()
        .map { it.value }
        .sum()
    

    println("Part 1 solution is $part1")
    println("Part 2 solution is $part2")
   
    
    assert(part1 == 1263)
    assert(part2 == 957831)
    
}



private fun bypassGain(
    fullPath: List<Coordinate>,
    start: Coordinate,
    end: Coordinate,
): Int = fullPath.indexOf(end) - fullPath.indexOf(start) - start.manhattanDistanceTo(end)



private fun shortestPathAvailable(
    availableCoordinates: List<Coordinate>,
    start: Coordinate,
    end: Coordinate,
): MutableMap<Coordinate, Coordinate> {
    val frontier = ArrayDeque<Coordinate>()
    frontier.addLast(start)
    val reached = mutableSetOf<Coordinate>()
    reached.add(start)
    val cameFrom = mutableMapOf<Coordinate, Coordinate>()
        
    while(frontier.isNotEmpty()){
        val current = frontier.removeFirst()

        if (current == end) {
            break
        }
        
        neighbors(current)
            .forEach{ neighbor ->
                if((neighbor !in reached) && (neighbor in availableCoordinates)){ 
                    reached.add(neighbor)
                    frontier.addLast(neighbor)
                    cameFrom[neighbor] = current
            }
        }
    }

    return cameFrom
}



private fun MutableMap<Coordinate, Coordinate>.constructPath(
    start: Coordinate,
    end: Coordinate
): List<Coordinate>?
{
    val path = mutableListOf<Coordinate>()
    var current = end
    while (current != start){
        path += current
        if (this.containsKey(current))
            current = this[current]!!
        else {
           return null
        }
    }
    path.add(start)
    return path.reversed()
}

private fun neighbors(
    coordinate: Coordinate,
    includeDiagonals: Boolean = false,
    step: Int = 1,
): List<Coordinate> {
    val main = listOf(coordinate.copy(y = coordinate.y + step), coordinate.copy(y = coordinate.y - step), 
        coordinate.copy(x = coordinate.x + step), coordinate.copy(x = coordinate.x - step))
    
    val diagonals = listOf(
        coordinate.copy(x = coordinate.x + step, y = coordinate.y + step),
        coordinate.copy(x = coordinate.x - step, y = coordinate.y - step), 
        coordinate.copy(x = coordinate.x + step, y = coordinate.y - step),
        coordinate.copy(x = coordinate.x - step, y = coordinate.y + step)
    )
    
    return (if (includeDiagonals) main + diagonals else main)
}