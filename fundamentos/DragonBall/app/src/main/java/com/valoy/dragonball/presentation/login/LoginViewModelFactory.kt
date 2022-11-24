package com.valoy.dragonball.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valoy.dragonball.network.LoginService
import com.valoy.dragonball.repository.Preferences

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginViewModel(LoginService(), Preferences(context)) as T
}