package util

import model.math.Vector
import model.math.average

class RotatingVectorArray(private val size: Int) {
    var index: Int = 0

    private val contents = Array<Vector?>(size) { null }

    operator fun plus(element: Vector) {
        contents[index++ % size] = element
    }

    fun average() = contents.filterNotNull().average()
}