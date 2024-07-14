package top.kkoishi.pml.io

import top.kkoishi.pml.core.Lexer
import top.kkoishi.pml.core.PDXLocalisation
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.nio.charset.Charset
import java.nio.file.Path
import kotlin.io.path.Path

class PDXYamlReader(private val reader: Reader) : Reader() {
    @JvmOverloads
    constructor(path: String, charset: Charset = Charsets.UTF_8) : this(Path(path), charset)

    @JvmOverloads
    constructor(file: Path, charset: Charset = Charsets.UTF_8) : this(file.toFile(), charset)

    @JvmOverloads
    constructor(file: File, charset: Charset = Charsets.UTF_8) : this(file.inputStream(), charset)

    @JvmOverloads
    constructor(inputStream: InputStream, charset: Charset = Charsets.UTF_8) : this(
        inputStream.buffered().reader(charset)
    )

    fun readFull(ignoreComment: Boolean = true): PDXLocalisation {
        val parser = PDXLocalisation.Parser(Lexer(ReaderCharIterator(reader)), ignoreComment)
        parser.parse()
        return parser.result()
    }

    fun readToken(): String? {
        val lexer = Lexer(ReaderCharIterator(reader))
        return lexer.scan()?.content
    }

    override fun read(cbuf: CharArray, off: Int, len: Int): Int =
        reader.read(cbuf, off, len)

    override fun close() {
        reader.close()
    }
}
