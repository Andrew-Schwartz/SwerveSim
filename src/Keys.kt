import model.math.Vector
import processingExt.NamedKey

object Keys {
    val pressedKeys: MutableSet<NamedKey> = mutableSetOf()

    val rotationSpeed: Int
        get() = pressedKeys
            .map { it.rotationValue }
            .sum()

    val direction: Vector
        get() = Vector(
            pressedKeys.map { it.xValue }.sum(),
            pressedKeys.map { it.yValue }.sum()
        )

    operator fun plusAssign(key: NamedKey) {
        pressedKeys += key
    }

    operator fun minusAssign(key: NamedKey) {
        pressedKeys -= key
    }

    private val NamedKey.rotationValue: Int
        get() = when (this) {
            NamedKey.X -> 1
            NamedKey.Z -> -1
            else -> 0
        }

    private val NamedKey.xValue: Int
        get() = when (this) {
            NamedKey.LEFT_ARROW -> -1
            NamedKey.RIGHT_ARROW -> 1
            else -> 0
        }

    private val NamedKey.yValue: Int
        get() = when (this) {
            NamedKey.UP_ARROW -> 1
            NamedKey.DOWN_ARROW -> -1
            else -> 0
        }
}