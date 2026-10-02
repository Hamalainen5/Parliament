package com.example.parliament.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MemberViewModel(
    private val repository: MemberRepository
) : ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var members by mutableStateOf<List<Member>>(emptyList())
        private set

    init {
        loadMembers()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                repository.refreshMembers()
                members = repository.getMembers()

            } catch (e: Exception) {
                errorMessage = "Failed to load parliament data"
                e.printStackTrace()

            } finally {
                isLoading = false
            }
        }
    }
}

class MemberViewModelFactory(
    private val repository: MemberRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemberViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}