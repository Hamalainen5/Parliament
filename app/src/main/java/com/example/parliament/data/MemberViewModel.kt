package com.example.parliament.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MemberViewModel(
    private val repository: MemberRepository
) : ViewModel() {

    private val selectedParty = MutableStateFlow<String?>(null)

    val selectedPartyMembers =
        selectedParty
            .flatMapLatest { party ->
                if (party == null) {
                    flowOf(emptyList())
                } else {
                    repository.getMembersByParty(party)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val members: StateFlow<List<Member>> =
        repository.getMembers()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val parties: StateFlow<List<PartyWithMemberCount>> =
        repository.getPartiesWithMemberCount()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val favoriteMemberCount: StateFlow<Int> =
        parties
            .map { partyList ->
                partyList
                    .filter { it.favorite }
                    .sumOf { it.memberCount }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    val hasMajority: StateFlow<Boolean> =
        parties
            .map { partyList ->
                val totalMembers = partyList.sumOf { it.memberCount }
                val favoriteMembers = partyList
                    .filter { it.favorite }
                    .sumOf { it.memberCount }

                favoriteMembers > totalMembers / 2
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    fun selectParty(party: String) {
        selectedParty.value = party
    }

    fun clearSelectedParty() {
        selectedParty.value = null
    }

    fun setFavorite(code: String, favorite: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(code, favorite)
        }
    }

    init {
        refreshMembers()
    }

    private fun refreshMembers() {
        viewModelScope.launch {
            try {
                repository.refreshMembers()
            } catch (e: Exception) {
                e.printStackTrace()
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