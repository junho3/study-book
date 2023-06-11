package com.example.study.chapter5.code

class Chapter554 {
    fun sendOwnerALetter(vehicle: Vehicle): String? {
        return getOwnersAddress(vehicle)
            ?.let { "SEND_LETTER" }
    }

    private fun getOwnersAddress(vehicle: Vehicle): Address? {
        if (vehicle.hasBeenScraped()) {
            return Address("SCRAPYARD_ADDRESS")
        }
        return null
    }
}

class Vehicle(private val scraped: Boolean) {
    fun hasBeenScraped(): Boolean {
        return scraped
    }
}

class Address(val address: String)
