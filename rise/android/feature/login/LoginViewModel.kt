package com.example.app.feature.login

import com.example.app.base.BaseReactiveViewModel
import com.example.app.feature.*
import com.example.app.common_util.buildChannel
import com.example.app.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

class LoginViewModel(
    private val authenticator: Authenticator,
    navigator: Navigator
) : BaseReactiveViewModel<LoginViewModel.Event, LoginViewModel.State>(navigator) {

    sealed class State {
        object Uninitialized : State()
        object Loading : State()
        data class ValidationError(val validations: Validations) : State()
        data class LoggedIn(val user: User) : State()
        object Unauthorized : State()
    }

    sealed class Event {
        data class Login(val email: String, val password: String) : Event()
    }

    data class Validations(
        val email: Validation,
        val password: Validation
    )

    fun CoroutineScope.login(email: String, password: String) = buildChannel<State> { states ->
        val emailValidation = email.validateForEmail()
        val passwordValidation = password.validateForPassword(
            minLength = 6,
            maxLength = 10,
            upperCaseChars = 4
        )

        if (isAllValid(emailValidation, passwordValidation)) {
            // field validation success
            states.send(State.Loading)
            when (val result = authenticator.logIn(email, password)) {
                is Success -> {
                    val user = result.value
                    states.send(State.LoggedIn(user))
                    navigator.showHome()
                }
                is Error -> {
                    val failure = result.failure
                    states.send(State.Unauthorized)
                }
            }
        } else {
            // field validation errors
            states.send(
                State.ValidationError(
                    Validations(
                        email = emailValidation,
                        password = passwordValidation
                    )
                )
            )
        }
    }

    override fun initialState(): State {
        return State.Uninitialized
    }

    override fun CoroutineScope.mapEventToState(
        event: Event,
        currentState: State
    ): ReceiveChannel<State> {
        return when (event) {
            is Event.Login -> login(event.email, event.password)
        }
    }

}