package model.math

fun Vector.drawable(center: Vector): DrawableVector = DrawableVector(center, this)
fun Vector.drawable(x: Double, y: Double): DrawableVector = drawable(Vector(x, y))

// Functional extensions
fun Vector.map(transform: (Double) -> Double) = Vector(transform(x), transform(y))

fun Vector.map(other: Vector, transform: (Double, Double) -> Double) =
    Vector(transform(x, other.x), transform(y, other.y))

fun Vector.mapX(transform: (Double) -> Double) = Vector(transform(x), y)
fun Vector.mapY(transform: (Double) -> Double) = Vector(x, transform(y))

fun Vector.both(predicate: (Double) -> Boolean) = predicate(x) && predicate(y)
fun Vector.both(other: Vector, predicate: (Double, Double) -> Boolean) =
    predicate(x, other.x) && predicate(y, other.y)

fun Vector.either(predicate: (Double) -> Boolean) = predicate(x) || predicate(y)
fun Vector.either(other: Vector, predicate: (Double, Double) -> Boolean) =
    predicate(x, other.x) || predicate(y, other.y)
