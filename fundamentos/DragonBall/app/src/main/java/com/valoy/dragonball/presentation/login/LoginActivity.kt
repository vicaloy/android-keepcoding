package com.valoy.dragonball.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.valoy.dragonball.databinding.ActivityLoginBinding
import com.valoy.dragonball.presentation.game.GameActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> { LoginViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindSignInButton()
        observeState()
    }

    private fun bindSignInButton(){
        binding.signInButton.setOnClickListener {
            viewModel.onLogin(
                binding.userField.editText?.text.toString(),
                binding.passwordField.editText?.text.toString()
            )
        }
    }

    private fun observeState(){
        viewModel.stateLiveData.observe(this) {
            when (it) {
                is LoginViewModel.State.Loading -> {
                    binding.signInButton.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is LoginViewModel.State.Success -> {
                    startActivity(Intent(this, GameActivity::class.java))
                    finish()
                }
                is LoginViewModel.State.Error -> {
                    binding.signInButton.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

}