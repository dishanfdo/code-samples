package com.example.app.common_util

sealed class Either<out L, out R> {
    /** Represent the left side of [Either] class which by convention isa "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** Represent the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    companion object {
        fun <L> left(a: L) = Left(a)
        fun <R> right(b: R) = Right(b)
    }
}

inline fun <T, L, R> Either<L, R>.map(f: (R) -> T): Either<L, T> = when (this) {
    is Either.Left -> this
    is Either.Right -> Either.Right(f(b))
}

inline fun <T, L, R> Either<L, R>.flatMap(f: (R) -> Either<L, T>): Either<L, T> = when (this) {
    is Either.Left -> this
    is Either.Right -> f(b)
}