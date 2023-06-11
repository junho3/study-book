package com.example.study.chapter6.code

class Chapter662Sealed {
    sealed class SlipRule(
        val debitAccountSubjectCode: String, // 차변
        val creditAccountSubjectCode: String, // 대변
    )

    object ApproveKakaoPayCardPayment : SlipRule(
        debitAccountSubjectCode = "카카오페이_결제승인_차변코드",
        creditAccountSubjectCode = "카카오페이_결제승인_대변코드",
    )

    object CancelKakaoPayPayment : SlipRule(
        debitAccountSubjectCode = "카카오페이_결제취소_차변코드",
        creditAccountSubjectCode = "카카오페이_결제취소_대변코드",
    )

    fun findSlipRule(slipRule: SlipRule): String {
        return when (slipRule) {
            ApproveKakaoPayCardPayment -> "카카오페이_결제승인"
            CancelKakaoPayPayment -> "카카오페이_결제취소"
        }
    }
}
