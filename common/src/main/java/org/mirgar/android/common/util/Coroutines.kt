package org.mirgar.android.common.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mirgar.android.common.R
import org.mirgar.android.common.exception.ExceptionWithResources
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

suspend fun loadFileContent(file: File) = withContext(Dispatchers.IO) {
    try {
        FileInputStream(file).use { it.readBytes() }
    } catch (_: FileNotFoundException) {
        throw ExceptionWithResources { getString(R.string.file_not_found, file.name) }
    }
}

