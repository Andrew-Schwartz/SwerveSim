package model.math

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.*

data class Vector(val x: Double, val y: Double) {
    companion object {
        val iHat = Vector(1.0, 0.0)
        val jHat = Vector(0.0, 1.0)

        val ZERO = Vector(0.0, 0.0)
    }

    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())
    constructor(coords: List<Number>) : this(coords[0], coords[1])
    constructor(coords: Pair<Number, Number>) : this(coords.first, coords.second)

    val magnitude: Double get() = hypot(x, y)
    operator fun component3() = magnitude

    /**
     * angle in degrees
     */
    val angle: Double get() = toDegrees(atan2(y, x))

    operator fun component4() = angle

    fun normalize(targetMagnitude: Double = 1.0): Vector = this * (targetMagnitude / this.magnitude)

    fun rotate(degrees: Double): Vector {
        val radians = toRadians(degrees)
        return Vector(x * cos(radians) - y * sin(radians), x * sin(radians) + y * cos(radians))
    }

    fun rotateAround(center: Vector, degrees: Double): Vector {
        val zeroed = Vector(this.x - center.x, this.y - center.y)
        return center + zeroed.rotate(degrees)
    }

    infix fun bound(max: Double): Vector = abs(max).let { if (abs(magnitude) > max) normalize(max) else this }

    infix fun boundEach(max: Double): Vector = map { it bound max }

    operator fun plus(other: Vector): Vector = map(other, Double::plus)

    operator fun unaryMinus(): Vector = map { -it }

    operator fun minus(other: Vector): Vector = map(other, Double::minus)

    operator fun times(scalar: Double): Vector = map { it * scalar }

    operator fun div(scalar: Double): Vector = map { it / scalar }

    infix fun dot(other: Vector): Double = x * other.x + y * other.y
}
