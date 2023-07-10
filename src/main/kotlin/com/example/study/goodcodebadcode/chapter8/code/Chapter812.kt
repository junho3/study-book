package com.example.study.goodcodebadcode.chapter8.code

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class Chapter812(
    private val createSlipUseCaseFactory: CreateSlipUseCaseFactory,
) {
    fun create(paymentGateway: PaymentGateway): Slip {
        return createSlipUseCaseFactory.create(paymentGateway)
    }

    enum class PaymentGateway {
        KAKAOPAY, NAVERPAY
    }
}

@Component
class CreateSlipUseCaseFactory(
    private val createKakaopaySlipService: CreateKakaopaySlipService,
    private val createNaverpaySlipService: CreateNaverpaySlipService,
) {
    fun create(paymentGateway: Chapter812.PaymentGateway): Slip {
        return when (paymentGateway) {
            Chapter812.PaymentGateway.KAKAOPAY -> createKakaopaySlipService.create()
            Chapter812.PaymentGateway.NAVERPAY -> createNaverpaySlipService.create()
        }
    }
}

interface CreateSlipUseCase {
    fun create(): Slip
}

data class Slip(val name: String)

@Service
class CreateKakaopaySlipService : CreateSlipUseCase {
    override fun create(): Slip {
        return Slip("카카오페이")
    }
}

@Service
class CreateNaverpaySlipService : CreateSlipUseCase {
    override fun create(): Slip {
        return Slip("네이버페이")
    }
}
