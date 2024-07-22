package top.kkoishi.pml.core

internal class Lexer(val rest: CharIterator) {
    private var prepeek = ArrayDeque<Char>()
    private var peek: Char = '\u0000'
    private val buf = StringBuilder()
    private val skipLineSep: () -> Boolean = when {
        Platform.isLinux -> {
            { skipLineSepLinux() }
        }

        Platform.isWindows -> {
            { skipLineSepWin() }
        }

        Platform.isMacos -> {
            { skipLineSepMac() }
        }

        else -> {
            { skipLineSepLinux() }
        }
    }
    var line: Int = 0
    var column: Int = 0

    private fun hasNext(): Boolean {
        return prepeek.isNotEmpty() || rest.hasNext()
    }

    private fun nextChar(): Char {
        column++
        if (prepeek.isNotEmpty())
            return prepeek.removeFirst()
        if (rest.hasNext())
            return rest.nextChar()
        throw NoSuchElementException("No more rest char.")
    }

    fun scan(): Token? {
        while (true) {
            if (!hasNext())
                return null
            peek = nextChar()
            // skip any blank character and line separator
            if (peek.isWhitespace()) {
                if (skipLineSep()) {
                    ++line
                    column = 0
                }
                continue
            }
            if (!skipLineSep())
                break
        }
        when (peek) {
            'l' -> return language()
            ':' -> return Token.COLON
            '"' -> return valueString()
            '#' -> return comment()
            else -> {
                if (peek.isDigit())
                    return number()
                return string()
            }
        }
    }

    private fun language(): Token {
        buf.clear()
        buf.append(peek)
        while (hasNext()) {
            peek = nextChar()
            if (peek == ':' || peek.isWhitespace() || peek == '\r' || peek == '\n') {
                prepeek.addLast(peek)
                break
            }
            buf.append(peek)
        }
        return when (val tag = buf.toString()) {
            "l_english", "l_braz_por", "l_german", "l_french",
            "l_spanish", "l_polish", "l_russian", "l_simp_chinese",
            "l_japanese", "l_korean" -> Token.token(tag, TokenType.LANGUAGE)

            else -> Token.token(tag, TokenType.STRING)
        }
    }

    private fun valueString(): Token {
        buf.clear()
        buf.append(peek)
        while (hasNext()) {
            peek = nextChar()
            if (peek == '\r' || peek == '\n' || peek == '#') {
                prepeek.addLast(peek)
                break
            }
            buf.append(peek)
        }
//        var crFlag = false
//
//        while (hasNext()) {
//            peek = nextChar()
//            when {
//                crFlag -> {
//                    buf.append(peek)
//                    crFlag = false
//                }
//
//                peek == '\\' -> {
//                    crFlag = true
//                    buf.append(peek)
//                }
//
//                peek == '"' -> {
//                    buf.append(peek)
//                    break
//                }
//
//                else -> buf.append(peek)
//            }
//        }
        return Token.token(buf.removeRange(buf.lastIndexOf('"') + 1, buf.length).toString(), TokenType.STRING)
    }

    private fun number(): Token {
        buf.clear()
        buf.append(peek)
        var keyFlag = false
        while (hasNext()) {
            peek = nextChar()
            if (!peek.isDigit()) {
                if (!peek.isWhitespace() && peek != '"' && peek != ':') {
                    if (!keyFlag)
                        keyFlag = true
                    buf.append(peek)
                    continue
                }
                if (peek == ':')
                    keyFlag = true
                prepeek.addLast(peek)
                break
            }
            buf.append(peek)
        }
        return Token.token(buf.toString(), if (keyFlag) TokenType.STRING else TokenType.NUMBER)
    }

    private fun string(): Token {
        buf.clear()
        buf.append(peek)
        while (hasNext()) {
            peek = nextChar()
            if (peek == ':' || peek.isWhitespace() || peek == '\r' || peek == '\n') {
                prepeek.addLast(peek)
                break
            }
            buf.append(peek)
        }
        return Token.token(buf.toString(), TokenType.STRING)
    }

    private fun comment(): Token {
        buf.clear()
        buf.append(peek)
        while (hasNext()) {
            peek = nextChar()
            if (peek == '\r' || peek == '\n') {
                prepeek.addLast(peek)
                break
            }
            buf.append(peek)
        }
        return Token.token(buf.toString(), TokenType.COMMENT)
    }

    /* Use for platform function reference */
    private fun skipLineSepLinux(): Boolean = peek == '\n'
    private fun skipLineSepWin(): Boolean = peek == '\n'
    private fun skipLineSepMac(): Boolean = peek == '\r'
}
