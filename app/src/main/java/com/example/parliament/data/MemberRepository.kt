package com.example.parliament.data

import kotlinx.coroutines.flow.Flow

class MemberRepository(
    private val api: ParliamentApi,
    private val memberDao: MemberDao,
    private val partyDao: PartyDao
) {

    suspend fun refreshMembers() {
        val members = api.getMembers()

        memberDao.deleteAllMembers()
        memberDao.insertMembers(members)

        val parties = members
            .map { member -> Party(code = member.party) }
            .distinctBy { party -> party.code }

        partyDao.insertParties(parties)
    }

    fun getMembers(): Flow<List<Member>> {
        return memberDao.getAllMembers()
    }

    fun getMembersByParty(party: String): Flow<List<Member>> {
        return memberDao.getMembersByParty(party)
    }

    fun getPartiesWithMemberCount(): Flow<List<PartyWithMemberCount>> {
        return partyDao.getPartiesWithMemberCount()
    }

    suspend fun setFavorite(code: String, favorite: Boolean) {
        partyDao.setFavorite(code, favorite)
    }
}