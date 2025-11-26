

fun main() {
    val incomingByteCoordinates = readInput("18")
        .map { row -> row.split(",") }
        .map { (x, y) -> Coordinate(x.toInt(), y.toInt())}
    
 
    val shortestPathSoFar = shortestPath(
        wallCoordinates = incomingByteCoordinates.slice(0 until 1024),
        gridSize = 71,
    )
    
    val part1 = shortestPathSoFar?.size

    val part2 = findFirstBlocking(shortestPathSoFar,incomingByteCoordinates, gridSize = 71, startingIndex = 1024)

    println("Part 1 solution is $part1")
    println("Part 1 solution is ${part2?.x},${part2?.y}")
    
    assert(part1 == 506)
    assert(part2?.x == 62 && part2.y == 6)
}


private fun shortestPath(
    wallCoordinates: List<Coordinate>,
    gridSize: Int,
    start: Coordinate = Coordinate(0, 0),
    end: Coordinate = Coordinate(gridSize - 1, gridSize - 1)
): List<Coordinate>? {
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
        
        neighbors(current, gridSize)
            .forEach{ neighbor ->
                if((neighbor !in reached) && (neighbor !in wallCoordinates)){ 
                    reached.add(neighbor)
                    frontier.addLast(neighbor)
                    cameFrom[neighbor] = current
            }
        }
    }

    return cameFrom.constructPath(start, end)
}

private fun findFirstBlocking(
    shortestPathSoFar: List<Coordinate>?,
    coordinates: List<Coordinate>,
    gridSize: Int,
    startingIndex: Int,
): Coordinate? {
    var soFar = shortestPathSoFar
    val expandedBy = coordinates.slice(0 until startingIndex).toMutableList()

    coordinates.slice(startingIndex until coordinates.size)
        .forEach { blockingCoordinate -> 
            expandedBy.add(blockingCoordinate)
            if (soFar?.contains(blockingCoordinate) == true ){ 
                soFar = shortestPath(expandedBy, gridSize)
                if (soFar == null) return blockingCoordinate
        }
    }
    return null
}

private fun MutableMap<Coordinate, Coordinate>.constructPath(
    start: Coordinate = Coordinate(0, 0),
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
    gridSize: Int,
    includeDiagonals: Boolean = false,
    isInfinite: Boolean = false
): List<Coordinate> {
    val main = listOf(coordinate.copy(y = coordinate.y + 1), coordinate.copy(y = coordinate.y - 1), 
        coordinate.copy(x = coordinate.x + 1), coordinate.copy(x = coordinate.x - 1))
    
    val diagonals = listOf(
        coordinate.copy(x = coordinate.x + 1, y = coordinate.y + 1),
        coordinate.copy(x = coordinate.x - 1, y = coordinate.y - 1), 
        coordinate.copy(x = coordinate.x + 1, y = coordinate.y - 1),
        coordinate.copy(x = coordinate.x - 1, y = coordinate.y + 1)
    )
    
    return (if (includeDiagonals) main + diagonals else main)
        .filter{ if(isInfinite) true else it.x in 0 until gridSize && it.y in 0 until gridSize}
}