package com.example.parliament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.parliament.data.MemberRepository
import com.example.parliament.data.MemberViewModel
import com.example.parliament.data.MemberViewModelFactory
import com.example.parliament.data.ParliamentDatabase
import com.example.parliament.data.RetrofitInstance
import com.example.parliament.screens.MemberListScreen
import com.example.parliament.ui.theme.ParliamentTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {

    private val viewModel: MemberViewModel by viewModels {
        val database = ParliamentDatabase.getDatabase(this)

        val repository = MemberRepository(
            api = RetrofitInstance.api,
            dao = database.memberDao()
        )

        MemberViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParliamentTheme {
                Scaffold(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) { innerPadding ->

                    when {
                        viewModel.isLoading -> {
                            Column(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()

                                Text(
                                    text = "Loading parliament data..."
                                )
                            }
                        }

                        viewModel.errorMessage != null -> {
                            Column(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = viewModel.errorMessage!!
                                )
                            }
                        }

                        else -> {
                            MemberListScreen(
                                members = viewModel.members,
                                modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}