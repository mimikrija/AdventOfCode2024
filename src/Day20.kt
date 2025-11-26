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
    val fullPath = cameFromPath.constructPath(start, end)
    
    val part1 = fullPath?.flatMap { start ->
        val candidates = byPassCandidates(
            position = start,
            fullPath = fullPath,
        )
        candidates
            .mapNotNull{ end -> bypassGain(
                cameFrom = cameFromPath,
                start = start,
                end = end,
            ) }.filter { it > 0 }
    }?.groupingBy { it }?.eachCount()?.filter { it.key >=100 }?.map { it.value }?.sum()
    

    println("Part 1 solution is $part1")
 
    
    assert(part1 == 1263)
    
}

private fun byPassCandidates(
    position: Coordinate,
    fullPath: List<Coordinate>,
): List<Coordinate> {
    val neighbors = neighbors(position, step = 2)
    return neighbors.filter { it in fullPath }
}

private fun bypassGain(
    cameFrom: MutableMap<Coordinate, Coordinate>,
    start: Coordinate,
    end: Coordinate
): Long? = cameFrom.constructPath(start, end)?.size?.minus(2)?.toLong()


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