package com.example.study.ismycodethatweird.chapter7

class Code_7_14 {
    
    class Party(private val members: MutableList<String> = mutableListOf()) {
        
        fun add(newMember: String): Party {
            require(!isExist(newMember))
            require(!isFull())
            
            val adding: MutableList<String> = members
            adding.add(newMember)
            
            return Party(adding)
        }
        
        private fun isExist(member: String): Boolean {
            return members.any { it == member }
        }
        
        private fun isFull(): Boolean {
            return members.size == MAX_MEMBER_COUNT
        }
        
        fun members(): List<String> {
            return members
        }
        
        companion object {
            const val MAX_MEMBER_COUNT = 4
        }
    }
}
