package io.input

import org.lwjgl.glfw.GLFW.*

abstract class Controller(val port: Int) {
    val exists: Boolean get() = glfwJoystickPresent(port)

    lateinit var axes: FloatArray

    var buttonsMap: Int = 0

    fun poll() {
        if (exists) {
            axes = glfwGetJoystickAxes(port)!!.let { buffer ->
                FloatArray(buffer.remaining()) { buffer.get() }
            }

            buttonsMap = glfwGetJoystickButtons(port)!!.let { buffer ->
                var sum = 0
                for (i in 0 until buffer.remaining())
                    sum += buffer.get().toInt() shl i
                sum
            }
        }
    }
}
