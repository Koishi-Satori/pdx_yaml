package top.kkoishi.pml.io

import top.kkoishi.pml.core.PDXLocalisation
import java.io.OutputStream

object PDXYamlOutput {
    fun formattedOutput(out: OutputStream, localisation: PDXLocalisation) {
        out.write(localisation.language.toByteArray())
        out.write(0x3A) // this is the char ':'
        out.write(0x0A) // this is the char '\n'

        val lines = ArrayDeque(localisation.comments)
        lines.addAll(localisation.entries.map {
            PDXLocalisation.WithLineNumber(
                it.value.toString(),
                it.line,
                it.column
            )
        })
        lines.sortBy { it.line }
        while (lines.isNotEmpty()) {
            out.write(0x20)// this is the char ' '
            out.write(lines.removeFirst().value.toByteArray())
            out.write(0x0A) // this is the char '\n'
        }
    }
}
