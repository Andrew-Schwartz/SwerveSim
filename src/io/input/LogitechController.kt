package io.input

import model.math.Vector
import model.math.map
import model.math.withDeadband

class LogitechController(port: Int) : Controller(port) {
    val leftStick: Vector by MayExist(Vector.ZERO) {
        Vector(axes[0], -axes[1]).map { it.withDeadband(0.05) }
    }

    val rightStick: Vector by MayExist(Vector.ZERO) {
        Vector(axes[2], -axes[3]).map { it.withDeadband(0.05) }
    }

    val leftTrigger: Double by MayExist(default = 0.0) { (axes[4] + 1) / 2.0 }

    val rightTrigger: Double by MayExist(default = 0.0) { (axes[5] + 1) / 2.0 }

    val aButton by Button(0)
    val bButton by Button(1)
    val xButton by Button(2)
    val yButton by Button(3)
    val leftBumper by Button(4)
    val rightBumper by Button(5)
    val backButton by Button(6)
    val startButton by Button(7)
    val leftStickButton by Button(8)
    val rightStickButton by Button(9)
    val dUp by Button(10)
    val dLeft by Button(11)
    val dDown by Button(12)
    val dRight by Button(13)
}