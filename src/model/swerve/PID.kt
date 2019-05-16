package model.swerve

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class PID(
    val setpoint: () -> Double,
    val current: () -> Double,
    val p: Double = 0.0,
    val i: Double = 0.0,
    val d: Double = 0.0
) : ReadOnlyProperty<Any, Double> {
    private val error get() = current() - setpoint()
    private var accum = 0.0
    private var prev = current()

    override fun getValue(thisRef: Any, property: KProperty<*>): Double {
        val e = error/*.also { println(it) }*/
        accum += e
        val delta = current() - prev
        return (p * e + i * accum + d * delta).also { prev = current() }
    }
}