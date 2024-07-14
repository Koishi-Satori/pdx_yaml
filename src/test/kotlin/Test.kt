import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import top.kkoishi.pml.core.Lexer
import top.kkoishi.pml.io.PDXYamlReader
import top.kkoishi.pml.io.ReaderCharIterator
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.writeText

class Test {
    @Test
    @Timeout(225)
    fun testLexer() {
        val lexer = Lexer(ReaderCharIterator(Path("./test.yml").bufferedReader()))
        while (lexer.scan() != null) {
            // do nothing here
        }
    }

    @Test
    @Timeout(240)
    fun testReader1() {
        println(PDXYamlReader("./test.yml").readFull())
    }

    @Test
    @Timeout(240)
    fun testReader2() {
        val reader = PDXYamlReader("./test.yml")
        while (reader.readToken() != null) {
            // do nothing here
        }
    }

    @Test
    @Timeout(256)
    fun testOutput() {
        Path("./test.out").writeText(PDXYamlReader("./test.yml").readFull(false).toString())
    }
}
