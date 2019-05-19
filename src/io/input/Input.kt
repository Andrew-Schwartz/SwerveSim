package io.input

import libraryExtensions.NamedKey
import model.math.Vector

object Input {
    val pressedKeys: MutableSet<NamedKey> = mutableSetOf()
    lateinit var controller: LogitechController

    val rotationSpeed: Double
        get() = pressedKeys.map { it.rotationValue }.sum() +
                controller.rightTrigger - controller.leftTrigger

    val direction: Vector
        get() = Vector(
            pressedKeys.map { it.xValue }.sum(),
            pressedKeys.map { it.yValue }.sum()
        ) + controller.leftStick

    operator fun plusAssign(key: NamedKey) {
        pressedKeys += key
    }

    operator fun minusAssign(key: NamedKey) {
        pressedKeys -= key
    }

    fun readControllers() {
        controller.poll()
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