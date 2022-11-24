package com.valoy.dragonball.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.valoy.dragonball.dto.UserDTO
import com.valoy.dragonball.network.LoginService
import com.valoy.dragonball.network.Resource
import com.valoy.dragonball.repository.Preferences

class LoginViewModel(
    private val loginService: LoginService,
    private val preferences: Preferences
) : ViewModel() {

    val stateLiveData: MutableLiveData<State> by lazy {
        MutableLiveData<State>()
    }

    init {
        loggedIn()
    }

    fun onLogin(user: String, password: String) {
        stateLiveData.postValue(State.Loading)

        loginService.execute(UserDTO(user, password)) { resource ->
            if (resource is Resource.Success) {
                resource.data?.let { preferences.saveToken(it) }
                stateLiveData.postValue(State.Success)
            } else {
                stateLiveData.postValue(State.Error("Try again"))
            }
        }

    }

    private fun loggedIn() {
        if (!preferences.getToken().isNullOrEmpty()) {
            stateLiveData.postValue(State.Success)
        }
    }

    sealed class State {
        object Success : State()
        data class Error(val message: String) : State()
        object Loading : State()
    }
}