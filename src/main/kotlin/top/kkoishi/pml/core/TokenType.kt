package top.kkoishi.pml.core

internal enum class TokenType(val hasContent: Boolean = true) {
    COLON(false),
    STRING,
    LANGUAGE,
    NUMBER,
    COMMENT,
}
