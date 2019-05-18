package model.math

import kotlin.math.*

fun Double.negateIf(predicate: (Double) -> Boolean): Double = this * if (predicate(this)) -1 else 1

fun Double.bound(max: Double): Double = max.absoluteValue.let { max(-it, min(it, this)) }

fun Double.bound(min: Double, max: Double): Double = max(min, min(max, this))

fun Double.bound(min: Number, max: Number): Double = max(min.toDouble(), min(max.toDouble(), this))

fun Double.withDeadband(deadband: Double): Double =
    if (abs(this) >= deadband) sign * ((1 - deadband) * absoluteValue + deadband)
    else 0.0

infix fun Double.epsilonEquals(other: Double): Boolean = abs(this - other) < 1E-12

fun Double.sqrt(): Double = sqrt(this)

fun Double.round(n: Int): Double {
    val factor = 10.0.pow(n)
    return (this * factor).toInt() / factor
}