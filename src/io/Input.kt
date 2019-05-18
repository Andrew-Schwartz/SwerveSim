package io

import model.math.Vector
import model.math.sum
import processingExt.NamedKey

object Input {
    val pressedKeys: MutableSet<NamedKey> = mutableSetOf()
    val controllers: MutableSet<Controller> = mutableSetOf()

    val rotationSpeed: Double
        get() = pressedKeys.map { it.rotationValue }.sum() +
                controllers.map { it.rightBumper - it.leftBumper }.sum()

    val direction: Vector
        get() = Vector(
            pressedKeys.map { it.xValue }.sum(),
            pressedKeys.map { it.yValue }.sum()
        ) + controllers.map { it.leftStick }.sum()

    operator fun plusAssign(key: NamedKey) {
        pressedKeys += key
    }

    operator fun minusAssign(key: NamedKey) {
        pressedKeys -= key
    }

    fun readControllers() {
        for (controller in controllers) controller.poll()
    }

    private val NamedKey.rotationValue: Double
        get() = when (this) {
            NamedKey.X -> 1.0
            NamedKey.Z -> -1.0
            else -> 0.0
        }

    private val NamedKey.xValue: Double
        get() = when (this) {
            NamedKey.LEFT_ARROW -> -1.0
            NamedKey.RIGHT_ARROW -> 1.0
            else -> 0.0
        }

    private val NamedKey.yValue: Double
        get() = when (this) {
            NamedKey.UP_ARROW -> 1.0
            NamedKey.DOWN_ARROW -> -1.0
            else -> 0.0
        }
}