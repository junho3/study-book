package com.example.study.goodcodebadcode.chapter4.code

import mu.KotlinLogging
import java.time.LocalDateTime

class Chapter4ResultTest {
    fun main(transactionAmount: Long): PaymentApiResponse {
        return callPaymentApi(transactionAmount = transactionAmount)
            .onFailure { logger.error { "ERROR ${it.message}" } }
            .onSuccess { logger.info { "SUCCESS: paymentNo: ${it.paymentNo} transactionAt: ${it.transactionAt}" } }
            .getOrThrow()
    }
    private fun callPaymentApi(transactionAmount: Long): Result<PaymentApiResponse> {
        return runCatching {
            logger.info { "payment-api 호출" }

            require(transactionAmount >= 0) { "결제금액이 음수 일 수 없습니다." }

            PaymentApiResponse(transactionAmount = transactionAmount)
        }
            .also { logger.info { "finally 구문" } }
    }
}

data class PaymentApiResponse(
    val paymentNo: String = "XXXXXXX",
    val transactionAmount: Long,
    val transactionAt: LocalDateTime = LocalDateTime.now(),
)

private val logger = KotlinLogging.logger {}
