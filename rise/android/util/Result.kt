package com.example.app.common_util

abstract class Failure
class FailureException(val failure: Failure): Exception()

typealias Result<T> = Either<Failure, T>
typealias Success<T> = Either.Right<T>
typealias Error = Either.Left<Failure>

private typealias ResultCompanion = Either.Companion

val <T> Result<T>.isSuccess get() = this.isRight
val <T> Result<T>.isFailure get() = this.isLeft

val <T> Success<T>.value get() = this.b
val Error.failure get() = this.a

fun <T> ResultCompanion.success(value: T) = right(value)
fun ResultCompanion.failure(failure: Failure) = left(failure)

fun <T> Result<T>.handle(onSuccess: (T) -> Unit, onFailure: (Failure) -> Unit) {
    when (this) {
        is Either.Left -> onFailure(a)
        is Either.Right -> onSuccess(b)
    }
}

fun <T> Result<T>.doOnSuccess(block: (T) -> Unit) {
    if (this is Success) {
        block(this.value)
    }
}

fun <T> Result<T>.unWrap(): T {
    when (this) {
        is Success -> return this.value
        is Error -> throw FailureException(this.failure)
    }
}

fun <T> Result<T>.mapFailure(converter: (Failure) -> Failure): Result<T> {
    return when (this) {
        is Success -> this
        is Error -> mapFailure(converter)
    }
}

inline fun Error.mapFailure(converter: (Failure) -> Failure): Error {
    val failure = this.failure
    val converted = converter(failure)
    return Result.failure(converted)
}