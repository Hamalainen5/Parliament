package com.example.parliament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parliament.data.MemberRepository
import com.example.parliament.data.MemberViewModel
import com.example.parliament.data.MemberViewModelFactory
import com.example.parliament.data.ParliamentDatabase
import com.example.parliament.data.RetrofitInstance
import com.example.parliament.screens.MemberListScreen
import com.example.parliament.screens.PartyListScreen
import com.example.parliament.ui.theme.ParliamentTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.parliament.screens.MemberPartyScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MemberViewModel by viewModels {
        val database = ParliamentDatabase.getDatabase(this)

        val repository = MemberRepository(
            api = RetrofitInstance.api,
            memberDao = database.memberDao(),
            partyDao = database.partyDao()
        )

        MemberViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParliamentTheme {

                val parties = viewModel.parties.collectAsStateWithLifecycle()

                val favoriteMemberCount =
                    viewModel.favoriteMemberCount.collectAsStateWithLifecycle()

                var selectedParty by remember {
                    mutableStateOf<String?>(null)
                }
                val partyMembers = viewModel.selectedPartyMembers.collectAsStateWithLifecycle()

                val hasMajority = viewModel.hasMajority.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (selectedParty == null) {
                        PartyListScreen(
                            parties = parties.value,
                            favoriteMemberCount = favoriteMemberCount.value,
                            hasMajority = hasMajority.value,
                            onFavoriteChanged = { code, favorite ->
                                viewModel.setFavorite(code, favorite)
                            },
                            onPartyClick = { party ->
                                selectedParty = party
                                viewModel.selectParty(party)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        MemberPartyScreen(
                            party = selectedParty!!,
                            members = partyMembers.value,
                            onBack = {
                                selectedParty = null
                                viewModel.clearSelectedParty()
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}