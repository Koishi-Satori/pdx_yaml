package top.kkoishi.pml.core

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
 * Which listed above indicates that the token type consists of COLON, STRING, LANGUAGE, NUMBER.
 */
internal class Token private constructor(val content: String, val type: TokenType) {
    override fun toString(): String {
        return "<$content, $type>"
    }

    operator fun component1() = content

    operator fun component2() = type

    companion object {
        @JvmStatic
        val COLON: Token = token("", TokenType.COLON)

        @JvmStatic
        fun token(content: String, type: TokenType): Token {
            return Token(content, type)
        }
    }
}
