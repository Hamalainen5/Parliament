package com.example.parliament.data

import kotlinx.coroutines.flow.Flow

class MemberRepository(
    private val api: ParliamentApi,
    private val database: ParliamentDatabase
) {

    suspend fun refreshMembers() {
        val members = api.getMembers()

        val parties = members
            .map { member -> Party(code = member.party) }
            .distinctBy { party -> party.code }

        database.refreshData(
            members = members,
            parties = parties
        )
    }

    fun getMembersByParty(party: String): Flow<List<Member>> {
        return database.memberDao().getMembersByParty(party)
    }

    fun getPartiesWithMemberCount(): Flow<List<PartyWithMemberCount>> {
        return database.partyDao().getPartiesWithMemberCount()
    }

    suspend fun setFavorite(code: String, favorite: Boolean) {
        database.partyDao().setFavorite(code, favorite)
    }
}