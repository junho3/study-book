package com.example.study.ismycodethatweird.chapter6


class Code_6_49 {
    
    data class OrderHistory(
        val totalAmount: Long,
        val frequency: Long,
        val returnRate: Double,
    )
    
    interface CustomerRule {
        fun ok(orderHistory: OrderHistory): Boolean
    }
    
    class GoldCustomerTotalAmountRule : CustomerRule {
        override fun ok(orderHistory: OrderHistory): Boolean {
            return 1000000 <= orderHistory.totalAmount
        }
    }
    
    class GoldCustomerFrequencyRule : CustomerRule {
        override fun ok(orderHistory: OrderHistory): Boolean {
            return 10 < orderHistory.frequency
        }
    }
    
    class GoldCustomerReturnRateRule : CustomerRule {
        override fun ok(orderHistory: OrderHistory): Boolean {
            return orderHistory.returnRate <= 0.001
        }
    }
    
    class CustomerPolicy {
        private val rules: MutableSet<CustomerRule> = mutableSetOf()

        fun add(customerRule: CustomerRule) {
            rules.add(customerRule)
        }
        
        fun complyWithAll(orderHistory: OrderHistory): Boolean {
            rules.forEach { 
                if (!it.ok(orderHistory)) {
                    return false
                }
            }
            
            return true
        }
    }
    
    class GoldCustomerPolicy(
        private val policy: CustomerPolicy = CustomerPolicy()
    ) {
        init {
            policy.add(GoldCustomerTotalAmountRule())
            policy.add(GoldCustomerFrequencyRule())
            policy.add(GoldCustomerReturnRateRule())
        }
        
        fun complyWithAll(history: OrderHistory): Boolean {
            return policy.complyWithAll(history)
        }
    }
}
