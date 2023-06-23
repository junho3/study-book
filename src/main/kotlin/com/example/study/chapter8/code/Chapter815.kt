package com.example.study.chapter8.code

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
