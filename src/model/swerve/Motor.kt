package model.swerve

import model.math.bound
import model.math.kEpsilon
import kotlin.math.max
import kotlin.math.min

class Motor(
    private val maxVoltage: Double = 12.0, // V
    private val speedPerVolt: Double, // rad/s / V (free)
    private val torquePerVolt: Double, // N.m / V (stall)
    private val frictionVoltage: Double // V (overcome friction)
) {
    /**
     * between -1.0 and 1.0
     */
    var targetSpeed: Double = 0.0
        set(value) {
            field = value bound 1.0
        }

    val targetVoltage: Double get() = targetSpeed * maxVoltage

    /**
     * @return speed in rad/s
     */
    fun freeSpeedAtVoltage(): Double {
        return when {
            targetVoltage > kEpsilon -> max(0.0, targetVoltage - frictionVoltage) * speedPerVolt
            targetVoltage < -kEpsilon -> min(0.0, targetVoltage + frictionVoltage) * speedPerVolt
            else -> 0.0
        }
    }

    /**
     * @param outputSpeed rad/s
     */
    fun torqueAtVoltage(outputSpeed: Double) {
        val effectiveVoltage = when {
            outputSpeed < -kEpsilon -> -frictionVoltage
            outputSpeed > kEpsilon -> frictionVoltage
            else -> TODO("continue stealing from 254")
        }
    }
}
