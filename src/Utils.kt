import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("inputs/$name.txt").readText().trim().lines()

fun readInputNoLines(name: String) = Path("inputs/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)

operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = Pair(this.first - other.first, this.second - other.second)

operator fun Int.times(other: Pair<Int, Int>) = Pair(this * other.first, this * other.second)

fun <T> getCombinations(list: List<T>): List<Pair<T, T>> =
    list.flatMapIndexed { index, value ->
        list.drop(index + 1).map { other -> value to other }
    }

sealed interface Direction {
    val direction: Pair<Int, Int>

    fun rotateClockwise(): Direction
}

data object Up : Direction {
    override val direction = 0 to -1

    override fun rotateClockwise() = Right
}

data object Right : Direction {
    override val direction = 1 to 0

    override fun rotateClockwise() = Down
}

data object Down : Direction {
    override val direction = 0 to 1

    override fun rotateClockwise() = Left
}

data object Left : Direction {
    override val direction = -1 to 0

    override fun rotateClockwise() = Up
}

data class Coordinate(val x: Int, val y: Int){
    fun manhattanDistanceTo(other: Coordinate) = (x - other.x).absoluteValue + (y - other.y).absoluteValue
}
