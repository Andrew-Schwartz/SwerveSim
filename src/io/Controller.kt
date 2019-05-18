package io

import model.math.Vector
import model.math.map
import model.math.withDeadband
import org.lwjgl.glfw.GLFW.glfwGetJoystickAxes
import org.lwjgl.glfw.GLFW.glfwJoystickPresent

class Controller(private val port: Int) {
    val exists: Boolean
        get() = glfwJoystickPresent(port)

    val leftStick: Vector
        get() = if (exists) {
            Vector(axes[0], -axes[1]).map { it.withDeadband(0.05) }
        } else {
            Vector(0.0, 0.0)
        }

    val rightStick: Vector
        get() = if (exists) {
            Vector(axes[2], -axes[3]).map { it.withDeadband(0.05) }
        } else {
            Vector(0.0, 0.0)
        }

    val leftBumper: Double
        get() = if (exists) (axes[4] + 1) / 2.0 else 0.0

    val rightBumper: Double
        get() = if (exists) (axes[5] + 1) / 2.0 else 0.0

    private lateinit var axes: FloatArray

    fun poll() {
        if (exists)
            axes = glfwGetJoystickAxes(port)!!.let { buffer ->
                FloatArray(buffer.remaining()) { buffer.get() }
            }
    }
}