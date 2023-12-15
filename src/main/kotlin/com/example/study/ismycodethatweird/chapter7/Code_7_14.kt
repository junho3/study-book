package com.example.study.ismycodethatweird.chapter7

class Code_7_14 {
    
    class Party(private val members: List<String> = listOf()) {
        
        fun add(newMember: String): Party {
            require(!isExist(newMember))
            require(!isFull())
            
            val adding: MutableList<String> = members.toMutableList()
            adding.add(newMember)
            
            return Party(adding)
        }
        
        private fun isExist(member: String): Boolean {
            return members.any { it == member }
        }
        
        private fun isFull(): Boolean {
            return members.size == MAX_MEMBER_COUNT
        }
        
        companion object {
            const val MAX_MEMBER_COUNT = 4
        }
    }
}
