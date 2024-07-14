package top.kkoishi.pml.core

import top.kkoishi.pml.exception.SyntaxException

/**
 * The syntax of pdx yaml:
 * ```
 * pdx_yaml -> l_tag entries | 'empty_set'
 *
 * l_tag -> language :
 *
 * language -> "l_english" | "l_braz_por" | "l_german" | "l_german"
 *           | "l_french" | "l_spanish" | "l_polish" | "l_russian"
 *           | "l_simp_chinese" | "l_japanese" | "l_korean"
 *
 * entries -> entries | entry
 *          | entry
 *          | 'empty_set'
 *
 * entry -> 'string' : number 'string'
 *        | 'string' : 'string'
 *
 * number -> number digit
 *         | digit
 *
 * digit -> "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" |
 * ```
 * The COMMENT tokens can be ignored.
 */
internal class Parser(val lexer: Lexer, val ignoreComment: Boolean = true) {
    private var curToken: Token? = null
    fun parse() {
        languageTag()
        entries()
    }

    private fun languageTag() {
        curToken = lexer.scan()
        verifyType(TokenType.LANGUAGE, "Require a language tag, but got $curToken")
        curToken = lexer.scan()
        verifyType(TokenType.COLON, "A language tag should contains a colon, but got $curToken")
    }

    private fun entries() {
        while (true) {
            curToken = lexer.scan()
            // if null, means there is no more tokens.
            // then we need to skip the comments.
            if (curToken == null)
                return
            if (curToken!!.type == TokenType.COMMENT)
                continue
            entry()
        }
    }

    private fun entry() {
        verifyType(
            TokenType.STRING,
            "A pdx yaml entry require a string as its key, but got $curToken"
        )
        curToken = lexer.scan()
        verifyType(
            TokenType.COLON,
            "A pdx yaml entry require a colon here, but got $curToken"
        )
        curToken = lexer.scan()
        if (curToken!!.type == TokenType.NUMBER) {
            curToken = lexer.scan()
        }
        verifyType(
            TokenType.STRING,
            "A pdx yaml entry require a string as its value, but got $curToken"
        )
    }

    private fun verifyType(requiredType: TokenType, message: String) {
        if (curToken == null || curToken!!.type != requiredType)
            throw SyntaxException("$message at (${lexer.line}:${lexer.column})")
    }
}