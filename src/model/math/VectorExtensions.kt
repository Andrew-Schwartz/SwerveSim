package model.math

fun Vector.drawable(center: Vector): DrawableVector = DrawableVector(center, this)
fun Vector.drawable(x: Double, y: Double): DrawableVector = drawable(Vector(x, y))

// Functional extensions
inline fun Vector.map(transform: (Double) -> Double) = Vector(transform(x), transform(y))

inline fun Vector.map(other: Vector, transform: (Double, Double) -> Double) =
    Vector(transform(x, other.x), transform(y, other.y))

inline fun Vector.mapX(transform: (Double) -> Double) = Vector(transform(x), y)
inline fun Vector.mapY(transform: (Double) -> Double) = Vector(x, transform(y))

inline fun Vector.both(predicate: (Double) -> Boolean) = predicate(x) && predicate(y)
inline fun Vector.both(other: Vector, predicate: (Double, Double) -> Boolean) =
    predicate(x, other.x) && predicate(y, other.y)

inline fun Vector.either(predicate: (Double) -> Boolean) = predicate(x) || predicate(y)
inline fun Vector.either(other: Vector, predicate: (Double, Double) -> Boolean) =
    predicate(x, other.x) || predicate(y, other.y)

fun Iterable<Vector>.sum(): Vector = reduce(Vector::plus)