package model.math

import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun Double.negateIf(predicate: (Double) -> Boolean): Double = this * if (predicate(this)) -1 else 1

fun Double.bound(max: Double): Double = max.absoluteValue.let { max(-it, min(it, this)) }

infix fun Double.epsilonEquals(other: Double): Boolean = abs(this - other) < 1E-12