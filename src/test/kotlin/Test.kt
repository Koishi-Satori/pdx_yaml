import top.kkoishi.pml.core.Lexer
import top.kkoishi.pml.io.PDXYamlReader
import kotlin.io.path.Path
import kotlin.io.path.readText

@Suppress("CanBeVal")
fun main() {
    var time = System.currentTimeMillis()
    var lexer = Lexer(Path("/home/koishi/IdeaProjects/pdx_yaml/test.yml").readText().iterator())
    while (true) {
        lexer.scan() ?: break
        //println(token)
    }
    var cost = System.currentTimeMillis() - time
    println("Cost: $cost ms")
    time = System.currentTimeMillis()
    println(PDXYamlReader("/home/koishi/IdeaProjects/pdx_yaml/test.yml").readFull())
    cost = System.currentTimeMillis() - time
    println("Cost: $cost ms")
}