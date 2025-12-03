package com.gmail.wizaripost.seedFinder.logging

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class FileLogger {

    @Value("\${logging.max-size:10485760}") // 10MB по умолчанию
    private lateinit var maxSizeStr: String

    private val maxSize: Long
        get() = parseSize(maxSizeStr)

//    @Value("\${logging.directory:\"logs\"}") // 10MB по умолчанию
//    private lateinit var LOG_DIR: String

    private val LOG_DIR: String = "logs"

    private val loggers = mutableMapOf<String, LoggerInstance>()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val logDir = File(LOG_DIR)

    init {
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
        println("SimpleFileLogger initialized. Logs in: ${logDir.absolutePath}")
    }

    fun getLogger(clazz: Class<*>): Logger {
        val className = clazz.simpleName
        synchronized(this) {
            return loggers.getOrPut(className) {
                LoggerInstance(className)
            }
        }
    }

    inner class LoggerInstance(private val className: String) : Logger {
        private var writer: PrintWriter? = null
        private var currentFileIndex = 0
        private var currentFileSize = 0L
        private val classLogDir: File

        init {
            // Создаем папку для класса: logs/ClassName/
            classLogDir = File(logDir, className)
            if (!classLogDir.exists()) {
                classLogDir.mkdirs()
            }

            findLatestFile()
        }

        private fun findLatestFile() {
            val files = classLogDir.listFiles { it.name.startsWith("$className.log") } ?: emptyArray()

            if (files.isNotEmpty()) {
                var maxIndex = 0
                for (file in files) {
                    val index = when {
                        file.name == "$className.log" -> 0
                        file.name.startsWith("$className.log.") -> {
                            file.name.substringAfter("$className.log.").toIntOrNull() ?: 0
                        }
                        else -> 0
                    }
                    if (index > maxIndex) maxIndex = index
                }

                currentFileIndex = maxIndex
                val currentFile = getCurrentFile()
                if (currentFile.exists()) {
                    currentFileSize = currentFile.length()
                    if (currentFileSize >= maxSize) {
                        createNewFile()
                    }
                }
            }
        }

        private fun getCurrentFileName(): String {
            return if (currentFileIndex == 0) "$className.log" else "$className$currentFileIndex.log"
        }

        private fun getCurrentFile(): File = File(classLogDir, getCurrentFileName())

        private fun createNewFile() {
            writer?.close()
            writer = null
            currentFileIndex++
            currentFileSize = 0L
            // Тихое создание нового файла
        }

        override fun info(msg: String) = write(msg, "INFO")
        override fun debug(msg: String) = write(msg, "DEBUG")
        override fun warn(msg: String) = write(msg, "WARN")
        override fun error(msg: String) = write(msg, "ERROR")

        override fun error(msg: String, e: Throwable) {
            write("$msg: ${e.message}", "ERROR")
            // Выводим только ошибки в консоль
            val consoleMsg = "\u001B[31m[$className] $msg: ${e.message}\u001B[0m"
            println(consoleMsg)
            e.printStackTrace()
        }

        private fun write(message: String, level: String) {
            synchronized(this) {
                // Проверяем размер
                if (currentFileSize >= maxSize) {
                    createNewFile()
                }

                // Открываем файл если нужно
                if (writer == null) {
                    val file = getCurrentFile()
                    writer = PrintWriter(FileOutputStream(file, true), true)
                }

                // Форматируем
                val timestamp = LocalDateTime.now().format(dateFormatter)
                val logMessage = "[$timestamp] [$level] $message"

                // Пишем в файл
                writer?.println(logMessage)
                currentFileSize += logMessage.length + 1

                // Выводим в консоль
                val consoleMsg = when (level) {
                    "ERROR" -> "\u001B[31m[$className] $logMessage\u001B[0m"
                    "WARN" -> "\u001B[33m[$className] $logMessage\u001B[0m"
                    "DEBUG" -> "\u001B[36m[$className] $logMessage\u001B[0m"
                    else -> "[$className] $logMessage"
                }
                // Спам в консоль
//                println(consoleMsg)
            }
        }

        fun close() {
            writer?.close()
        }
    }
}

interface Logger {
    fun info(msg: String)
    fun debug(msg: String)
    fun warn(msg: String)
    fun error(msg: String)
    fun error(msg: String, e: Throwable)
}

private fun parseSize(sizeStr: String): Long {
    // Регулярное выражение для поиска чисел и единиц измерения
    val regex = Regex("""(\d+)\s*([KMG]?B)?""", RegexOption.IGNORE_CASE)
    val match = regex.find(sizeStr)

    return if (match != null) {
        val value = match.groupValues[1].toLong()
        val unit = match.groupValues[2].uppercase()

        when (unit) {
            "KB" -> value * 1024
            "MB" -> value * 1024 * 1024
            "GB" -> value * 1024 * 1024 * 1024
            "B" -> value
            "" -> value // если единица не указана, считаем байтами
            else -> value // по умолчанию байты
        }
    } else {
        // Если не распарсилось, пытаемся как число
        sizeStr.toLongOrNull() ?: 10 * 1024 * 1024
    }
}