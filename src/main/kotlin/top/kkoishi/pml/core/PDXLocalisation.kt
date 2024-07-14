package top.kkoishi.pml.core

import top.kkoishi.pml.io.PDXYamlOutput
import java.io.ByteArrayOutputStream
import java.io.StringBufferInputStream

/**
 * A class which represents a PDX localisation file.
 */
data class PDXLocalisation(
    var language: String,
    val comments: ArrayDeque<WithLineNumber<String>>,
    val entries: ArrayDeque<WithLineNumber<Entry>>,
) {
    data class WithLineNumber<T>(var value: T, var line: Int = -1, var column: Int = -1) {
        fun hasLineNumber() = line == -1
        fun hasColumnNumber() = column == -1
        override fun toString(): String = value.toString()
    }

    data class Entry(var key: String, var value: String, var number: Int = -1) {
        fun hasNumber() = number == -1
        override fun toString(): String = "$key:${if (number == -1) "" else number} $value"
    }

    internal class Parser(lexer: Lexer, ignoreComment: Boolean = true) :
        top.kkoishi.pml.core.Parser(lexer, ignoreComment) {
        private var key: String? = null
        private var num: Int = -1
        private var keyIndex = -1
        private val instance = PDXLocalisation("", ArrayDeque(), ArrayDeque())

        override fun processComment() {
            instance.comments.addLast(WithLineNumber(curToken!!.content, lexer.line, lexer.column))
        }

        override fun processLanguage() {
            instance.language = curToken!!.content
        }

        override fun processEntryKey() {
            keyIndex = lexer.column
            key = curToken!!.content
        }

        override fun processEntryNumber() {
            num = curToken!!.content.toInt()
        }

        override fun processEntryValue() {
            instance.entries.addLast(WithLineNumber(Entry(key!!, curToken!!.content, num), lexer.line, keyIndex))
            key = null
            num = -1
            keyIndex = -1
        }

        fun result() = instance
    }

    override fun toString(): String {
        val bos = ByteArrayOutputStream()
        PDXYamlOutput.formattedOutput(bos, this)
        val result = bos.toString()
        bos.close()
        return result
    }
}