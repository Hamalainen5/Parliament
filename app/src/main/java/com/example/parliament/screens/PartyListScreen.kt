package com.example.parliament.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parliament.data.PartyWithMemberCount

@Composable
fun PartyListScreen(
    parties: List<PartyWithMemberCount>,
    favoriteMemberCount: Int,
    hasMajority: Boolean,
    onFavoriteChanged: (String, Boolean) -> Unit,
    onPartyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Favorite party members: $favoriteMemberCount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Text(
                text = if (hasMajority) {
                    "Majority government possible"
                } else {
                    "Majority government not possible"
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(parties) { party ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onPartyClick(party.code)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = party.code.uppercase(),
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "${party.memberCount} members",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Checkbox(
                        checked = party.favorite,
                        onCheckedChange = { checked ->
                            onFavoriteChanged(party.code, checked)
                        }
                    )
                }
            }
        }
    }
}