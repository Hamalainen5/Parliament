package com.example.parliament.data

class MemberRepository(
    private val api: ParliamentApi,
    private val dao: MemberDao
) {

    suspend fun refreshMembers() {
        val members = api.getMembers()

        dao.deleteAllMembers()
        dao.insertMembers(members)
    }

    suspend fun getMembers(): List<Member> {
        return dao.getAllMembers()
    }
}