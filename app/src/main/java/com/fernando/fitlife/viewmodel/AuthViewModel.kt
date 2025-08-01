package com.fernando.fitlife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.fitlife.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()
    val currentUser get() = repo.currentUser

    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun clearMessage() {
        _message.value = null
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repo.login(email, password)
            if (result != null) {
                _role.value = result
                _message.value = "Login realizado com sucesso"
            } else {
                _message.value = "Falha ao realizar login"
            }
        }
    }

    fun register(
        email: String,
        password: String,
        trainer: Boolean,
        nome: String,
        especialidade: String? = null,
        descricao: String? = null,
        fotoUrl: String? = null
    ) {
        viewModelScope.launch {
            val success = repo.register(
                email,
                password,
                if (trainer) "trainer" else "client",
                nome,
                especialidade,
                descricao,
                fotoUrl
            )
            if (success) {
                _role.value = if (trainer) "trainer" else "client"
                _message.value = "Conta criada com sucesso"
            } else {
                _message.value = "Erro ao criar conta"
            }
        }
    }

    fun logout() {
        repo.logout()
        _role.value = null
    }

    fun loadRole() {
        val uid = repo.currentUser?.uid ?: return
        viewModelScope.launch {
            _role.value = repo.getRole(uid)
        }
    }
}
