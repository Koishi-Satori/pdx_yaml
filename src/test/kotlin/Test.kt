import top.kkoishi.pml.core.Lexer
import top.kkoishi.pml.core.Parser
import kotlin.io.path.Path
import kotlin.io.path.readText

@Suppress("CanBeVal")
fun main () {
    var time = System.currentTimeMillis()
    var lexer = Lexer(Path("/home/koishi/IdeaProjects/pdx_yaml/test.yml").readText().iterator())
    while (true) {
        val token = lexer.scan() ?: break
        println(token)
    }
    var cost = System.currentTimeMillis() - time
    println("Cost: $cost ms")
    lexer = Lexer(Path("/home/koishi/IdeaProjects/pdx_yaml/test.yml").readText().iterator())
    Parser(lexer).parse()
}