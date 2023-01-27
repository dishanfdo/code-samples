package com.example.app.feature.login

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.example.app.R
import com.example.app.app.apputil.hideSoftKeyboard
import com.example.app.app.apputil.updateFieldWithValidation
import com.example.app.app.base.BaseReactiveActivity
import com.example.app.app.feature.Navigator
import com.example.app.common_util.Validation
import com.example.app.common_util.ValidationError
import com.example.app.data.AuthenticatorImpl
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup_step_one.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity: BaseReactiveActivity<LoginViewModel.Event, LoginViewModel.State>() {

    override val reactiveViewModel: LoginViewModel by viewModel()

    override fun getContentViewResource(): Int? {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBtn.setOnClickListener {
            val email = emailText.editText?.text?.toString() ?: ""
            val password = passwordText.editText?.text?.toString() ?: ""
            errorMsg.text=""
            hideSoftKeyboard()
            dispatch(LoginViewModel.Event.Login(email, password))
        }

        fogetPassword.setOnClickListener {
            errorMsg.text=""
            hideSoftKeyboard()
            dispatch(LoginViewModel.Event.ResetPassword)
        }
    }

    override fun react(state: LoginViewModel.State) {
        when (state) {
            LoginViewModel.State.Loading -> {
                showLoadingIndicator()
            }

            is LoginViewModel.State.ValidationError -> {
                hideLoadingIndicator()
                val validations = state.validations

                emailText.updateWithEmailValidation(validations.email)
                passwordText.updateWithPasswordValidation(validations.password)
            }

            is LoginViewModel.State.LoginError -> {
                hideLoadingIndicator()
                val failure = state.failure
                val handled = handleCommonFailures(failure)
                if (!handled) {
                    when (failure) {
                        is AuthenticatorImpl.AuthFailure.InvalidAuth -> {
                            showInvalidCredentialsError()
                        }
                        else -> showUnknownError()
                    }
                }
            }
        }
    }

    private fun showInvalidCredentialsError() {
        errorMsg.text=getString(R.string.invalid_email_password_error).toString()
    }

    private fun TextInputLayout.updateWithPasswordValidation(validation: Validation) {
        updateFieldWithValidation(validation) { errors ->
            error = when (val valError = errors.first()) {
                ValidationError.Empty -> getString(R.string.empty_password_error)
                else -> getString(R.string.empty_password_error)
            }
        }
    }

    private fun TextInputLayout.updateWithEmailValidation(validation: Validation) {
        updateFieldWithValidation(validation) { errors ->
            error = when (errors.first()) {
                ValidationError.Empty ->getString(R.string.empty_email_error)
                ValidationError.InvalidValue -> getString(R.string.invalid_email_error)
                else ->  getString(R.string.invalid_email_error)
            }
        }

    }
}
