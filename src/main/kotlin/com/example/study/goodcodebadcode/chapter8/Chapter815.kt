package com.example.study.goodcodebadcode.chapter8

import org.springframework.stereotype.Component

@Component
class Chapter815(
    private val fileValueReader: FileValueReader,
) {
    fun close(): Boolean {
        return fileValueReader.close()
    }
}

interface FileValueReader {
    fun close(): Boolean
}

@Component
class FileValueReaderImpl : FileValueReader {
    override fun close(): Boolean {
        return true
    }
}
